package pl.kosiorski.gamification.service;

import org.junit.jupiter.api.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.kosiorski.gamification.domain.Badge;
import pl.kosiorski.gamification.domain.BadgeCard;
import pl.kosiorski.gamification.domain.GameStats;
import pl.kosiorski.gamification.domain.ScoreCard;
import pl.kosiorski.gamification.repository.BadgeCardRepository;
import pl.kosiorski.gamification.repository.ScoreCardRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

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
  public void processFirstCorrectAttemptTest() {
    // given
    Long userId = 1L;
    Long attemptId = 7L;
    int totalScore = 10;
    ScoreCard scoreCard = new ScoreCard(userId, attemptId);
    given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(totalScore);
    // this repository will return the just-won score card
    given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId))
        .willReturn(Collections.singletonList(scoreCard));
    given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId))
        .willReturn(Collections.emptyList());

    // when
    GameStats iteration = gameService.newAttemptForUser(userId, attemptId, true);

    // then
    assertThat(iteration.getScore()).isEqualTo(scoreCard.getScore());
    assertThat(iteration.getBadges()).containsOnly(Badge.FIRST_WON);
  }

  @Test
  public void processCorrectAttemptForLuckyNumberBadgeTest() {
    // given
    Long userId = 1L;
    Long attemptId = 7L;
    int totalScore = 100;
    BadgeCard badgeCard = new BadgeCard(userId, Badge.FIRST_WON);
    given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(totalScore);
    // this repository will return the just-won score card
    given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId))
        .willReturn(createNScoreCards(10, userId));
    given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId))
        .willReturn(Collections.singletonList(badgeCard));

    // when
    GameStats iteration = gameService.newAttemptForUser(userId, attemptId, true);

    // then
    assertThat(iteration.getScore()).isEqualTo(ScoreCard.DEFAULT_SCORE);
    assertThat(iteration.getBadges()).containsOnly(Badge.BRONZE_MULTIPLICATOR);
  }

  @Test
  public void processWrongAttemptTest() {
    // given
    Long userId = 1L;
    Long attemptId = 7L;
    int totalScore = 10;
    ScoreCard scoreCard = new ScoreCard(userId, attemptId);
    given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(totalScore);
    // this repository will return the just-won score card
    given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId))
        .willReturn(Collections.singletonList(scoreCard));
    given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId))
        .willReturn(Collections.emptyList());

    // when
    GameStats iteration = gameService.newAttemptForUser(userId, attemptId, false);

    // then
    assertThat(iteration.getScore()).isEqualTo(0);
    assertThat(iteration.getBadges()).isEmpty();
  }

  @Test
  public void retrieveStatsForUserTest() {
    // given
    Long userId = 1L;
    int totalScore = 1000;
    BadgeCard badgeCard = new BadgeCard(userId, Badge.SILVER_MULTIPLICATOR);
    given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(totalScore);
    given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId))
        .willReturn(Collections.singletonList(badgeCard));

    // when
    GameStats stats = gameService.retrieveStatsForUser(userId);

    // assert - should score one card and win the badge FIRST_WON
    assertThat(stats.getScore()).isEqualTo(totalScore);
    assertThat(stats.getBadges()).containsOnly(Badge.BRONZE_MULTIPLICATOR);
  }

  private List<ScoreCard> createNScoreCards(int n, Long userId) {
    return IntStream.range(0, n)
        .mapToObj(i -> new ScoreCard(userId, (long) i))
        .collect(Collectors.toList());
  }
}
