package tocados.marin.RESTServer.models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import tocados.marin.RESTServer.models.user.User;

@Entity
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private String current_token;

    @Column(nullable = false)
    private Timestamp creation_date;

    public Token() {
    }

    /**
     * @param user
     * @param current_token
     * @param creation_date
     */
    public Token(User user, String current_token, Timestamp creation_date) {
        this.user = user;
        this.current_token = current_token;
        this.creation_date = creation_date;
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
     * @return the user
     */
    @JsonIgnore
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    @JsonIgnore
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the current_token
     */
    public String getCurrent_token() {
        return current_token;
    }

    /**
     * @param current_token the current_token to set
     */
    public void setCurrent_token(String current_token) {
        this.current_token = current_token;
    }

    /**
     * @return the Creation_date
     */
    public Timestamp getCreation_date() {
        return creation_date;
    }

    /**
     * @param Creation_date the Creation_date to set
     */
    public void setCreation_date(Timestamp creation_date) {
        this.creation_date = creation_date;
    }
}
