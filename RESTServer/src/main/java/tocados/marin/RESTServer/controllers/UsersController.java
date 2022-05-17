package tocados.marin.RESTServer.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tocados.marin.RESTServer.models.User;
import tocados.marin.RESTServer.services.TokensService;
import tocados.marin.RESTServer.services.UsersService;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    UsersService userService;

    @Autowired
    TokensService tokensService;

    // ------------------------------------------------------------------------------------

    // Get all users to show the scores.
    @GetMapping()
    public List<User> getUsers() {
        return userService.getUsers();
    }

    // Post user to create it on the DDBB.
    @PostMapping
    @RequestMapping("/register")
    public User insertUser(@RequestBody User user) {
        return this.userService.insertUser(user);
    }

    // TODO - Post login..
    @PostMapping
    @RequestMapping("/login")
    public String logIn(@RequestBody User user) {
        return userService.logIn(user);
    }

    // TODO - Post logout..
    @PostMapping
    @RequestMapping("/logout")
    public void test(@RequestBody Map<String, Object> json) {
        for (Map.Entry<String, Object> entry : json.entrySet()) {
            System.out.println(entry.getKey() + ", " + entry.getValue());
        }
    }

    // TODO - Put user to update user
    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    // TODO - Delete user from DDBB.
    @DeleteMapping(path = "/{username}")
    public User deleteUserFromUsername(@RequestBody String username) {
        return this.userService.deleteUserFromUsername(username);
    }
}
