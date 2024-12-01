package bookingapp.telegram;

import bookingapp.exception.NotificationException;
import bookingapp.model.telegram.TelegramChat;
import bookingapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@RequiredArgsConstructor
@Component
public class TelegramBot extends TelegramLongPollingBot {
    public static final int ENROLL_INDENTATION = 8;
    public static final String SPLIT_REGEX = " ";
    private final UserService userService;
    private final String botToken;
    private final String botUsername;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getFrom().getId();
            String command = extractCommand(update.getMessage().getText());

            switch (command) {
                case "/start" -> startCommand(chatId,
                        update.getMessage().getFrom().getFirstName());
                case "/enroll" -> enrollCommand(chatId,
                        update.getMessage().getText().substring(ENROLL_INDENTATION));
                case "/stop" -> stopCommand(chatId, update.getMessage().getFrom().getFirstName());
                default -> sendMessage(chatId, NotificationTemplates.COMMAND_NOT_RECOGNIZED_TEXT);
            }
        }
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    public void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new NotificationException("Can't send message: " + message);
        }
    }

    private void startCommand(Long chatId, String userName) {
        sendMessage(chatId, String.format(NotificationTemplates.START_COMMAND_TEMPLATE, userName));
    }

    private void enrollCommand(Long chatId, String username) {
        TelegramChat telegramChat = userService.subscribeToChat(chatId, username);
        sendMessage(chatId, String.format(NotificationTemplates.ENROLL_COMMAND_TEMPLATE,
                telegramChat.getChatId(), telegramChat.getUser().getEmail()));
    }

    private void stopCommand(Long chatId, String userName) {
        userService.unsubscribeFromChat(chatId);
        sendMessage(chatId, String.format(NotificationTemplates.STOP_COMMAND_TEMPLATE, userName));
    }

    private String extractCommand(String text) {
        String[] commands = text.split(SPLIT_REGEX);
        return commands[0];
    }
}
