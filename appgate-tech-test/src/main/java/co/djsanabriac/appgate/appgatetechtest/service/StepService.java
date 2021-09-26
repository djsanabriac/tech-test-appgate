package co.djsanabriac.appgate.appgatetechtest.service;

import co.djsanabriac.appgate.appgatetechtest.controller.OperationController;
import co.djsanabriac.appgate.appgatetechtest.model.dto.StepDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.NotSupportedException;
import java.util.List;
import java.util.Locale;

@Service
public class StepService {

    Logger logger = LoggerFactory.getLogger(StepService.class);

    public static List<StepDTO> processStep(String sid, StepDTO step, List<StepDTO> stepDTOS) {

        switch (step.getType().toLowerCase()){
            case "operand":
                step.setExecuted(false);
                stepDTOS.add(step);
                break;
            case "operation":
                step.setExecuted(false);
                stepDTOS.add(step);
                processOperation(stepDTOS);
                break;
            default:
                break;

        }

        return stepDTOS;
    }

    private static void processOperation(List<StepDTO> stepDTOS) {

        StepDTO operation = stepDTOS.get(stepDTOS.size()-1);
        Double result = null;
        StepDTO toAdd = null;
        for (StepDTO s :
                stepDTOS) {

            if( s.getExecuted() ) continue;

            //In case of exception, mark next operands as executed
            if( operation.getType().equals("exception") ){
                s.setExecuted(true);
                continue;
            }

            if( s.equals(operation) ) break;

            if( s.getType().equals("exception") ){
                toAdd = new StepDTO("exception", "Operation not supported", true);
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
                toAdd = new StepDTO("exception", "Operation not supported", true);
                operation.setExecuted(true);
                operation = toAdd;
            }

            s.setExecuted(true);

        }

        operation.setExecuted(true);

        if(toAdd != null) {
            stepDTOS.add(toAdd);
            return;
        }

        if( result != null ){
            toAdd = new StepDTO("result", result.toString(), false);
            stepDTOS.add(toAdd);
        }

    }

    private static Double executeOperation(StepDTO s, Double result, StepDTO operation) throws NotSupportedException {

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
