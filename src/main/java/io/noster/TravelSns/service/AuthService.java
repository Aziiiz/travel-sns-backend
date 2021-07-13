package io.noster.TravelSns.service;

import io.noster.TravelSns.model.FacebookAuthModel;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<?> facebook(FacebookAuthModel facebookAuthModel);
}
