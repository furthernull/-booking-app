package bookingapp.controller;

import bookingapp.dto.payment.PaymentRequestDto;
import bookingapp.dto.payment.PaymentResponse;
import bookingapp.exception.AccessDeniedException;
import bookingapp.model.user.Role;
import bookingapp.model.user.User;
import bookingapp.service.PaymentService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse createPayment(
            @RequestBody @Valid PaymentRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {
        return paymentService.initiatePayment(user, requestDto);
    }

    @GetMapping("/success/")
    public PaymentResponse successPayment(@RequestParam String sessionId) {
        return paymentService.handleSuccessPayment(sessionId);
    }

    @GetMapping("/cancel/")
    public PaymentResponse cancelPayment(@RequestParam String sessionId) {
        return paymentService.handleCancelPayment(sessionId);
    }

}
