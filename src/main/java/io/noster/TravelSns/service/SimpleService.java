package io.noster.TravelSns.service;

import io.noster.TravelSns.payload.MessageResponse;
import org.springframework.http.ResponseEntity;

public interface SimpleService {

    ResponseEntity<MessageResponse> getSimple();
}
