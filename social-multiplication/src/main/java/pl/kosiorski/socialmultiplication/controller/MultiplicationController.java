package pl.kosiorski.socialmultiplication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kosiorski.socialmultiplication.domain.Multiplication;
import pl.kosiorski.socialmultiplication.service.MultiplicationService;

@RestController
@RequestMapping("/multiplications")
final class MultiplicationController {

  private final MultiplicationService multiplicationService;

  public MultiplicationController(MultiplicationService multiplicationService) {
    this.multiplicationService = multiplicationService;
  }

  @GetMapping("/random")
  public Multiplication getRandomMultiplication() {
    return multiplicationService.createRandomMultiplication();
  }
}
