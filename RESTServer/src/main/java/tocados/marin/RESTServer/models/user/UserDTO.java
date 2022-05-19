package tocados.marin.RESTServer.models.user;

import java.util.ArrayList;
import java.util.List;

/**
 * Class created to return users without passwords and tokens.
 */
public class UserDTO {
    private Integer id;
    private String username;
    private List<Integer> scores;

    /**
     * @param id
     * @param username
     * @param scores
     */
    private UserDTO(Integer id, String username, List<Integer> scores) {
        this.id = id;
        this.username = username;
        this.scores = scores;
    }

    public static UserDTO transformUserToDTO(User user) {
        List<Integer> scores = new ArrayList<>();

        user.getScores().forEach((v) -> {
            scores.add(v.getScore());
        });

        return new UserDTO(user.getId(), user.getUsername(), scores);
    }

    public static List<UserDTO> transformUserListToDTO(List<User> user) {
        List<UserDTO> usersDTO = new ArrayList<>();

        user.forEach((u) -> {
            usersDTO.add(transformUserToDTO(u));
        });

        return usersDTO;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
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
     * @return the scores
     */
    public List<Integer> getScores() {
        return scores;
    }

    /**
     * @param scores the scores to set
     */
    public void setScores(List<Integer> scores) {
        this.scores = scores;
    }
}
