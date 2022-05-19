package tocados.marin.RESTServer.impls;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private static final Integer MAX_SIZE = 3;

    /**
     * ------------------------------------------------------------------------------------
     * GET Methods:
     */

    @Override
    public List<ScoreDTO> getTopScores() {
        return ScoreDTO
                .transformScoreListToDTO(((List<Score>) scoresRepository.findAll()).stream().sorted().limit(MAX_SIZE)
                        .collect(Collectors.toList()));
    }

    @Override
    public List<ScoreDTO> getUserTopScores(User user) {
        return ScoreDTO.transformScoreListToDTO(
                ((List<Score>) scoresRepository.findScoresByUser(usersRepository.findByUsername(user.getUsername())))
                        .stream().sorted().limit(MAX_SIZE)
                        .collect(Collectors.toList()));
    }

    /**
     * ------------------------------------------------------------------------------------
     * POST Methods:
     */

    @Override
    @SuppressWarnings("unchecked")
    public Boolean insertScore(Map<String, Object> json) {
        if (!json.containsKey("user") || !json.containsKey("user")) {
            return false;
        }

        Map<String, String> user = (Map<String, String>) json.get("user");

        if (!user.containsKey("username") || !user.containsKey("password") || !user.containsKey("token")) {
            return false;
        }
        Integer scoreInteger;
        try {
            scoreInteger = ((Integer) json.get("score"));
        } catch (Exception e) {
            return false;
        }

        User userFromDDBB = usersRepository.findByUsername(user.get("username"));

        if (UsersServiceImpl.checkIfUserIsValid(userFromDDBB,
                user.get("password"), user.get("token"))) {
            // If the user has the max saved scores and the last score is less than the new
            // score, we remove the last item.
            if (userFromDDBB.getScores().size() == MAX_SIZE && userFromDDBB.getScores()
                    .get(userFromDDBB.getScores().size() - 1).getScore() < scoreInteger) {
                scoresRepository.delete(userFromDDBB.getScores().get(userFromDDBB.getScores().size() - 1));
            } else if (userFromDDBB.getScores().size() == MAX_SIZE) {
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
