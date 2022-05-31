package tocados.marin.RESTServer.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tocados.marin.RESTServer.models.user.User;
import tocados.marin.RESTServer.services.UsersService;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    UsersService userService;

    // ------------------------------------------------------------------------------------

    @GetMapping()
    public Map<String, Object> getUsers() {
        return userService.getUsers();
    }

    /**
     * Structure:
     * {
     * "username": "John",
     * "password": "1234"
     * }
     */

    @PostMapping
    @RequestMapping("/register")
    public Map<String, Object> insertUser(@RequestBody User user) {
        return this.userService.insertUser(user);
    }

    /**
     * Structure:
     * {
     * "username": "John",
     * "password": "1234"
     * }
     */
    @PostMapping
    @RequestMapping("/login")
    public Map<String, Object> logIn(@RequestBody User user) {
        return userService.logIn(user);
    }

    /**
     * Structure:
     * {
     * "user": {
     * "username": "Javier",
     * "password": "1234"
     * },
     * "userUpdated": {
     * "username": "Javier",
     * "password": "12345"
     * },
     * "token":
     * "iHyzdPvI3SuNfb-6ew9lD1oYFuwmLf7ixo3WJTHFKJ9NNz_hNV64qitRAwIC6f9GkQd-2AgrQR1jOxWPnELkBA=="
     * }
     */
    @PutMapping
    @RequestMapping("/update")
    public Map<String, Object> updateUser(@RequestBody Map<String, Object> json) {
        return userService.updateUser(json);
    }

    /**
     * Structure:
     * {
     * "username": "Javier",
     * "password": "12345",
     * "token":
     * "iHyzdPvI3SuNfb-6ew9lD1oYFuwmLf7ixo3WJTHFKJ9NNz_hNV64qitRAwIC6f9GkQd-2AgrQR1jOxWPnELkBA=="
     * }
     */
    @DeleteMapping
    @RequestMapping("/delete")
    public Map<String, Object> deleteUserFromUsername(@RequestBody Map<String, String> json) {
        return this.userService.deleteUserFromUsername(json);
    }
}
