package pl.kosiorski.socialmultiplication.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import pl.kosiorski.socialmultiplication.domain.Multiplication;
import pl.kosiorski.socialmultiplication.domain.MultiplicationResultAttempt;
import pl.kosiorski.socialmultiplication.domain.User;
import pl.kosiorski.socialmultiplication.event.EventDispatcher;
import pl.kosiorski.socialmultiplication.event.MultiplicationSolvedEvent;
import pl.kosiorski.socialmultiplication.repository.MultiplicationRepository;
import pl.kosiorski.socialmultiplication.repository.MultiplicationResultAttemptRepository;
import pl.kosiorski.socialmultiplication.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class MultiplicationServiceImpl implements MultiplicationService {

  private final RandomGeneratorService randomGeneratorService;
  private final MultiplicationResultAttemptRepository attemptRepository;
  private final UserRepository userRepository;
  private final MultiplicationRepository multiplicationRepository;
  private final EventDispatcher eventDispatcher;

  public MultiplicationServiceImpl(
      RandomGeneratorService randomGeneratorService,
      MultiplicationResultAttemptRepository attemptRepository,
      UserRepository userRepository,
      MultiplicationRepository multiplicationRepository,
      EventDispatcher eventDispatcher) {
    this.randomGeneratorService = randomGeneratorService;
    this.attemptRepository = attemptRepository;
    this.userRepository = userRepository;
    this.multiplicationRepository = multiplicationRepository;
    this.eventDispatcher = eventDispatcher;
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
        multiplicationRepository.findByFactorAAndAndFactorB(
            attempt.getMultiplication().getFactorA(), attempt.getMultiplication().getFactorB());

    // Avoids 'hack' attempts
    Assert.isTrue(!attempt.isCorrect(), "You can't send an attempt marked as correct!");

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

    eventDispatcher.send(
        new MultiplicationSolvedEvent(
            checkedAttempt.getId(), checkedAttempt.getUser().getId(), checkedAttempt.isCorrect()));

    return isCorrect;
  }

  @Override
  public List<MultiplicationResultAttempt> getStatsForUser(String userAlias) {
    return attemptRepository.findTop5ByUserAliasOrderByIdDesc(userAlias);
  }
}
