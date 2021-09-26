package co.djsanabriac.appgate.appgatetechtest.controller;

import co.djsanabriac.appgate.appgatetechtest.model.dto.GeneralResponse;
import co.djsanabriac.appgate.appgatetechtest.model.dto.StepDTO;
import co.djsanabriac.appgate.appgatetechtest.model.entity.Step;
import co.djsanabriac.appgate.appgatetechtest.repository.SessionRepository;
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

    @Autowired
    SessionRepository sessionRepository;

    @GetMapping("/operation/session")
    ResponseEntity getSession(){
        String uuid = stepService.getSession();
        return ResponseEntity.ok(
                new GeneralResponse<>(uuid != null,
                        uuid != null ? "OK": "FAILED_SESSION", uuid).toMap());
    }

    @PostMapping("/operation/step")
    ResponseEntity step(@RequestHeader("sid") String sid, @RequestBody(required = true) StepDTO step){

        if( sessionRepository.getBySid(sid) == null )
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new GeneralResponse<>(false, "INVALID_SID").toMap());

        List<StepDTO> steps = stepService.processStep(sid, step);

        logger.info(steps.toString());

        return ResponseEntity.ok(new GeneralResponse<>(true, "OK", steps).toMap());
    }

}
