package tocados.marin.RESTServer.models.user;

import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import tocados.marin.RESTServer.models.Token;
import tocados.marin.RESTServer.models.score.Score;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Token token;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Score> scores;

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
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the token
     */
    @JsonIgnore
    public Token getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    @JsonIgnore
    public void setToken(Token token) {
        this.token = token;
    }

    /**
     * @return the scores
     */
    @JsonIgnore
    public List<Score> getScores() {
        return scores;
    }

    /**
     * @param scores the scores to set
     */
    @JsonIgnore
    public void setScores(List<Score> scores) {
        this.scores = scores;
    }
}
