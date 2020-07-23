package pl.kosiorski.gamification.service;

import pl.kosiorski.gamification.domain.LeaderBoardRow;

import java.util.List;

public interface LeaderBoardService {
  /**
   * Retrieves the current leader board with the top score users
   *
   * @return the users with the highest score
   */
  List<LeaderBoardRow> getCurrentLeaderBoard();
}
