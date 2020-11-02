package pl.kosiorski.gamification.service;

import org.springframework.stereotype.Service;
import pl.kosiorski.gamification.domain.LeaderBoardRow;

import java.util.List;

@Service
public class LeaderBoardServiceImpl implements LeaderBoardService {
  @Override
  public List<LeaderBoardRow> getCurrentLeaderBoard() {
    return null;
  }
}
