package pl.kosiorski.gamification.repository;

import org.springframework.data.repository.CrudRepository;
import pl.kosiorski.gamification.domain.BadgeCard;

import java.util.List;

public interface BadgeCardRepository extends CrudRepository<BadgeCard, Long> {
  List<BadgeCard> findByUserIdOrderByBadgeTimestampDesc(final Long userId);
}
