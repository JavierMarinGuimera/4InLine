package tocados.marin.RESTServer.models.score;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import tocados.marin.RESTServer.models.user.User;

@Entity
@Table(name = "scores")
public class Score implements Comparable<Score> {
    public static final Integer MAX_SIZE = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private Integer score;

    @Column(nullable = true)
    private Boolean is_winner;

    /**
     * Getters and Setters:
     */

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
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
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

    /**
     * @return the isWinner
     */
    public Boolean getIsWinner() {
        return is_winner;
    }

    /**
     * @param isWinner the isWinner to set
     */
    public void setIsWinner(Boolean isWinner) {
        this.is_winner = isWinner;
    }

    public static List<Score> orderScoreList(List<Score> scores) {
        return scores.stream().sorted().limit(MAX_SIZE)
                .collect(Collectors.toList());
    }

    @Override
    public int compareTo(Score score) {
        if (this.getScore() > score.getScore()) {
            return -1;
        } else if (this.getScore() < score.getScore()) {
            return 1;
        }

        return 0;
    }
}
