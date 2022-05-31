package tocados.marin.RESTServer.services;

import java.util.Map;

import org.springframework.stereotype.Service;

import tocados.marin.RESTServer.models.user.User;

@Service
public interface UsersService {
    /**
     * ------------------------------------------------------------------------------------
     * GET Methods:
     */

    public Map<String, Object> getUsers();

    public User getUserFromUsername(String username);

    /**
     * ------------------------------------------------------------------------------------
     * POST Methods:
     */

    public Map<String, Object> insertUser(User user);

    public Map<String, Object> logIn(User user);

    /**
     * ------------------------------------------------------------------------------------
     * PUT Methods:
     */

    public Map<String, Object> updateUser(Map<String, Object> json);

    /**
     * ------------------------------------------------------------------------------------
     * DELETE Methods:
     */

    public Map<String, Object> deleteUserFromUsername(Map<String, String> json);
}
