package pl.kosiorski.multiplication.repository;

import org.springframework.data.repository.CrudRepository;
import pl.kosiorski.multiplication.domain.Multiplication;

import java.util.Optional;

public interface MultiplicationRepository extends CrudRepository<Multiplication, Long> {

  Optional<Multiplication> findAllByFactorAAndFactorB(int factorA, int factorB);
}
