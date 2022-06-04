package tocados.marin.RESTServer.impls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tocados.marin.RESTServer.managers.TokensManager;
import tocados.marin.RESTServer.models.score.Score;
import tocados.marin.RESTServer.models.score.ScoreDTO;
import tocados.marin.RESTServer.models.user.User;
import tocados.marin.RESTServer.repositories.ScoresRepository;
import tocados.marin.RESTServer.repositories.UsersRepository;
import tocados.marin.RESTServer.services.ScoresService;

@Service
public class ScoresServiceImpl implements ScoresService {
    @Autowired
    ScoresRepository scoresRepository;

    @Autowired
    UsersRepository usersRepository;

    /**
     * ------------------------------------------------------------------------------------
     * GET Methods:
     */

    @Override
    public Map<String, Object> getTopScores() {
        Map<String, Object> responseMap = new HashMap<>();
        List<Score> orderedScoreList = Score.orderScoreList((List<Score>) scoresRepository.findAll());

        responseMap.put("scores", ScoreDTO.transformScoreListToDTO(orderedScoreList));
        return responseMap;
    }

    @Override
    public Map<String, Object> getUserTopScores(Map<String, Object> json) {
        Map<String, Object> responseMap = new HashMap<>();
        List<Score> orderedScoreList = Score.orderScoreList(
                (List<Score>) scoresRepository
                        .findScoresByUser(usersRepository.findByUsername((String) json.get("username"))));

        responseMap.put("scores", ScoreDTO.transformScoreListToDTO(orderedScoreList));
        return responseMap;
    }

    /**
     * ------------------------------------------------------------------------------------
     * POST Methods:
     */

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> insertScore(Map<String, Object> json) {
        Map<String, Object> responseMap = new HashMap<>();

        if (!json.containsKey("server") || !json.containsKey("player")) {
            responseMap.put("created", false);
            responseMap.put("error", "structure incorrect");
        }

        Map<String, Object> server = null;
        Map<String, Object> player = null;

        try {
            server = (Map<String, Object>) json.get("server");
            player = (Map<String, Object>) json.get("player");
        } catch (ClassCastException e) {
            responseMap.put("created", false);
            responseMap.put("error", "server or player structure incorrect");
            return responseMap;
        }

        if (server == null || player == null) {
            responseMap.put("created", false);
            responseMap.put("error", "server or player structure not found");
            return responseMap;
        }

        if (!server.containsKey("username") || !server.containsKey("token")) {
            responseMap.put("created", false);
            responseMap.put("error", "server structure incorrect");
        }
        if (!player.containsKey("username") || !player.containsKey("score") || !player.containsKey("isWinner")) {
            responseMap.put("created", false);
            responseMap.put("error", "user structure incorrect");
        }

        User serverUser = usersRepository.findByUsername((String) server.get("username"));
        User userFromDDBB = usersRepository.findByUsername((String) player.get("username"));
        Integer scoreInteger = (Integer) player.get("score");
        Boolean scoreWinner = (Boolean) player.get("isWinner");

        if (TokensManager.isValidToken(serverUser.getToken()) && userFromDDBB != null
                && userFromDDBB.getScores() != null) {

            // If the user has the max saved scores and the last score is less than the new
            // score, we remove the last item.
            System.out.println(userFromDDBB.getScores());
            List<Score> orderedScores = Score.orderScoreList(userFromDDBB.getScores());
            if (orderedScores.size() == Score.MAX_SIZE
                    && orderedScores.get(orderedScores.size() - 1).getScore() < scoreInteger) {
                Score scoreToDelete = orderedScores.get(orderedScores.size() - 1);
                userFromDDBB.getScores().remove(scoreToDelete);
                scoresRepository.delete(scoreToDelete);
            } else if (orderedScores.size() == Score.MAX_SIZE) {
                responseMap.put("created", false);
                responseMap.put("error", "next score less than last 3");
            }

            Score score = new Score();
            score.setUser(userFromDDBB);
            score.setScore(scoreInteger);
            score.setIsWinner(scoreWinner);
            scoresRepository.save(score);

            responseMap.put("created", true);
        }

        return responseMap;
    }

}
