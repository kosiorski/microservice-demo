package pl.kosiorski.multiplication.service;

import pl.kosiorski.multiplication.domain.Multiplication;
import pl.kosiorski.multiplication.domain.MultiplicationResultAttempt;

public interface MultiplicationService {

  Multiplication createRandomMultiplication();

  boolean checkAttempt(final MultiplicationResultAttempt attempt);
}
