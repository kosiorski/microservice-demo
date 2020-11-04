package pl.kosiorski.gamification.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pl.kosiorski.gamification.service.GameService;

/** This class receives the events and triggers the associated business logic. */
@Slf4j
@Component
public class EventHandler {

  private final GameService gameService;

  public EventHandler(GameService gameService) {
    this.gameService = gameService;
  }

  @RabbitListener(queues = "${multiplication.queue}")
  void handleMultiplicationSolved(final MultiplicationSolvedEvent event) {
    log.info("Multiplication Solved Event recieved: {}", event.getMultiplicationResultAttemptId());

    try {

      gameService.newAttemptForUser(
          event.getUserId(), event.getMultiplicationResultAttemptId(), event.isCorrect());
    } catch (Exception e) {
      log.error("Error when trying to process MultiplicationsSolvedEvent", e);

      // Avoids the event to be re-queued and  reprocessed.
      throw new AmqpRejectAndDontRequeueException(e);

      //TODO  dead letter exchange
    }
  }
}
