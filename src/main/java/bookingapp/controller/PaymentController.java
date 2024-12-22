package bookingapp.controller;

import bookingapp.dto.payment.PaymentResponse;
import bookingapp.exception.AccessDeniedException;
import bookingapp.model.user.Role;
import bookingapp.model.user.User;
import bookingapp.service.PaymentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/")
    public List<PaymentResponse> getPayments(
            @RequestParam(name = "user_id", required = false) Long userId,
            @AuthenticationPrincipal User user,
            @PageableDefault Pageable pageable
    ) {
        String userRole = user.getRoles().stream()
                .findFirst()
                .map(Role::getAuthority)
                .orElseThrow(() -> new AccessDeniedException(
                        "Can't retrieve role for user " + userId));
        if (userRole.equals("ADMIN")) {
            return paymentService.getPayments(userId, pageable);
        }
        return paymentService.getPayments(user.getId(), pageable);
    }
}
