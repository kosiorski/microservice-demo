package pl.kosiorski.gamification.service;

import org.springframework.stereotype.Service;
import pl.kosiorski.gamification.domain.GameStats;
import pl.kosiorski.gamification.repository.BadgeCardRepository;
import pl.kosiorski.gamification.repository.ScoreCardRepository;

@Service
public class GameServiceImpl implements GameService {

  private final ScoreCardRepository scoreCardRepository;
  private final BadgeCardRepository badgeCardRepository;

  public GameServiceImpl(
      ScoreCardRepository scoreCardRepository, BadgeCardRepository badgeCardRepository) {
    this.scoreCardRepository = scoreCardRepository;
    this.badgeCardRepository = badgeCardRepository;
  }

  @Override
  public GameStats newAttemptForUser(Long userId, Long attemptId, boolean correct) {
    return null;
  }

  @Override
  public GameStats retrieveStatsForUser(Long userId) {
    return null;
  }
}
