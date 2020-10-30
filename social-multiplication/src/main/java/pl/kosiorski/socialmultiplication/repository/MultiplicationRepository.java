package pl.kosiorski.socialmultiplication.repository;

import org.springframework.data.repository.CrudRepository;
import pl.kosiorski.socialmultiplication.domain.Multiplication;

import java.util.Optional;

public interface MultiplicationRepository extends CrudRepository<Multiplication, Long> {

  Optional<Multiplication> findByFactorAAndAndFactorB(int factorA, int factorB);
}
