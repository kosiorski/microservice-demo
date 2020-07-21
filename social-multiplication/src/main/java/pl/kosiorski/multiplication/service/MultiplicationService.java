package pl.kosiorski.multiplication.service;

import pl.kosiorski.multiplication.domain.Multiplication;
import pl.kosiorski.multiplication.domain.MultiplicationResultAttempt;

import java.util.List;

public interface MultiplicationService {

  Multiplication createRandomMultiplication();

  boolean checkAttempt(final MultiplicationResultAttempt resultAttempt);

  List<MultiplicationResultAttempt> getStatsForUser(String userAlias);
}
