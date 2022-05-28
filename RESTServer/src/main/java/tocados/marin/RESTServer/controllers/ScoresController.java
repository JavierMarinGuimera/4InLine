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

    /**
     * Structure:
     * {
     * "username": "Javier",
     * "password": "1234"
     * }
     */
    @GetMapping
    @RequestMapping("/user_top")
    public List<ScoreDTO> getUserTopScores(@RequestBody Map<String, Object> json) {
        return scoresService.getUserTopScores(json);
    }

    /**
     * Structure:
     * {
     * "server": {
     * "username": "Javier",
     * "password": "1234",
     * "token":
     * "tO7HoLtmbLbd8SUHNFso1Y822IeIKP5EnkEWB8EL1r5FEDn3rnkWliw8Wk7_0evbWqSSQZpqFttQagpTbQExUg=="
     * },
     * "user": {
     * "username": "Javier",
     * "score": 1
     * }
     * }
     * }
     */
    @PostMapping
    @RequestMapping("/insert_score")
    public Boolean insertScore(@RequestBody Map<String, Object> json) {
        return scoresService.insertScore(json);
    }
}
