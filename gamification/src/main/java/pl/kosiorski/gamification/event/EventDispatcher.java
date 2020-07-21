package pl.kosiorski.gamification.event;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;

/** Handles the communication with the Event Bus. */
public class EventDispatcher {

  private final RabbitTemplate rabbitTemplate;
  private final String multiplicationExchange;
  private final String multiplicationSolvedRoutingKey;

  public EventDispatcher(
      RabbitTemplate rabbitTemplate,
      @Value("${multiplication.exchange}") String multiplicationExchange,
      @Value("${multiplication.solved.key}") String multiplicationSolvedRoutingKey) {
    this.rabbitTemplate = rabbitTemplate;
    this.multiplicationExchange = multiplicationExchange;
    this.multiplicationSolvedRoutingKey = multiplicationSolvedRoutingKey;
  }

  public void send(final MultiplicationSolvedEvent multiplicationSolvedEvent) {
    rabbitTemplate.convertAndSend(
        multiplicationExchange, multiplicationSolvedRoutingKey, multiplicationSolvedEvent);
  }
}
