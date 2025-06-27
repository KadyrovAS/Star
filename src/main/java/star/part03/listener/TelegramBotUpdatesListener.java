package star.part03.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.DeleteWebhook;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import star.part03.service.MessageHandler;

import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;
    private final MessageHandler messageHandler;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, MessageHandler messageHandler) {
        this.telegramBot = telegramBot;
        this.messageHandler = messageHandler;
    }

    @PostConstruct
    public void init() {
        telegramBot.execute(new DeleteWebhook());
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update: updates){
            messageHandler.put(update);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
