package tocados.marin.RESTServer.services;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface ScoresService {

    /**
     * ------------------------------------------------------------------------------------
     * GET Methods:
     */

    public Map<String, Object> getTopScores();

    public Map<String, Object> getUserTopScores(Map<String, Object> json);

    /**
     * ------------------------------------------------------------------------------------
     * POST Methods:
     */

    public Map<String, Object> insertScore(Map<String, Object> json);
}
