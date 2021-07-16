package io.noster.TravelSns.service;

import io.noster.TravelSns.model.FacebookAuthModel;
import io.noster.TravelSns.model.FacebookUserModel;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<?> facebook(FacebookAuthModel facebookAuthModel);
    ResponseEntity<?> google(FacebookAuthModel facebookAuthModel);
}
