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

import tocados.marin.RESTServer.models.user.User;
import tocados.marin.RESTServer.models.user.UserDTO;
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

    @GetMapping()
    public List<UserDTO> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    @RequestMapping("/register")
    public Boolean insertUser(@RequestBody User user) {
        return this.userService.insertUser(user);
    }

    @PostMapping
    @RequestMapping("/login")
    public String logIn(@RequestBody User user) {
        return userService.logIn(user);
    }

    @PostMapping
    @RequestMapping("/logout")
    public Boolean logout(@RequestBody Map<String, String> json) {
        return userService.logOut(json);
    }

    @PutMapping
    @RequestMapping("/update")
    public Boolean updateUser(@RequestBody Map<String, Object> json) {
        return userService.updateUser(json);
    }

    @DeleteMapping
    @RequestMapping("/delete")
    public Boolean deleteUserFromUsername(@RequestBody Map<String, String> json) {
        return this.userService.deleteUserFromUsername(json);
    }
}
