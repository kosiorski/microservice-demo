package pl.kosiorski.gamification.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.kosiorski.gamification.domain.LeaderBoardRow;
import pl.kosiorski.gamification.repository.ScoreCardRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class LeaderBoardServiceImplTest {

  private LeaderBoardServiceImpl leaderBoardService;
  @Mock private ScoreCardRepository scoreCardRepository;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    leaderBoardService = new LeaderBoardServiceImpl(scoreCardRepository);
  }

  @Test
  public void retrieveLeaderBoardTest() {
    // given
    LeaderBoardRow leaderBoardRow1 = new LeaderBoardRow(1L, 30L);
    LeaderBoardRow leaderBoardRow2 = new LeaderBoardRow(2L, 60L);
    LeaderBoardRow leaderBoardRow3 = new LeaderBoardRow(5L, 10L);
    List<LeaderBoardRow> expectedLeaderBoard =
        Arrays.asList(leaderBoardRow1, leaderBoardRow2, leaderBoardRow3);
    given(scoreCardRepository.findFirst10()).willReturn(expectedLeaderBoard);

    // when
    List<LeaderBoardRow> leaderBoard = leaderBoardService.getCurrentLeaderBoard();

    // then
    assertThat(leaderBoard).isEqualTo(expectedLeaderBoard);
  }
}
