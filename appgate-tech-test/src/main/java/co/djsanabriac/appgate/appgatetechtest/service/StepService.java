package co.djsanabriac.appgate.appgatetechtest.service;

import co.djsanabriac.appgate.appgatetechtest.controller.OperationController;
import co.djsanabriac.appgate.appgatetechtest.model.dto.StepDTO;
import co.djsanabriac.appgate.appgatetechtest.model.entity.Session;
import co.djsanabriac.appgate.appgatetechtest.model.entity.Step;
import co.djsanabriac.appgate.appgatetechtest.repository.SessionRepository;
import co.djsanabriac.appgate.appgatetechtest.repository.StepRepository;
import org.hibernate.cache.spi.entry.StructuredCacheEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.NotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class StepService {

    Logger logger = LoggerFactory.getLogger(StepService.class);

    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private StepRepository stepRepository;

    public String getSession(){

        String uuid = null;

        do {
            uuid = UUID.randomUUID().toString();
        }while ( sessionRepository.getBySid(uuid) != null );

        Session s = new Session();
        s.setSid(uuid);
        sessionRepository.save(s);

        return uuid;

    }

    public List<StepDTO> processStep(String sid, StepDTO stepDTO) {

        switch (stepDTO.getType().toLowerCase()){
            case "operand":
                saveStep(sid, stepDTO);
                break;
            case "operation":
                saveStep(sid, stepDTO);
                processOperation(sid);
                break;
            default:
                break;

        }

        List<StepDTO> stepDTOS = getStepDTOS(sid);

        return stepDTOS;
    }

    public List<StepDTO> getStepDTOS(String sid) {
        List<Step> steps = stepRepository.getAllBySessionSid(sid);

        List<StepDTO> stepDTOS = new ArrayList<>();

        steps.forEach(step -> { stepDTOS.add(new StepDTO(step.getType(),step.getValue(),step.getExecuted())); });
        return stepDTOS;
    }

    private void saveStep(String sid, StepDTO stepDTO) {
        stepDTO.setExecuted(false);
        Step step = new Step().fromStepDTO(stepDTO);
        step.setSid(sessionRepository.getBySid(sid));
        stepRepository.save(step);
    }

    private void processOperation(String sid) {

        List<Step> steps = stepRepository.getAllBySessionSid(sid);

        Step operation = steps.get(steps.size()-1);
        Double result = null;
        Step toAdd = null;
        for (Step s :
                steps) {

            if( s.getExecuted() ) continue;

            //In case of exception, mark next operands as executed
            if( operation.getType().equals("exception") ){
                s.setExecuted(true);
                continue;
            }

            if( s.equals(operation) ) break;

            if( s.getType().equals("exception") ){
                toAdd = new Step(null, "exception", "Operation not supported", false, sessionRepository.getBySid(sid));
                operation.setExecuted(true);
                operation = toAdd;
                break;
            }

            if( result == null || s.getType().equals("result") ){
                result = Double.parseDouble(s.getValue());
                s.setExecuted(true);
                continue;
            }

            try{
               result = executeOperation(s, result, operation);
            }catch (NotSupportedException e){
                toAdd = new Step(null,"exception", "Operation not supported", true, sessionRepository.getBySid(sid));
                operation.setExecuted(true);
                operation = toAdd;
            }

            s.setExecuted(true);

        }

        operation.setExecuted(true);

        if(toAdd != null) {
            stepRepository.save(toAdd);
            return;
        }

        if( result != null ){
            toAdd = new Step(null,"result", result.toString(), false, sessionRepository.getBySid(sid));
            stepRepository.save(toAdd);
        }

    }

    private static Double executeOperation(Step s, Double result, Step operation) throws NotSupportedException {

        Double value = Double.parseDouble(s.getValue());

        switch (operation.getValue()){
            case "sum":
                result = result + value;
                break;
            case "rest":
                result = result - value;
                break;
            case "multiplication":
                result = result * value;
                break;
            case "division":
                if(value.equals(0D)) throw new NotSupportedException();
                result = result / value ;
                break;
        }

        return result;

    }
}
