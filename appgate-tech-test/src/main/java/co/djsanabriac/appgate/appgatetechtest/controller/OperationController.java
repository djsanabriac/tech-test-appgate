package co.djsanabriac.appgate.appgatetechtest.controller;

import co.djsanabriac.appgate.appgatetechtest.model.dto.GeneralResponse;
import co.djsanabriac.appgate.appgatetechtest.model.dto.StepDTO;
import co.djsanabriac.appgate.appgatetechtest.service.StepService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class OperationController {

    Logger logger = LoggerFactory.getLogger(OperationController.class);

    List<String> sessionList = new ArrayList<>();
    Map<String, List<StepDTO>> sessionStepsMap = new HashMap();

    @Autowired
    StepService stepService;

    @GetMapping("/operation/session")
    ResponseEntity getSession(){

        String uuid = null;
        do {
            uuid = UUID.randomUUID().toString();
        }while ( sessionStepsMap.keySet().contains(uuid) );

        sessionStepsMap.put(uuid, new ArrayList<>());
        logger.info(sessionStepsMap.toString());

        return ResponseEntity.ok(new GeneralResponse<>(true, "OK", uuid).toMap());
    }

    @PostMapping("/operation/step")
    ResponseEntity step(@RequestHeader("sid") String sid, @RequestBody(required = true) StepDTO step){

        if( !sessionStepsMap.keySet().contains(sid) )
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new GeneralResponse<>(false, "INVALID_SID").toMap());

        stepService.processStep(sid, step, sessionStepsMap.get(sid));

        logger.info(sessionStepsMap.toString());

        return ResponseEntity.ok(new GeneralResponse<>(true, "OK",sessionStepsMap.get(sid)).toMap());
    }

}
