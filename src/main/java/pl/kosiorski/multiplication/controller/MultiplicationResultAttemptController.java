package pl.kosiorski.multiplication.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kosiorski.multiplication.service.MultiplicationService;

@RestController
@RequestMapping("/results")
final class MultiplicationResultAttemptController {

  private final MultiplicationService multiplicationService;

  MultiplicationResultAttemptController(MultiplicationService multiplicationService) {
    this.multiplicationService = multiplicationService;
  }

  // TODO post

  @RequiredArgsConstructor
  @NoArgsConstructor(force = true)
  @Getter
  public static final class ResultResponse {
    private final boolean correct;
  }
}
