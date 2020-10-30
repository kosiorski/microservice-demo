package pl.kosiorski.socialmultiplication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kosiorski.socialmultiplication.domain.MultiplicationResultAttempt;
import pl.kosiorski.socialmultiplication.service.MultiplicationService;

import java.util.List;

@RestController
@RequestMapping("/results")
final class MultiplicationResultAttemptController {

  private final MultiplicationService multiplicationService;

  public MultiplicationResultAttemptController(MultiplicationService multiplicationService) {
    this.multiplicationService = multiplicationService;
  }

  @PostMapping
  ResponseEntity<MultiplicationResultAttempt> postResult(
      @RequestBody MultiplicationResultAttempt multiplicationResultAttempt) {

    boolean isCorrect = multiplicationResultAttempt.isCorrect();

    MultiplicationResultAttempt attemptCopy =
        new MultiplicationResultAttempt(
            multiplicationResultAttempt.getUser(),
            multiplicationResultAttempt.getMultiplication(),
            multiplicationResultAttempt.getResultAttempt(),
            isCorrect);

    return ResponseEntity.ok(attemptCopy);
  }

  @GetMapping
  ResponseEntity<List<MultiplicationResultAttempt>> getStatistics(
      @RequestParam("alias") String alias) {
    return ResponseEntity.ok(multiplicationService.getStatsForUser(alias));
  }
}
