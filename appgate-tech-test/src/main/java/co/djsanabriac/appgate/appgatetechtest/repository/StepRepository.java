package co.djsanabriac.appgate.appgatetechtest.repository;

import co.djsanabriac.appgate.appgatetechtest.model.entity.Step;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StepRepository extends CrudRepository<Step, Integer> {

    List<Step> getAllBySessionSid(String sid);

}
