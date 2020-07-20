package pl.kosiorski.multiplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.ListAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.kosiorski.multiplication.domain.Multiplication;
import pl.kosiorski.multiplication.domain.MultiplicationResultAttempt;
import pl.kosiorski.multiplication.domain.User;
import pl.kosiorski.multiplication.service.MultiplicationService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@WebMvcTest(MultiplicationResultAttemptController.class)
public class MultiplicationResultAttemptControllerTest {

  @MockBean private MultiplicationService multiplicationService;
  @Autowired MockMvc mockMvc;

  private JacksonTester<MultiplicationResultAttempt> jsonResultAttempt;
  private JacksonTester<List<MultiplicationResultAttempt>> jsonResultAttemptList;

  @Before
  public void setup() {
    JacksonTester.initFields(this, new ObjectMapper());
  }

  @Test
  public void postResultReturnCorrect() throws Exception {
    genericParameterizedTest(true);
  }

  @Test
  public void postResultReturnNotCorrect() throws Exception {
    genericParameterizedTest(false);
  }

  @Test
  public void getUserStats() throws Exception {
    // given
    User user = new User("adam kowalski");
    Multiplication multiplication = new Multiplication(50, 70);
    MultiplicationResultAttempt attempt =
        new MultiplicationResultAttempt(user, multiplication, 3500, true);
    List<MultiplicationResultAttempt> recentAttempts = Arrays.asList(attempt, attempt);
    given(multiplicationService.getStatsForUser("adam kowalski")).willReturn(recentAttempts);

    // when
    MockHttpServletResponse response =
        mockMvc.perform(get("/results").param("alias", "adam kowalski")).andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString())
        .isEqualTo(jsonResultAttemptList.write(recentAttempts).getJson());
  }

  void genericParameterizedTest(final boolean correct) throws Exception {
    // given
    given(multiplicationService.checkAttempt(any(MultiplicationResultAttempt.class)))
        .willReturn(correct);
    User user = new User("john");
    Multiplication multiplication = new Multiplication(50, 70);
    MultiplicationResultAttempt attempt =
        new MultiplicationResultAttempt(user, multiplication, 3500, true);
    // when
    MockHttpServletResponse response =
        mockMvc
            .perform(
                post("/results")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonResultAttempt.write(attempt).getJson()))
            .andReturn()
            .getResponse();
    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString())
        .isEqualTo(
            jsonResultAttempt
                .write(
                    new MultiplicationResultAttempt(
                        attempt.getUser(),
                        attempt.getMultiplication(),
                        attempt.getResultAttempt(),
                        correct))
                .getJson());
  }
}
