package io.noster.TravelSns.controller;


import io.noster.TravelSns.model.FacebookAuthModel;
import io.noster.TravelSns.service.AuthService;
import io.noster.TravelSns.service.impl.AuthServiceImpl;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;


@RestController
@Api(tags = {"AUTH API"})
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);



    @PostMapping("/login/facebook")
    public ResponseEntity<?> facebook(@RequestBody  FacebookAuthModel facebookAuthModel) {
        return authService.facebook(facebookAuthModel);
    }

    @PostMapping("/login/google")
    public ResponseEntity<?> google(@RequestBody  FacebookAuthModel facebookAuthModel) {
            return authService.google(facebookAuthModel);
    }
    @GetMapping("details")
    public  ResponseEntity<?> authentication(@CurrentSecurityContext(expression = "authentication")Authentication authentication) {
        return authService.getUserDetails(authentication);
    }
}
