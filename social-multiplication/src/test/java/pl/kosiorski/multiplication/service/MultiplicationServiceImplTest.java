package pl.kosiorski.multiplication.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.kosiorski.multiplication.domain.Multiplication;
import pl.kosiorski.multiplication.domain.MultiplicationResultAttempt;
import pl.kosiorski.multiplication.domain.User;
import pl.kosiorski.multiplication.event.EventDispatcher;
import pl.kosiorski.multiplication.event.MultiplicationSolvedEvent;
import pl.kosiorski.multiplication.repository.MultiplicationRepository;
import pl.kosiorski.multiplication.repository.MultiplicationResultAttemptRepository;
import pl.kosiorski.multiplication.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
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
    this.multiplicationServiceImpl =
        new MultiplicationServiceImpl(
            randomGeneratorService,
            attemptRepository,
            userRepository,
            multiplicationRepository,
            eventDispatcher);
  }

  @Test
  public void checkCorrectAttemptTest() {
    // given
    Multiplication multiplication = new Multiplication(50, 60);

    User user = new User("adam kowalski");

    MultiplicationResultAttempt attempt =
        new MultiplicationResultAttempt(user, multiplication, 3000, false);

    MultiplicationResultAttempt verifiedAttempt =
        new MultiplicationResultAttempt(user, multiplication, 3000, true);

    MultiplicationSolvedEvent event =
        new MultiplicationSolvedEvent(attempt.getId(), attempt.getUser().getId(), true);

    given(userRepository.findByAlias("adam kowalski")).willReturn(Optional.empty());

    // when
    boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);

    // then
    assertThat(attemptResult).isTrue();
    verify(attemptRepository).save(verifiedAttempt);
    verify(eventDispatcher).send(eq(event));
  }

  @Test
  public void checkWrongAttemptTest() {
    // given
    Multiplication multiplication = new Multiplication(50, 60);

    User user = new User("adam kowalski");

    MultiplicationResultAttempt attempt =
        new MultiplicationResultAttempt(user, multiplication, 3010, false);

    MultiplicationSolvedEvent event =
        new MultiplicationSolvedEvent(attempt.getId(), attempt.getUser().getId(), false);

    given(userRepository.findByAlias("adam kowalski")).willReturn(Optional.empty());

    // when
    boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);

    // then
    assertThat(attemptResult).isFalse();
    verify(attemptRepository).save(attempt);
    verify(eventDispatcher).send(eq(event));
  }

  @Test
  public void retrieveStats() {
    // given
    Multiplication multiplication = new Multiplication(50, 60);
    User user = new User("adam kowalski");
    MultiplicationResultAttempt attempt1 =
        new MultiplicationResultAttempt(user, multiplication, 3010, false);
    MultiplicationResultAttempt attempt2 =
        new MultiplicationResultAttempt(user, multiplication, 3051, false);

    List<MultiplicationResultAttempt> latestAttempts = Arrays.asList(attempt1, attempt2);

    given(userRepository.findByAlias("adam kowalski")).willReturn(Optional.empty());
    given(attemptRepository.findTop5ByUserAliasOrderByIdDesc("adam kowalski"))
        .willReturn(latestAttempts);

    // when
    List<MultiplicationResultAttempt> latestAttemptsResult =
        multiplicationServiceImpl.getStatsForUser("adam kowalski");

    // then
    assertThat(latestAttemptsResult).isEqualTo(latestAttempts);
  }
}
