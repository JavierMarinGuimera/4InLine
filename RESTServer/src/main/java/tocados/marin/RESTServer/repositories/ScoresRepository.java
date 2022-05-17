package tocados.marin.RESTServer.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tocados.marin.RESTServer.models.Score;

@Repository
public interface ScoresRepository extends CrudRepository<Score, Integer> {
}
