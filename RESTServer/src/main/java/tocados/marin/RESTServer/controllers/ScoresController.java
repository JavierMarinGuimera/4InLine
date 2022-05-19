package tocados.marin.RESTServer.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tocados.marin.RESTServer.models.score.ScoreDTO;
import tocados.marin.RESTServer.models.user.User;
import tocados.marin.RESTServer.services.ScoresService;

@RestController
@RequestMapping("/scores")
public class ScoresController {
    @Autowired
    ScoresService scoresService;

    @GetMapping
    @RequestMapping("/top")
    public List<ScoreDTO> getTopScores() {
        return scoresService.getTopScores();
    }

    @GetMapping
    @RequestMapping("/user_top")
    public List<ScoreDTO> getUserTopScores(@RequestBody User user) {
        return scoresService.getUserTopScores(user);
    }

    @PostMapping
    @RequestMapping("/insert_score")
    public Boolean insertScore(@RequestBody Map<String, Object> json) {
        return scoresService.insertScore(json);
    }
}
