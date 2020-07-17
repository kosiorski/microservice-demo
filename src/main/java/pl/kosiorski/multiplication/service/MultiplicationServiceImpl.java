package pl.kosiorski.multiplication.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import pl.kosiorski.multiplication.domain.Multiplication;
import pl.kosiorski.multiplication.domain.MultiplicationResultAttempt;
import pl.kosiorski.multiplication.domain.User;
import pl.kosiorski.multiplication.repository.MultiplicationRepository;
import pl.kosiorski.multiplication.repository.MultiplicationResultAttemptRepository;
import pl.kosiorski.multiplication.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class MultiplicationServiceImpl implements MultiplicationService {

  private final RandomGeneratorService randomGeneratorService;
  private final MultiplicationResultAttemptRepository attemptRepository;
  private final UserRepository userRepository;
  private final MultiplicationRepository multiplicationRepository;

  public MultiplicationServiceImpl(
      RandomGeneratorService randomGeneratorService,
      MultiplicationResultAttemptRepository attemptRepository,
      UserRepository userRepository,
      MultiplicationRepository multiplicationRepository) {
    this.randomGeneratorService = randomGeneratorService;
    this.attemptRepository = attemptRepository;
    this.userRepository = userRepository;
    this.multiplicationRepository = multiplicationRepository;
  }

  @Override
  public Multiplication createRandomMultiplication() {
    int factorA = randomGeneratorService.generateRandomFactor();
    int factorB = randomGeneratorService.generateRandomFactor();

    return new Multiplication(factorA, factorB);
  }

  @Transactional
  @Override
  public boolean checkAttempt(MultiplicationResultAttempt attempt) {

    Optional<User> user = userRepository.findByAlias(attempt.getUser().getAlias());

    Optional<Multiplication> multiplication =
        multiplicationRepository.findAllByFactorAAndFactorB(
            attempt.getMultiplication().getFactorA(), attempt.getMultiplication().getFactorB());

    Assert.isTrue(!attempt.isCorrect(), "You can't send an attempt marked as correct!!");

    boolean isCorrect =
        attempt.getResultAttempt()
            == attempt.getMultiplication().getFactorA() * attempt.getMultiplication().getFactorB();

    MultiplicationResultAttempt checkedAttempt =
        new MultiplicationResultAttempt(
            user.orElse(attempt.getUser()),
            multiplication.orElse(attempt.getMultiplication()),
            attempt.getResultAttempt(),
            isCorrect);

    attemptRepository.save(checkedAttempt);

    return isCorrect;
  }
}
