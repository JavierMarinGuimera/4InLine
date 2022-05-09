package tocados.marin.RESTServer.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tocados.marin.RESTServer.models.User;
import tocados.marin.RESTServer.services.UsersService;

@RestController
@RequestMapping("/user")
public class UsersController {
    @Autowired
    UsersService userService;

    // Get all users to show the scores.
    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    // Get user from id.
    @GetMapping(path = "/{id}")
    public User getUser(@PathVariable("id") Integer id) {
        return userService.getUserFromId(id);
    }

    // Post user to create it on the DDBB.
    @PostMapping
    public User insertUser(@RequestBody User user) {
        return this.userService.insertUser(user);
    }

    // TODO - Post login..
    @PostMapping
    @RequestMapping("/login")
    public User logIn(@RequestBody User user) {
        return userService.logIn(user);
    }

    // TODO - Put user to update username, password, scores
    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    // TODO - Delete user from DDBB.
    @DeleteMapping(path = "/{id}")
    public User deleteUserFromId(@RequestBody Integer id) {
        return this.userService.deleteUserFromId(id);
    }

    // TODO - Delete user from DDBB.
    @DeleteMapping(path = "/{username}")
    public User deleteUserFromUsername(@RequestBody String username) {
        return this.userService.deleteUserFromUsername(username);
    }
}
