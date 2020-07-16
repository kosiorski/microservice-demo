package pl.kosiorski.multiplication.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.kosiorski.multiplication.domain.Multiplication;
import pl.kosiorski.multiplication.domain.MultiplicationResultAttempt;
import pl.kosiorski.multiplication.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

public class MultiplicationServiceImplTest {

  @Mock private RandomGeneratorService randomGeneratorService;
  private MultiplicationServiceImpl multiplicationServiceImpl;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    this.multiplicationServiceImpl = new MultiplicationServiceImpl(randomGeneratorService);
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
        new MultiplicationResultAttempt(user, multiplication, 3000);

    // when
    boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);

    // then
    assertThat(attemptResult).isTrue();
  }

  @Test
  public void checkWrongAttemptTest() {
    // given
    Multiplication multiplication = new Multiplication(50, 60);
    User user = new User("adam kowalski");
    MultiplicationResultAttempt attempt =
        new MultiplicationResultAttempt(user, multiplication, 6666);

    // when
    boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);

    // then
    assertThat(attemptResult).isFalse();
  }
}
