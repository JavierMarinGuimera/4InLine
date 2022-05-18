package tocados.marin.RESTServer.impls;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tocados.marin.RESTServer.managers.EncrypterManager;
import tocados.marin.RESTServer.managers.TokensManager;
import tocados.marin.RESTServer.models.Token;
import tocados.marin.RESTServer.models.user.User;
import tocados.marin.RESTServer.models.user.UserDTO;
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
     * Check if user is not null, the password is the same of the database and the
     * token is the same too.
     * 
     * @param userToCheck User to check.
     * @param password    Password to check.
     * @param token       Token to check.
     * @return
     */
    private Boolean checkIfUserIsValid(User userToCheck, String password, String token) {
        if (userToCheck != null
                && userToCheck.getPassword().equals(EncrypterManager.encryptUserPassword(password))
                && userToCheck.getToken().getCurrent_token().equals(token)) {
            return true;
        }
        return false;
    }

    /**
     * ------------------------------------------------------------------------------------
     * GET Methods:
     */

    @Override
    public List<UserDTO> getUsers() {
        return UserDTO.transformUserListToDTO((List<User>) usersRepository.findAll());
    }

    @Override
    public User getUserFromUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    /**
     * ------------------------------------------------------------------------------------
     * POST Methods:
     */

    @Override
    public Boolean insertUser(User user) {
        if (getUserFromUsername(user.getUsername()) != null) {
            return false;
        }

        user.setPassword(EncrypterManager.encryptUserPassword(user.getPassword()));

        User createdUser = usersRepository.save(user);

        tokensRepository.save(
                new Token(createdUser, TokensManager.generateRandomToken(), new Timestamp(System.currentTimeMillis())));

        return true;
    }

    @Override
    public String logIn(User user) {
        // Search user from username on DDBB to know if exist.
        User userFromDDBB = getUserFromUsername(user.getUsername());

        // Check if the user exists and the user password is equals to the userFromDDBB
        // password.
        if (userFromDDBB == null || !EncrypterManager.encryptUserPassword(user.getPassword())
                .equals(userFromDDBB.getPassword())) {
            return "Incorrect credentials!";
        }

        Token token = userFromDDBB.getToken();

        // Check if this user has token that is not expired.
        if (token != null && TokensManager.isValidToken(token)) {
            // Return the current token because is still valid.
            return token.getCurrent_token();
        } else {
            // Update the current user token and the creation date and returning it.
            token.setCurrent_token(TokensManager.generateRandomToken());
            token.setCreation_date(new Timestamp(System.currentTimeMillis()));
            return usersRepository.save(userFromDDBB).getToken().getCurrent_token();
        }

    }

    @Override
    public Boolean logOut(Map<String, String> json) {
        String username = "";
        String token = "";

        try {
            username = json.get("username");
            token = json.get("token");
        } catch (Exception e) {
            return false;
        }

        // Search user from username on DDBB to know if exist.
        User userFromDDBB = getUserFromUsername(username);

        return userFromDDBB != null && userFromDDBB.getToken().getCurrent_token().equals(token)
                && TokensManager.isValidToken(userFromDDBB.getToken());
    }

    /**
     * ------------------------------------------------------------------------------------
     * PUT Methods:
     */

    @Override
    public Boolean updateUser(User user, User userUpdated, String token) {
        // Search user from username on DDBB to know if exist.
        User userFromDDBB = getUserFromUsername(user.getUsername());

        if (checkIfUserIsValid(userFromDDBB, user.getPassword(), token)) {
            userFromDDBB.setUsername(userUpdated.getUsername());
            userFromDDBB.setPassword(EncrypterManager.encryptUserPassword(userUpdated.getPassword()));
            usersRepository.save(userFromDDBB);

            return true;
        }

        return false;
    }

    /**
     * ------------------------------------------------------------------------------------
     * DELETE Methods:
     */

    @Override
    public Boolean deleteUserFromUsername(Map<String, String> json) {
        String username = json.get("username");
        String password = json.get("password");
        String token = json.get("token");

        if (username == null || password == null || token == null) {
            return false;
        }

        // Search user from username on DDBB to know if exist.
        User userFromDDBB = getUserFromUsername(username);

        if (checkIfUserIsValid(userFromDDBB, password, token)) {

            usersRepository.delete(userFromDDBB);
            return true;
        }

        return false;
    }
}
