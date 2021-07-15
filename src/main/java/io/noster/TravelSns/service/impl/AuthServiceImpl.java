package io.noster.TravelSns.service.impl;

import io.noster.TravelSns.model.FacebookAuthModel;
import io.noster.TravelSns.model.User;
import io.noster.TravelSns.payload.LoginResponse;
import io.noster.TravelSns.security.jwt.JwtTokenProvider;
import io.noster.TravelSns.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.nio.file.attribute.UserPrincipal;
import java.util.Optional;
import java.util.Properties;

public class AuthServiceImpl implements AuthService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    public ResponseEntity<?> facebook(FacebookAuthModel facebookAuthModel) {
        String templateUrl = String.format(Properties.FACEBOOK_AUTH_URL, facebookAuthModel.getAuthToken());
        FacebookAuthModel facebookAuthModel1 = webClient.get().uri(templateUrl).retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> {
                    throw new ResponseStatusException(clientResponse.statusCode(), "facebook login error");
                })
                .bodyToMono(FacebookAuthModel.class)
                .block();

        final Optional<User> userOptional = userRepository.findByEmail(facebookAuthModel.getEmail());

        if(userOptional.isEmpty()) {
            final User user = new User(facebookAuthModel.getEmail(), new RendomString(10).nextString(), LoginMethodEnum.FACEBOOK, "ROLE_USER");
            userRepository.save(user);
            final UserPrincipal userPrincipal = new UserPrincipal(user);
            String jwt = tokenProvider.generateToken(userPrincipal);
            URI location = ServletUriComponentsBuilder
                    .fromContextPath().path("/api/v1/user/{username}")
                    .buildAndExpand(facebookAuthModel.getFirstName()).toUri();


            return ResponseEntity.created(location).body(new LoginResponse(Properties.TOKEN_PREFIX + jwt));
        }else {
            final User user = userOptional.get();
            if ((user.getLoginMethodEnum() != LoginMethodEnum.FACEBOOK)) {
                return ResponseEntity.badRequest().body("previously logged in with different method");
            }

            UserPrincipal userPrincipal = new UserPrincipal(user);
            String jwt = tokenProvider.generateTokenWithPrinciple(userPrincipal);
            return ResponseEntity.ok(new LoginResponse(Properties.TOKEN_PREFIX + jwt));
        }
        return null;
    }
}
