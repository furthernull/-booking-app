package bookingapp.config;

import bookingapp.telegram.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class TelegramBotConfig {
    @Bean
    public String botUsername(@Value("${telegram.bot.username}") String botUsername) {
        return botUsername;
    }

    @Bean
    public String botToken(@Value("${telegram.bot.token}") String botToken) {
        return botToken;
    }

    @Bean
    public TelegramBotsApi getTelegramBotsApi(TelegramBot bookingBot) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(bookingBot);
        return botsApi;
    }
}
