package tocados.marin.RESTServer.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping()
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public User insertUser(@RequestBody User user) {
        return this.userService.insertUser(user);
    }
}
