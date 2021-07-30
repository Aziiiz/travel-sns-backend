package io.noster.TravelSns.service;

import io.noster.TravelSns.model.FacebookAuthModel;
import io.noster.TravelSns.model.FacebookUserModel;
import io.noster.common.basic.BasicResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

public interface AuthService {

    ResponseEntity<?> facebook(FacebookAuthModel facebookAuthModel) throws ResponseStatusException;
    ResponseEntity<?> google(FacebookAuthModel facebookAuthModel);
    ResponseEntity<?> getUserDetails(Authentication authentication);
}
