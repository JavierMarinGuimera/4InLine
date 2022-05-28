package tocados.marin.RESTServer.impls;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<ScoreDTO> getTopScores() {
        List<Score> orderedScoreList = Score.orderScoreList((List<Score>) scoresRepository.findAll());
        return ScoreDTO
                .transformScoreListToDTO(orderedScoreList);
    }

    @Override
    public List<ScoreDTO> getUserTopScores(Map<String, Object> json) {
        List<Score> orderedScoreList = Score.orderScoreList(
                (List<Score>) scoresRepository
                        .findScoresByUser(usersRepository.findByUsername((String) json.get("username"))));
        return ScoreDTO.transformScoreListToDTO(orderedScoreList);
    }

    /**
     * ------------------------------------------------------------------------------------
     * POST Methods:
     */

    @Override
    @SuppressWarnings("unchecked")
    public Boolean insertScore(Map<String, Object> json) {
        if (!json.containsKey("server") || !json.containsKey("user")) {
            return false;
        }

        Map<String, Object> server;
        Map<String, Object> user;

        try {
            server = (Map<String, Object>) json.get("server");
            user = (Map<String, Object>) json.get("user");
        } catch (ClassCastException e) {
            return false;
        }

        if (!server.containsKey("username") || !server.containsKey("password") || !server.containsKey("token")) {
            return false;
        }
        if (!user.containsKey("username") || !user.containsKey("score")) {
            return false;
        }

        User serverUser = usersRepository.findByUsername((String) server.get("username"));
        User userFromDDBB = usersRepository.findByUsername((String) user.get("username"));
        Integer scoreInteger = (Integer) user.get("score");

        if (UsersServiceImpl.checkIfUserIsValid(serverUser,
                (String) server.get("password"), (String) server.get("token")) && userFromDDBB != null) {

            // If the user has the max saved scores and the last score is less than the new
            // score, we remove the last item.
            List<Score> orderedScores = Score.orderScoreList(userFromDDBB.getScores());
            if (orderedScores.size() == Score.MAX_SIZE
                    && orderedScores.get(orderedScores.size() - 1).getScore() < scoreInteger) {
                Score scoreToDelete = orderedScores.get(orderedScores.size() - 1);
                userFromDDBB.getScores().remove(scoreToDelete);
                scoresRepository.delete(scoreToDelete);
            } else if (orderedScores.size() == Score.MAX_SIZE) {
                return false;
            }

            Score score = new Score();
            score.setUser(userFromDDBB);
            score.setScore(scoreInteger);
            scoresRepository.save(score);

            return true;
        }

        return false;
    }

}
