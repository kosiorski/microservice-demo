package pl.kosiorski.socialmultiplication.repository;

import org.springframework.data.repository.CrudRepository;
import pl.kosiorski.socialmultiplication.domain.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

  Optional<User> findByAlias(final String alias);
}
