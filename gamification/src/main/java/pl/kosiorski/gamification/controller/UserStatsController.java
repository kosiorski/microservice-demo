package pl.kosiorski.gamification.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.kosiorski.gamification.domain.GameStats;
import pl.kosiorski.gamification.service.GameService;

@RestController
@RequestMapping("/stats")
public class UserStatsController {

  private final GameService gameService;

  public UserStatsController(GameService gameService) {
    this.gameService = gameService;
  }

  @GetMapping
  public GameStats getStatsForUser(@RequestParam("userId") final Long userId) {
    return gameService.retrieveStatsForUser(userId);
  }
}
