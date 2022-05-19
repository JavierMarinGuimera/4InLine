package tocados.marin.RESTServer.models.score;

import java.util.ArrayList;
import java.util.List;

public class ScoreDTO {
    String username;
    Integer score;

    /**
     * @param username
     * @param score
     */
    private ScoreDTO(String username, Integer score) {
        this.username = username;
        this.score = score;
    }

    public static ScoreDTO transformScoreToDTO(Score score) {
        return new ScoreDTO(score.getUser().getUsername(), score.getScore());
    }

    public static List<ScoreDTO> transformScoreListToDTO(List<Score> scores) {
        List<ScoreDTO> scoresDTO = new ArrayList<>();

        scores.forEach((score) -> {
            scoresDTO.add(new ScoreDTO(score.getUser().getUsername(), score.getScore()));
        });

        return scoresDTO;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the score
     */
    public Integer getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(Integer score) {
        this.score = score;
    }

}
