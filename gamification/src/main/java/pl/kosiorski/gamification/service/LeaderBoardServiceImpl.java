package pl.kosiorski.gamification.service;

import pl.kosiorski.gamification.domain.LeaderBoardRow;
import pl.kosiorski.gamification.repository.ScoreCardRepository;

import java.util.List;

public class LeaderBoardServiceImpl implements LeaderBoardService {

  private final ScoreCardRepository scoreCardRepository;

  public LeaderBoardServiceImpl(ScoreCardRepository scoreCardRepository) {
    this.scoreCardRepository = scoreCardRepository;
  }

  @Override
  public List<LeaderBoardRow> getCurrentLeaderBoard() {
    return null;
  }
}
