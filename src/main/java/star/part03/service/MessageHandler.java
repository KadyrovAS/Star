package star.part03.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import star.part02.model.Recommendation;
import star.part02.service.RecommendationRuleSet;

import java.util.List;
import java.util.UUID;

/**
 * Используется для обеспечения работы telegram бота
 */
@Service
public class MessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);
    private final TelegramBot telegramBot;
    private final RecommendationRuleSet service;

    public MessageHandler(TelegramBot telegramBot,
                          @Qualifier("servicePart02") RecommendationRuleSet service) {
        this.telegramBot = telegramBot;
        this.service = service;
    }

    /**
     * Отправляет сообщение сервиса пользователю telegram
     * @param id Идентификатор пользователя telegram, приславшего сообщение
     * @param message Текст сформированного сообщения
     */
    private void sendMessageToUser(Long id, String message){
        telegramBot.execute(new SendMessage(id, message));
    }

    /**
     * Парсит сообщение, полученное от пользователя telegram
     * @param update Сообщение из telegram
     */
    public void put(Update update) {
        Long id = update.message().chat().id();
        String text = update.message().text();
        String[]words = text.split(" ");

        if (words.length != 3){
            sendMessageToUser(id, "incorrect message format");
            return;
        }
        String firstName = words[1];
        String lastName = words[2];

        if (words[0].strip().equals("/recommend")){
            List<UUID> usersId = service.findUserIdByName(firstName, lastName);
            if (usersId.size() != 1){
                sendMessageToUser(id, String.format("The user %s %s was not found in the bank's database.",
                        firstName,
                        lastName));
                return;
            }
            UUID userId = usersId.get(0);
            List<Recommendation>recommendations = service.findRecommendationById(userId).orElse(null);
            if (recommendations == null){
                sendMessageToUser(id,
                        String.format("There are no recommendations for %s %s",
                        firstName,
                        lastName));
                return;
            }

            StringBuilder line = new StringBuilder("We have the following recommendations for you\n");
            int count = 0;
            for (Recommendation recommendation: recommendations){
                count++;
                line.append(count + ". " + recommendation.getName() + "\n");
                line.append(recommendation.getText() + "\n");
            }
            sendMessageToUser(id,
                    line.toString());
        }
    }
}
