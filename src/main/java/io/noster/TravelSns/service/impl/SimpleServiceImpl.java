package io.noster.TravelSns.service.impl;

import io.noster.TravelSns.payload.MessageResponse;
import io.noster.TravelSns.service.SimpleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SimpleServiceImpl implements SimpleService {


    @Override
    public ResponseEntity<MessageResponse> getSimple() {
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Hello world"));
    }
}