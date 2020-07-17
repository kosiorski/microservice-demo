package pl.kosiorski.multiplication.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.kosiorski.multiplication.domain.Multiplication;
import pl.kosiorski.multiplication.domain.MultiplicationResultAttempt;
import pl.kosiorski.multiplication.domain.User;
import pl.kosiorski.multiplication.repository.MultiplicationRepository;
import pl.kosiorski.multiplication.repository.MultiplicationResultAttemptRepository;
import pl.kosiorski.multiplication.repository.UserRepository;

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

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    this.multiplicationServiceImpl =
        new MultiplicationServiceImpl(
            randomGeneratorService, attemptRepository, userRepository, multiplicationRepository);
  }

  //    @Test
  //    public void createRandomMultiplicationTest() {
  //      //given
  //      given(randomGeneratorService.generateRandomFactor()).willReturn(50, 30);
  //
  //      //when
  //      Multiplication multiplication = multiplicationServiceImpl.createRandomMultiplication();
  //
  //      //then
  //      assertThat(multiplication.getFactorA()).isEqualTo(50);
  //      assertThat(multiplication.getFactorB()).isEqualTo(30);
  //      assertThat(multiplication.getResult()).isEqualTo(1500);
  //    }

  @Test
  public void checkCorrectAttemptTest() {
    // given
    Multiplication multiplication = new Multiplication(50, 60);
    User user = new User("adam kowalski");

    MultiplicationResultAttempt attempt =
        new MultiplicationResultAttempt(user, multiplication, 3000, false);

    MultiplicationResultAttempt verifiedAttempt =
        new MultiplicationResultAttempt(user, multiplication, 3000, true);

    given(userRepository.findByAlias("adam kowalski")).willReturn(Optional.empty());

    // when
    boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);

    // then
    assertThat(attemptResult).isTrue();
    verify(attemptRepository).save(verifiedAttempt);
  }

  @Test
  public void checkWrongAttemptTest() {
    // given
    Multiplication multiplication = new Multiplication(50, 60);
    User user = new User("adam kowalski");
    MultiplicationResultAttempt attempt =
        new MultiplicationResultAttempt(user, multiplication, 3010, false);
    given(userRepository.findByAlias("adam kowalski")).willReturn(Optional.empty());

    // when
    boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);

    // then
    assertThat(attemptResult).isFalse();
    verify(attemptRepository).save(attempt);
  }
}
