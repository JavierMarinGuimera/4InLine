package tocados.marin.RESTServer.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tocados.marin.RESTServer.repositories.TokensRepository;
import tocados.marin.RESTServer.repositories.UsersRepository;

/**
 * Test controller.
 * Used to test some things.
 */
@RestController
@RequestMapping("/tests")
public class TestsController {
    @Autowired
    UsersRepository usersRepository;

    @Autowired
    TokensRepository tokensRepository;

    @GetMapping()
    public void tests(@RequestBody Map<String, Object> json) {

    }
}
