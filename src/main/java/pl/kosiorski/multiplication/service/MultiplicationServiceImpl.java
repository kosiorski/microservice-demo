package pl.kosiorski.multiplication.service;

import org.springframework.stereotype.Service;
import pl.kosiorski.multiplication.domain.Multiplication;
import pl.kosiorski.multiplication.domain.MultiplicationResultAttempt;

@Service
public class MultiplicationServiceImpl implements MultiplicationService {

  private final RandomGeneratorService randomGeneratorService;

  public MultiplicationServiceImpl(RandomGeneratorService randomGeneratorService) {
    this.randomGeneratorService = randomGeneratorService;
  }

  @Override
  public Multiplication createRandomMultiplication() {
    int factorA = randomGeneratorService.generateRandomFactor();
    int factorB = randomGeneratorService.generateRandomFactor();

    return new Multiplication(factorA, factorB);
  }

  @Override
  public boolean checkAttempt(MultiplicationResultAttempt resultAttempt) {
    return resultAttempt.getResultAttempt()
        == resultAttempt.getMultiplication().getFactorA() * resultAttempt.getMultiplication().getFactorB();
  }
}
