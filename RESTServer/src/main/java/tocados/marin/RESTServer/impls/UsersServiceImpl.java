package tocados.marin.RESTServer.impls;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tocados.marin.RESTServer.managers.EncrypterManager;
import tocados.marin.RESTServer.managers.TokensManager;
import tocados.marin.RESTServer.models.Token;
import tocados.marin.RESTServer.models.User;
import tocados.marin.RESTServer.repositories.TokensRepository;
import tocados.marin.RESTServer.repositories.UsersRepository;
import tocados.marin.RESTServer.services.UsersService;

@Service
public class UsersServiceImpl implements UsersService {
    @Autowired
    UsersRepository usersRepository;

    @Autowired
    TokensRepository tokensRepository;

    /**
     * ------------------------------------------------------------------------------------
     * GET Methods:
     */

    public List<User> getUsers() {
        return (List<User>) usersRepository.findAll();
    }

    public User getUserFromUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    /**
     * ------------------------------------------------------------------------------------
     * POST Methods:
     */

    public User insertUser(User user) {
        if (getUserFromUsername(user.getUsername()) != null) {
            return null;
        }

        user.setPassword(EncrypterManager.encryptUserPassword(user.getPassword()));

        User createdUser = usersRepository.save(user);

        tokensRepository.save(
                new Token(createdUser, TokensManager.generateRandomToken(), new Timestamp(System.currentTimeMillis())));

        return createdUser;
    }

    public String logIn(User user) {
        // Search user from username on DDBB to know if exist.
        User userFromDDBB = getUserFromUsername(user.getUsername());

        if (userFromDDBB == null) {
            System.out.println("Null");
            return null;
        }

        // Check if the user password is equals to the userFromDDBB password.
        if (EncrypterManager.encryptUserPassword(user.getPassword())
                .equals(userFromDDBB.getPassword())) {

            // Check if this user has token that is not expired.
            Token userToken = tokensRepository.findByUserId(userFromDDBB.getId());

            if (userToken != null && userToken.getCreation_date().getTime() < TokensManager.TOKEN_EXPIRATION_TIME) {
                return userToken.getCurrent_token();
            } else {
                return "";
            }

        } else {
            return null;
        }
    }

    /**
     * ------------------------------------------------------------------------------------
     * PUT Methods:
     */

    public User updateUser(User user) {
        return null;
    }

    /**
     * ------------------------------------------------------------------------------------
     * DELETE Methods:
     */

    public User deleteUserFromUsername(String username) {
        return null;
    }

    public User deleteUserFromId(Integer id) {
        return null;
    }
}
