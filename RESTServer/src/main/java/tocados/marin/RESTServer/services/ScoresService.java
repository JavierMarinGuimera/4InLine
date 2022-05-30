package tocados.marin.RESTServer.services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import tocados.marin.RESTServer.models.score.ScoreDTO;

@Service
public interface ScoresService {

    /**
     * ------------------------------------------------------------------------------------
     * GET Methods:
     */

    public List<ScoreDTO> getTopScores();

    public List<ScoreDTO> getUserTopScores(Map<String, Object> json);

    /**
     * ------------------------------------------------------------------------------------
     * POST Methods:
     */

    public Boolean insertScore(Map<String, Object> json);
}
