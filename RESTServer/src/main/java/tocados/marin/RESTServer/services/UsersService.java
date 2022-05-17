package tocados.marin.RESTServer.services;

import java.util.List;

import org.springframework.stereotype.Service;

import tocados.marin.RESTServer.models.User;

@Service
public interface UsersService {
    /**
     * ------------------------------------------------------------------------------------
     * GET Methods:
     */

    public List<User> getUsers();

    public User getUserFromUsername(String username);

    /**
     * ------------------------------------------------------------------------------------
     * POST Methods:
     */

    public User insertUser(User user);

    public String logIn(User user);

    /**
     * ------------------------------------------------------------------------------------
     * PUT Methods:
     */

    public User updateUser(User user);

    /**
     * ------------------------------------------------------------------------------------
     * DELETE Methods:
     */

    public User deleteUserFromUsername(String username);

    public User deleteUserFromId(Integer id);
}
