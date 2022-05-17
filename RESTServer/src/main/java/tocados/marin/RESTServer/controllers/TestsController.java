package tocados.marin.RESTServer.controllers;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tocados.marin.RESTServer.managers.TokensManager;
import tocados.marin.RESTServer.models.Token;
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
    public String tests(@RequestParam String id) {

        // Check if this user has token that is not expired.
        Token tokenUser = tokensRepository.findByUserId(Integer.parseInt(id));

        return (tokenUser == null ? ""
                : (new Date(System.currentTimeMillis()).getTime() - tokenUser.getCreation_date().getTime()) + ", "
                        + TokensManager.TOKEN_EXPIRATION_TIME);
    }
}
