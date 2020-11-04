package pl.kosiorski.socialmultiplication.service;

import pl.kosiorski.socialmultiplication.domain.Multiplication;
import pl.kosiorski.socialmultiplication.domain.MultiplicationResultAttempt;

import java.util.List;
import java.util.Optional;

public interface MultiplicationService {
  /**
   * Creates a Multiplication object with two randomly-generated factors between 11 and 99.
   *
   * @return a Multiplication object with random factors
   */
  Multiplication createRandomMultiplication();

  /** @return true if the attempt matches the result of the multiplication, false otherwise. */
  boolean checkAttempt(final MultiplicationResultAttempt resultAttempt);

  List<MultiplicationResultAttempt> getStatsForUser(String userAlias);

  MultiplicationResultAttempt getResultById(Long resultId);
}
