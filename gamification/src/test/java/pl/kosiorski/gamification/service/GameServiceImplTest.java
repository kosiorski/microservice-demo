package pl.kosiorski.gamification.service;

import org.junit.jupiter.api.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.kosiorski.gamification.repository.BadgeCardRepository;
import pl.kosiorski.gamification.repository.ScoreCardRepository;

public class GameServiceImplTest {

  private GameServiceImpl gameService;

  @Mock private BadgeCardRepository badgeCardRepository;
  @Mock private ScoreCardRepository scoreCardRepository;

  @Before
  void setUp() {
    MockitoAnnotations.initMocks(this);
    gameService = new GameServiceImpl(scoreCardRepository, badgeCardRepository);
  }

  @Test
  void newAttemptForUser() {}

  @Test
  void retrieveStatsForUser() {}
}
