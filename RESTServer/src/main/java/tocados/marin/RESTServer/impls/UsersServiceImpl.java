package tocados.marin.RESTServer.impls;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public static Boolean checkIfUserIsValid(User userToCheck, String password, String token) {
        if (userToCheck != null
                && userToCheck.getPassword().equals(password)
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

        user.setPassword(user.getPassword());

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
        if (userFromDDBB == null || !user.getPassword()
                .equals(userFromDDBB.getPassword())) {
            return "false";
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
    @SuppressWarnings("unchecked")
    public Boolean updateUser(Map<String, Object> json) {
        if (!json.containsKey("user")
                || !json.containsKey("userUpdated")
                || !json.containsKey("token"))
            return false;

        HashMap<String, String> user = (HashMap<String, String>) json.get("user");
        HashMap<String, String> userUpdated = (HashMap<String, String>) json.get("userUpdated");
        String token = (String) json.get("token");

        if (!user.containsKey("username")
                || !user.containsKey("password")
                || !userUpdated.containsKey("username")
                || !userUpdated.containsKey("password"))
            return false;

        // Search user from username on DDBB to know if exist.
        User userFromDDBB = getUserFromUsername(user.get("username"));

        if (checkIfUserIsValid(userFromDDBB, user.get("password"), token)) {
            userFromDDBB.setUsername(userUpdated.get("username"));
            userFromDDBB.setPassword(userUpdated.get("password"));
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
