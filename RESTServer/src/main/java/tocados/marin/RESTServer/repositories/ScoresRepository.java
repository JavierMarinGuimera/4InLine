package tocados.marin.RESTServer.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tocados.marin.RESTServer.models.score.Score;
import tocados.marin.RESTServer.models.user.User;

@Repository
public interface ScoresRepository extends CrudRepository<Score, Integer> {
    public abstract List<Score> findScoresByUser(User user);
}
