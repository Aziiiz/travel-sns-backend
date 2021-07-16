package io.noster.TravelSns.service.impl;

import io.noster.TravelSns.dao.UserMapper;
import io.noster.TravelSns.model.*;
import io.noster.TravelSns.payload.LoginMethodEnum;
import io.noster.TravelSns.payload.LoginResponse;
import io.noster.TravelSns.security.jwt.JwtTokenProvider;
import io.noster.TravelSns.service.AuthService;
import net.bytebuddy.utility.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.Properties;

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
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final String TOKEN_PREFIX  = "Bearer ";

    @Override
    public ResponseEntity<?> facebook(FacebookAuthModel facebookAuthModel) {
        String templateUrl = String.format(facebookAuthUrl, facebookAuthModel.getAuthToken());
        FacebookUserModel facebookUserModel = webClient.get().uri(templateUrl).retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> {
                    throw new ResponseStatusException(clientResponse.statusCode(), "facebook login error");
                })
                .bodyToMono(FacebookUserModel.class)
                .block();
        logger.info("Print USSER +++ ",facebookUserModel.getLastName());
        final Optional<User> userOptional = userMapper.findByEmail(facebookUserModel.getEmail());

        if(userOptional.isEmpty()) {
            final User user = new User(facebookUserModel.getEmail(), new RandomString(10).nextString(), LoginMethodEnum.FACEBOOK, "ROLE_USER");
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
}
