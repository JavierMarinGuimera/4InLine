package tocados.marin.RESTServer.models;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(nullable = false)
    @OneToOne()
    private Integer user_id;

    @Column(nullable = false)
    private String current_token;

    @Column(nullable = false)
    private Date expire_date;

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
     * @return the user_id
     */
    public Integer getUser_id() {
        return user_id;
    }

    /**
     * @param user_id the user_id to set
     */
    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
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
     * @return the expire_date
     */
    public Date getExpire_date() {
        return expire_date;
    }

    /**
     * @param expire_date the expire_date to set
     */
    public void setExpire_date(Date expire_date) {
        this.expire_date = expire_date;
    }
}
