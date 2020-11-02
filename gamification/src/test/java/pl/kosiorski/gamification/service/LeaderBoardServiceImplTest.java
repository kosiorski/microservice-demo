package pl.kosiorski.gamification.service;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import pl.kosiorski.gamification.repository.ScoreCardRepository;

import static org.junit.jupiter.api.Assertions.*;

class LeaderBoardServiceImplTest {

  private LeaderBoardServiceImpl leaderBoardService;

  @Mock private ScoreCardRepository scoreCardRepository;

  @BeforeEach
  void setUp() {}
}
