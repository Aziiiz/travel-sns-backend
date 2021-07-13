package io.noster.TravelSns.controller;


import io.noster.TravelSns.payload.MessageResponse;
import io.noster.TravelSns.service.impl.SimpleServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class sampleController {

    final SimpleServiceImpl simple;

    public sampleController(SimpleServiceImpl simple) {
        this.simple = simple;
    }

    @GetMapping("/")
    private ResponseEntity<MessageResponse> simpleGet() {
            return simple.getSimple();
    }
}
