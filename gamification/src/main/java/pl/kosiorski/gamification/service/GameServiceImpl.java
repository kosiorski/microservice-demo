package pl.kosiorski.gamification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kosiorski.gamification.domain.Badge;
import pl.kosiorski.gamification.domain.BadgeCard;
import pl.kosiorski.gamification.domain.GameStats;
import pl.kosiorski.gamification.domain.ScoreCard;
import pl.kosiorski.gamification.repository.BadgeCardRepository;
import pl.kosiorski.gamification.repository.ScoreCardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GameServiceImpl implements GameService {

  private final ScoreCardRepository scoreCardRepository;
  private final BadgeCardRepository badgeCardRepository;

  public GameServiceImpl(
      ScoreCardRepository scoreCardRepository, BadgeCardRepository badgeCardRepository) {
    this.scoreCardRepository = scoreCardRepository;
    this.badgeCardRepository = badgeCardRepository;
  }

  @Override
  public GameStats newAttemptForUser(
      final Long userId, final Long attemptId, final boolean correct) {

    // give points only if it'scorrect
    if (correct) {
      ScoreCard scoreCard = new ScoreCard(userId, attemptId);
      scoreCardRepository.save(scoreCard);
      log.info(
          "User with id {} scored {} points for attempt id {}",
          userId,
          scoreCard.getScore(),
          attemptId);
      List<BadgeCard> badgeCards = processForBadges(userId, attemptId);
      return new GameStats(
          userId,
          scoreCard.getScore(),
          badgeCards.stream().map(BadgeCard::getBadge).collect(Collectors.toList()));
    }
    return GameStats.emptyStats(userId);
  }

  /**
   * Checks the total score and the different score cards obtained to give new badges in case their
   * conditions are met.
   */
  private List<BadgeCard> processForBadges(final Long userId, final Long attemptId) {
    List<BadgeCard> badgeCards = new ArrayList<>();
    int totalScore = scoreCardRepository.getTotalScoreForUser(userId);
    log.info("New score for user {} is {}", userId, totalScore);
    List<ScoreCard> scoreCardList =
        scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId);
    List<BadgeCard> badgeCardList =
        badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId);
    // Badges depending on score
    checkAndGiveBadgeBasedOnScore(
            badgeCardList, Badge.BRONZE_MULTIPLICATOR, totalScore, 100, userId)
        .ifPresent(badgeCards::add);
    checkAndGiveBadgeBasedOnScore(
            badgeCardList, Badge.SILVER_MULTIPLICATOR, totalScore, 500, userId)
        .ifPresent(badgeCards::add);
    checkAndGiveBadgeBasedOnScore(badgeCardList, Badge.GOLD_MULTIPLICATOR, totalScore, 999, userId)
        .ifPresent(badgeCards::add);
    // First won badge
    if (scoreCardList.size() == 1 && !containsBadge(badgeCardList, Badge.FIRST_WON)) {
      BadgeCard firstWonBadge = giveBadgeToUser(Badge.FIRST_WON, userId);
      badgeCards.add(firstWonBadge);
    }
    return badgeCards;
  }

  @Override
  public GameStats retrieveStatsForUser(final Long userId) {
    int score = scoreCardRepository.getTotalScoreForUser(userId);
    List<BadgeCard> badgeCards = badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId);
    return new GameStats(
        userId, score, badgeCards.stream().map(BadgeCard::getBadge).collect(Collectors.toList()));
  }

  /**
   * Convenience method to check the current score against the different thresholds to gain badges.
   * It also assigns badge to user if the conditions are met.
   */
  private Optional<BadgeCard> checkAndGiveBadgeBasedOnScore(
      final List<BadgeCard> badgeCards,
      final Badge badge,
      final int score,
      final int scoreThreshold,
      final Long userId) {
    if (score >= scoreThreshold && !containsBadge(badgeCards, badge)) {
      return Optional.of(giveBadgeToUser(badge, userId));
    }
    return Optional.empty();
  }
  /** Checks if the passed list of badges includes the one being checked */
  private boolean containsBadge(final List<BadgeCard> badgeCards, final Badge badge) {
    return badgeCards.stream().anyMatch(b -> b.getBadge().equals(badge));
  }
  /** Assigns a new badge to the given user */
  private BadgeCard giveBadgeToUser(final Badge badge, final Long userId) {
    BadgeCard badgeCard = new BadgeCard(userId, badge);
    badgeCardRepository.save(badgeCard);
    log.info("User with id {} won a new badge: {}", userId, badge);
    return badgeCard;
  }
}
