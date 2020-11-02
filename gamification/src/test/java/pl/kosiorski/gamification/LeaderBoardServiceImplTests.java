package pl.kosiorski.gamification;

import pl.kosiorski.gamification.domain.LeaderBoardRow;
import pl.kosiorski.gamification.repository.ScoreCardRepository;
import pl.kosiorski.gamification.service.LeaderBoardServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class LeaderBoardServiceImplTests {

  private LeaderBoardServiceImpl leaderBoardService;

  @Mock private ScoreCardRepository scoreCardRepository;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    leaderBoardService = new LeaderBoardServiceImpl(scoreCardRepository);
  }

  @Test
  public void getCurrentLeaderBoardTest() {
    // given
    Long firstUserId = 1L;
    Long secondUserId = 2L;
    Long thirdUserId = 3L;
    LeaderBoardRow leaderBoardRow1 = new LeaderBoardRow(firstUserId, 300L);
    LeaderBoardRow leaderBoardRow2 = new LeaderBoardRow(secondUserId, 400L);
    LeaderBoardRow leaderBoardRow3 = new LeaderBoardRow(thirdUserId, 100L);
    List<LeaderBoardRow> expectedLeaderBoard =
        Arrays.asList(leaderBoardRow1, leaderBoardRow2, leaderBoardRow3);

    given(scoreCardRepository.findFirst10()).willReturn(expectedLeaderBoard);

    // when
    List<LeaderBoardRow> leaderBoard = leaderBoardService.getCurrentLeaderBoard();

    // then
    assertThat(leaderBoard).isEqualTo(expectedLeaderBoard);
  }
}
