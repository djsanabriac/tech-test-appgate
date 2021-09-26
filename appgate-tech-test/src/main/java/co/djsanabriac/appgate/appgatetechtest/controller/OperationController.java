package co.djsanabriac.appgate.appgatetechtest.controller;

import co.djsanabriac.appgate.appgatetechtest.model.dto.GeneralResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class OperationController {

    Logger logger = LoggerFactory.getLogger(OperationController.class);

    List<String> sessionList = new ArrayList<>();

    @GetMapping("/operation/session")
    ResponseEntity getSession(){

        String uuid = null;
        do {
            uuid = UUID.randomUUID().toString();
        }while ( sessionList.contains(uuid) );

        sessionList.add(uuid);
        logger.info(sessionList.toString());

        return ResponseEntity.ok(new GeneralResponse<>(true, "OK", uuid).toMap());
    }

}
