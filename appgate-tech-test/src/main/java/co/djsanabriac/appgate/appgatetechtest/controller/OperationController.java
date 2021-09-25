package co.djsanabriac.appgate.appgatetechtest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OperationController {

    @GetMapping("/operation/session")
    ResponseEntity<String> getSession(){
        return ResponseEntity.ok("Session is working");
    }

}
