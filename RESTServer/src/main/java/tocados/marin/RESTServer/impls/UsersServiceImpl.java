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
    public Map<String, Object> getUsers() {
        Map<String, Object> usersMap = new HashMap<>();
        usersMap.put("users", UserDTO.transformUserListToDTO((List<User>) usersRepository.findAll()));
        return usersMap;
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
    public Map<String, Object> insertUser(User user) {
        Map<String, Object> responseMap = new HashMap<>();
        if (getUserFromUsername(user.getUsername()) != null) {
            responseMap.put("created", false);
            responseMap.put("error", "user exists");
        } else {
            user.setPassword(user.getPassword());

            User createdUser = usersRepository.save(user);

            tokensRepository.save(
                    new Token(createdUser, TokensManager.generateRandomToken(),
                            new Timestamp(System.currentTimeMillis())));

            responseMap.put("created", true);
        }

        return responseMap;
    }

    @Override
    public Map<String, Object> logIn(User user) {
        // Search user from username on DDBB to know if exist.
        User userFromDDBB = getUserFromUsername(user.getUsername());

        Map<String, Object> responseMap = new HashMap<>();

        // Check if the user exists and the user password is equals to the userFromDDBB
        // password.
        if (userFromDDBB == null) {
            responseMap.put("error", "User not found.");
        } else if (!user.getPassword()
                .equals(userFromDDBB.getPassword())) {
            responseMap.put("error", "Incorrect password.");

        } else {
            Token token = userFromDDBB.getToken();

            // Check if this user has token that is not expired.
            if (token != null && TokensManager.isValidToken(token)) {
                // Return the current token because is still valid.
                responseMap.put("token", token.getCurrent_token());
            } else {
                // Update the current user token and the creation date and returning it.
                token.setCurrent_token(TokensManager.generateRandomToken());
                token.setCreation_date(new Timestamp(System.currentTimeMillis()));
                responseMap.put("token", usersRepository.save(userFromDDBB).getToken().getCurrent_token());
            }

            responseMap.put("expiration_time", token.getCreation_date().getTime());
        }

        return responseMap;
    }

    /**
     * ------------------------------------------------------------------------------------
     * PUT Methods:
     */

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> updateUser(Map<String, Object> json) {
        Map<String, Object> responseMap = new HashMap<>();
        if (!json.containsKey("user")
                || !json.containsKey("userUpdated")
                || !json.containsKey("token")) {
            responseMap.put("updated", false);
            responseMap.put("error", "previus user structure incorrect");
        }

        HashMap<String, Object> user = (HashMap<String, Object>) json.get("user");
        HashMap<String, Object> userUpdated = (HashMap<String, Object>) json.get("userUpdated");
        String token = (String) json.get("token");

        if (!user.containsKey("username")
                || !user.containsKey("password")
                || !userUpdated.containsKey("username")
                || !userUpdated.containsKey("password")) {
            responseMap.put("updated", false);
            responseMap.put("error", "new user structure incorrect");
        }
        // Search user from username on DDBB to know if exist.
        User userFromDDBB = getUserFromUsername((String) user.get("username"));

        if (checkIfUserIsValid(userFromDDBB, (String) user.get("password"), token)) {
            userFromDDBB.setUsername((String) userUpdated.get("username"));
            userFromDDBB.setPassword(((String) userUpdated.get("password")));
            usersRepository.save(userFromDDBB);

            responseMap.put("updated", true);
        }

        if (!responseMap.containsKey("updated")) {
            responseMap.put("updated", false);
            responseMap.put("error", "user not updated");
        }

        return responseMap;
    }

    /**
     * ------------------------------------------------------------------------------------
     * DELETE Methods:
     */

    @Override
    public Map<String, Object> deleteUserFromUsername(Map<String, String> json) {
        Map<String, Object> responseMap = new HashMap<>();

        String username = json.get("username");
        String password = json.get("password");
        String token = json.get("token");

        if (username == null || password == null || token == null) {
            responseMap.put("deleted", false);
            responseMap.put("error", "structure incorrect");
        }

        // Search user from username on DDBB to know if exist.
        User userFromDDBB = getUserFromUsername(username);

        if (checkIfUserIsValid(userFromDDBB, password, token)) {

            usersRepository.delete(userFromDDBB);
            responseMap.put("deleted", true);
        }

        if (!responseMap.containsKey("deleted")) {
            responseMap.put("deleted", false);
            responseMap.put("error", "user invalid");
        }

        return responseMap;
    }
}
