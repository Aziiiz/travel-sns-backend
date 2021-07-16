package io.noster.TravelSns.controller;


import io.noster.TravelSns.model.FacebookAuthModel;
import io.noster.TravelSns.service.impl.AuthServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    final AuthServiceImpl authServiceimpl;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthServiceImpl authServiceimpl) {
        this.authServiceimpl = authServiceimpl;
    }


    @PostMapping("login/facebook")
    public ResponseEntity<?> facebook(@RequestBody  FacebookAuthModel facebookAuthModel) {
        return authServiceimpl.facebook(facebookAuthModel);
    }

    @PostMapping("login/google")
    public ResponseEntity<?> google(@RequestBody  FacebookAuthModel facebookAuthModel) {
        try {
            return authServiceimpl.google(facebookAuthModel);

        }catch (Exception e) {
            logger.info("print error " + e.getMessage());
            return null;
        }
    }
}
