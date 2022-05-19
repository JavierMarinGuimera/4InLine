package tocados.marin.RESTServer.services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import tocados.marin.RESTServer.models.user.User;
import tocados.marin.RESTServer.models.user.UserDTO;

@Service
public interface UsersService {
    /**
     * ------------------------------------------------------------------------------------
     * GET Methods:
     */

    public List<UserDTO> getUsers();

    public User getUserFromUsername(String username);

    /**
     * ------------------------------------------------------------------------------------
     * POST Methods:
     */

    public Boolean insertUser(User user);

    public String logIn(User user);

    public Boolean logOut(Map<String, String> json);

    /**
     * ------------------------------------------------------------------------------------
     * PUT Methods:
     */

    public Boolean updateUser(Map<String, Object> json);

    /**
     * ------------------------------------------------------------------------------------
     * DELETE Methods:
     */

    public Boolean deleteUserFromUsername(Map<String, String> json);
}
