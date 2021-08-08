package io.noster.TravelSns.service.impl;

import io.noster.TravelSns.dao.UserMapper;
import io.noster.TravelSns.model.*;
import io.noster.TravelSns.payload.LoginMethodEnum;
import io.noster.TravelSns.payload.LoginResponse;
import io.noster.TravelSns.payload.MessageResponse;
import io.noster.TravelSns.security.jwt.JwtTokenProvider;
import io.noster.TravelSns.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WebClient webClient;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Value("${jwt.auth.facebook}")
    private String facebookAuthUrl;

    @Value("${jwt.auth.google}")
    private String googleAuthUrl;
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final String TOKEN_PREFIX  = "Bearer ";

    @Override
    public ResponseEntity<?> facebook(FacebookAuthModel facebookAuthModel)  throws ResponseStatusException {
        String templateUrl = String.format(facebookAuthUrl, facebookAuthModel.getAuthToken());
        FacebookUserModel facebookUserModel = webClient.get().uri(templateUrl).retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> {
                    throw new ResponseStatusException(clientResponse.statusCode(), "facebook login error");
                })
                .bodyToMono(FacebookUserModel.class)
                .block();
        final Optional<User> userOptional = userMapper.findByEmail(facebookUserModel.getEmail());

        if(userOptional.isEmpty()) {
            final User user = new User(facebookUserModel.getEmail(), LoginMethodEnum.FACEBOOK, "ROLE_USER", facebookUserModel.getFirstName());
            userMapper.save(user);
            final UserPrincipal userPrincipal = new UserPrincipal(user);
            String jwt = tokenProvider.generateToken(userPrincipal);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentContextPath().path("/api/v1/user/{username}")
                    .buildAndExpand(facebookUserModel.getFirstName()).toUri();


            return ResponseEntity.created(location).body(new LoginResponse(TOKEN_PREFIX + jwt));
        }else {
            final User user = userOptional.get();
            if ((user.getLoginMethodEnum() != LoginMethodEnum.FACEBOOK)) {
                return ResponseEntity.badRequest().body("previously logged in with different method");
            }

            UserPrincipal userPrincipal = new UserPrincipal(user);
            String jwt = tokenProvider.generateTokenWithPrinciple(userPrincipal);
            return ResponseEntity.ok(new LoginResponse(TOKEN_PREFIX + jwt));
        }
    }

    @Override
    public ResponseEntity<?> google(FacebookAuthModel facebookAuthModel) {
        logger.info("GOOGLE AUTH Token " + facebookAuthModel.getAuthToken());
        String googleUrl = String.format(googleAuthUrl, facebookAuthModel.getAuthToken());
        GoogleUserModel googleUserModel = webClient.get().uri(googleUrl).retrieve()
            .onStatus(HttpStatus::isError, clientResponse -> {
                throw new ResponseStatusException(clientResponse.statusCode(), "google login error");
            })
            .bodyToMono(GoogleUserModel.class)
            .block();

        if (googleUserModel.getEmail().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Email is empty"));
        }
        final Optional<User> userOptional = userMapper.findByEmail(googleUserModel.getEmail());

        if(userOptional.isEmpty()) {
            final User user = new User(googleUserModel.getEmail(), LoginMethodEnum.GOOGLE, "ROLE_USER", googleUserModel.getName());
            userMapper.save(user);
            final UserPrincipal userPrincipal = new UserPrincipal(user);
            String jwt = tokenProvider.generateToken(userPrincipal);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentContextPath().path("/api/v1/user/{username}")
                    .buildAndExpand(googleUserModel.getFirstName()).toUri();
            return ResponseEntity.created(location).body(new LoginResponse(TOKEN_PREFIX + jwt));
        }else {
            final User user = userOptional.get();
            if((user.getLoginMethodEnum() != LoginMethodEnum.GOOGLE)) {
                return ResponseEntity.badRequest().body("previously logged in with different method");
            }
            UserPrincipal userPrincipal = new UserPrincipal(user);
            String jwt = tokenProvider.generateTokenWithPrinciple(userPrincipal);
            return ResponseEntity.ok(new LoginResponse(TOKEN_PREFIX + jwt));
        }
    }

    @Override
    public ResponseEntity<?> getUserDetails(Authentication authentication) {
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            return ResponseEntity.ok().body(userMapper.findByEmail(userDetails.getUsername()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse(e.getMessage()));
        }
    }
}
