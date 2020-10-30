package pl.kosiorski.socialmultiplication.event;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** Handles the communication with the Event Bus. */
@Component
public class EventDispatcher {

  private final RabbitTemplate rabbitTemplate;

  // The exchange to use to send anything related to Multiplication
  private final String multiplicationExchange;

  // The routing key to use to send this particular event
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
