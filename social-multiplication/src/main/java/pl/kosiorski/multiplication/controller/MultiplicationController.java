package pl.kosiorski.multiplication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kosiorski.multiplication.domain.Multiplication;
import pl.kosiorski.multiplication.service.MultiplicationService;

@RestController
@RequestMapping("/multiplications")
final class MultiplicationController {

  private final MultiplicationService multiplicationService;

  MultiplicationController(MultiplicationService multiplicationService) {
    this.multiplicationService = multiplicationService;
  }

  @GetMapping("/random")
  Multiplication getRandomMultiplication() {
    return multiplicationService.createRandomMultiplication();
  }
}
