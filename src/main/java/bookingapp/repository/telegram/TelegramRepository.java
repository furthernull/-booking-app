package bookingapp.repository.telegram;

import bookingapp.model.telegram.TelegramChat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TelegramRepository extends JpaRepository<TelegramChat, Long> {
    @EntityGraph(attributePaths = "user")
    Optional<TelegramChat> findByChatId(Long chatId);

    @EntityGraph(attributePaths = "user")
    Optional<TelegramChat> findByUserId(Long userId);

    @Query("SELECT tg FROM TelegramChat tg JOIN FETCH tg.user WHERE tg.isSubscribed = TRUE")
    List<TelegramChat> findAllByIsSubscribedIsTrue();
}
