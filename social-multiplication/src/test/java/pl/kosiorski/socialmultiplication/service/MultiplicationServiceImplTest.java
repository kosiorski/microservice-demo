package pl.kosiorski.socialmultiplication.service;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import pl.kosiorski.socialmultiplication.domain.Multiplication;
import pl.kosiorski.socialmultiplication.domain.MultiplicationResultAttempt;
import pl.kosiorski.socialmultiplication.domain.User;
import pl.kosiorski.socialmultiplication.event.EventDispatcher;
import pl.kosiorski.socialmultiplication.event.MultiplicationSolvedEvent;
import pl.kosiorski.socialmultiplication.repository.MultiplicationRepository;
import pl.kosiorski.socialmultiplication.repository.MultiplicationResultAttemptRepository;
import pl.kosiorski.socialmultiplication.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class MultiplicationServiceImplTest {

  private MultiplicationServiceImpl multiplicationServiceImpl;

  @Mock private RandomGeneratorService randomGeneratorService;
  @Mock private MultiplicationResultAttemptRepository attemptRepository;
  @Mock private UserRepository userRepository;
  @Mock private MultiplicationRepository multiplicationRepository;
  @Mock private EventDispatcher eventDispatcher;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    multiplicationServiceImpl =
        new MultiplicationServiceImpl(
            randomGeneratorService,
            attemptRepository,
            userRepository,
            multiplicationRepository,
            eventDispatcher);
  }

  @Test
  public void createRandomMultiplicationTest() {
    // given
    given(randomGeneratorService.generateRandomFactor()).willReturn(50, 30);

    // when
    Multiplication multiplication = multiplicationServiceImpl.createRandomMultiplication();

    // assert
    assertThat(multiplication.getFactorA()).isEqualTo(50);
    assertThat(multiplication.getFactorB()).isEqualTo(30);
  }

  @Test
  public void checkCorrectAttemptTest() {
    // given
    Multiplication multiplication = new Multiplication(50, 60);

    User user = new User("john_doe");

    MultiplicationResultAttempt attempt =
        new MultiplicationResultAttempt(user, multiplication, 3000, false);

    MultiplicationResultAttempt verifiedAttempt =
        new MultiplicationResultAttempt(user, multiplication, 3000, true);

    MultiplicationSolvedEvent event =
        new MultiplicationSolvedEvent(attempt.getId(), user.getId(), true);

    given(userRepository.findByAlias("john_doe")).willReturn(Optional.empty());

    // when
    boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);

    // then
    assertThat(attemptResult).isTrue();
    verify(attemptRepository).save(verifiedAttempt);
    verify(eventDispatcher).send(event);
  }

  @Test
  public void checkWrongAttemptTest() {
    // given
    Multiplication multiplication = new Multiplication(50, 60);

    User user = new User("john_doe");

    MultiplicationResultAttempt attempt =
        new MultiplicationResultAttempt(user, multiplication, 666, false);

    MultiplicationSolvedEvent event =
        new MultiplicationSolvedEvent(attempt.getId(), user.getId(), false);

    given(userRepository.findByAlias("john_doe")).willReturn(Optional.empty());

    // when
    boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);

    // then
    assertThat(attemptResult).isFalse();
    verify(attemptRepository).save(attempt);
    verify(eventDispatcher).send(event);
  }

  @Test
  public void retrieveStatsTest() {
    // given
    Multiplication multiplication = new Multiplication(50, 60);

    User user = new User("john_doe");

    MultiplicationResultAttempt attempt1 =
        new MultiplicationResultAttempt(user, multiplication, 3010, false);

    MultiplicationResultAttempt attempt2 =
        new MultiplicationResultAttempt(user, multiplication, 3051, false);

    List<MultiplicationResultAttempt> latestAttempts = Lists.newArrayList(attempt1, attempt2);

    given(userRepository.findByAlias("john_doe")).willReturn(Optional.empty());
    given(attemptRepository.findTop5ByUserAliasOrderByIdDesc(user.getAlias()))
        .willReturn(latestAttempts);

    // when
    List<MultiplicationResultAttempt> latestAttemptsResult =
        multiplicationServiceImpl.getStatsForUser(user.getAlias());

    // then
    assertThat(latestAttemptsResult).isEqualTo(latestAttempts);
  }
}
