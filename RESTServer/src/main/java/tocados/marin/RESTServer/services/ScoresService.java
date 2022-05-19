package tocados.marin.RESTServer.services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import tocados.marin.RESTServer.models.score.ScoreDTO;
import tocados.marin.RESTServer.models.user.User;

@Service
public interface ScoresService {

    /**
     * ------------------------------------------------------------------------------------
     * GET Methods:
     */

    public List<ScoreDTO> getTopScores();

    public List<ScoreDTO> getUserTopScores(User user);

    /**
     * ------------------------------------------------------------------------------------
     * POST Methods:
     */

    public Boolean insertScore(Map<String, Object> json);
}
