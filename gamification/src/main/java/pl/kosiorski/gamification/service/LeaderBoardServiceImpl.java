package pl.kosiorski.gamification.service;

import org.springframework.stereotype.Service;
import pl.kosiorski.gamification.domain.LeaderBoardRow;
import pl.kosiorski.gamification.repository.ScoreCardRepository;

import java.util.List;

@Service
public class LeaderBoardServiceImpl implements LeaderBoardService {

  private final ScoreCardRepository scoreCardRepository;

  public LeaderBoardServiceImpl(ScoreCardRepository scoreCardRepository) {
    this.scoreCardRepository = scoreCardRepository;
  }

  @Override
  public List<LeaderBoardRow> getCurrentLeaderBoard() {
    return scoreCardRepository.findFirst10();
  }
}
