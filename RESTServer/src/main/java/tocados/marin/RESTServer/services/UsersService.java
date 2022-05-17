package tocados.marin.RESTServer.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tocados.marin.RESTServer.managers.EncrypterManager;
import tocados.marin.RESTServer.models.User;
import tocados.marin.RESTServer.repositories.UsersRepository;

@Service
public class UsersService {
    @Autowired
    UsersRepository usersRepository;

    // ------------------------------------------------------------------------------------

    /**
     * GET Methods:
     */

    public List<User> getUsers() {
        return (List<User>) usersRepository.findAll();
    }

    public User getUserFromId(Integer id) {
        return usersRepository.findById(id).get();
    }

    private User getUserFromUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    // ------------------------------------------------------------------------------------

    /**
     * POST Methods:
     */

    public User insertUser(User user) {
        if (getUserFromUsername(user.getUsername()) != null) {
            return null;
        }

        user.setPassword(EncrypterManager.encryptUserPassword(user.getPassword()));
        return usersRepository.save(user);
    }

    public User logIn(User user) {
        User userFromDDBB = getUserFromUsername(user.getUsername());

        if (userFromDDBB == null) {
            System.out.println("Null");
            return null;
        }

        if (user.getUsername().equals(userFromDDBB.getUsername())
                && EncrypterManager.encryptUserPassword(user.getPassword())
                        .equals(userFromDDBB.getPassword())) {
            return userFromDDBB;
        } else {
            return null;
        }
    }

    // ------------------------------------------------------------------------------------

    /**
     * PUT Methods:
     */

    public User updateUser(User user) {
        return null;
    }

    // ------------------------------------------------------------------------------------

    /**
     * DELETE Methods:
     */

    public User deleteUserFromUsername(String username) {
        return null;
    }

    public User deleteUserFromId(Integer id) {
        return null;
    }

    // ------------------------------------------------------------------------------------

}
