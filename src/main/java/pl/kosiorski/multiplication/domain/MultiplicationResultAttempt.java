package pl.kosiorski.multiplication.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode
public class MultiplicationResultAttempt {

  private final User user;
  private final Multiplication multiplication;
  private final int resultAttempt;
}
