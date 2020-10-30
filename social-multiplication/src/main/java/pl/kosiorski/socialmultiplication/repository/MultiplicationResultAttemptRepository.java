package pl.kosiorski.socialmultiplication.repository;

import org.springframework.data.repository.CrudRepository;
import pl.kosiorski.socialmultiplication.domain.MultiplicationResultAttempt;

import java.util.List;

public interface MultiplicationResultAttemptRepository
    extends CrudRepository<MultiplicationResultAttempt, Long> {

  List<MultiplicationResultAttempt> findTop5ByUserAliasOrderByIdDesc(String userAlias);
}
