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

    // Get all users to show the scores.
    @GetMapping()
    public List<UserDTO> getUsers() {
        return userService.getUsers();
    }

    // Post user to create it on the DDBB.
    @PostMapping
    @RequestMapping("/register")
    public Boolean insertUser(@RequestBody User user) {
        return this.userService.insertUser(user);
    }

    // Post login.
    @PostMapping
    @RequestMapping("/login")
    public String logIn(@RequestBody User user) {
        return userService.logIn(user);
    }

    // Post logout.
    @PostMapping
    @RequestMapping("/logout")
    public Boolean logout(@RequestBody Map<String, String> json) {
        return userService.logOut(json);
    }

    // TODO - Put user to update user
    @PutMapping
    @RequestMapping("/update")
    public Boolean updateUser(@RequestBody Map<String, Object> json) {
        try {
            User user = (User) json.get("user");
            User userUpdated = (User) json.get("userUpdated");
            String token = (String) json.get("token");

            if (user == null || userUpdated == null || token == null) {
                return false;
            }

            return userService.updateUser(user, userUpdated, token);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete user from DDBB.
    @DeleteMapping
    @RequestMapping("/delete")
    public Boolean deleteUserFromUsername(@RequestBody Map<String, String> json) {
        return this.userService.deleteUserFromUsername(json);
    }
}
