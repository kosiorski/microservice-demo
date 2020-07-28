package pl.kosiorski.gamification.service;

import pl.kosiorski.gamification.domain.GameStats;

public class GameServiceImpl implements GameService {
    @Override
    public GameStats newAttemptForUser(Long userId, Long attemptId, boolean correct) {
    return null;
    }

    @Override
    public GameStats retrieveStatsForUser(Long userId) {
        return null;
    }
}
