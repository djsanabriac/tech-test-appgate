package co.djsanabriac.appgate.appgatetechtest.repository;

import co.djsanabriac.appgate.appgatetechtest.model.entity.Session;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends CrudRepository<Session, Integer> {

    Session getBySid(String sid);

}
