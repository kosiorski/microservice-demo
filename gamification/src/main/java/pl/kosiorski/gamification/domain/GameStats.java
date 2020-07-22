package pl.kosiorski.gamification.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This object contains the result of one or many iterations of the game. It may contain any
 * combination of {@link ScoreCard} objects and {@link BadgeCard} objects.
 *
 * <p>It can be used as a delta (as a single game iteration) or to represent the total amount of
 * score / badges.
 */
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public final class GameStats {

  private final Long userId;
  private final int score;
  private final List<Badge> badges;

  public GameStats() {
    this.userId = 0L;
    this.score = 0;
    this.badges = new ArrayList<>();
  }

  public static GameStats emptyStats(final Long userId) {
    return new GameStats(userId, 0, Collections.emptyList());
  }

  public List<Badge> getBadges() {
    return Collections.unmodifiableList(badges);
  }
}
