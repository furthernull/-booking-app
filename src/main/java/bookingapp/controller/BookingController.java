package bookingapp.controller;

import bookingapp.dto.booking.BookingFilterParameters;
import bookingapp.dto.booking.BookingRequestDto;
import bookingapp.dto.booking.BookingResponseDto;
import bookingapp.dto.booking.BookingUpdateRequestDto;
import bookingapp.model.booking.BookingStatus;
import bookingapp.model.user.User;
import bookingapp.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Booking management", description = "Managing user's bookings")
@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new booking",
            description = "permits the creation of new accommodation bookings")
    public BookingResponseDto create(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid BookingRequestDto requestDto) {
        return bookingService.createBooking(user, requestDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    @Operation(summary = "Get booking by id or/and status",
            description = "retrieves bookings based on user id and their status")
    public List<BookingResponseDto> getBookingsByIdAndStatus(
            @RequestParam(name = "user_id", required = false) Long userId,
            @RequestParam(name = "status") BookingStatus.Status status,
            @PageableDefault Pageable pageable) {
        return bookingService.filter(new BookingFilterParameters(userId, status), pageable);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("/my")
    @Operation(summary = "Get own bookings", description = "retrieve users bookings")
    public List<BookingResponseDto> getBookingsByUserId(
            @AuthenticationPrincipal User user,
            @PageableDefault Pageable pageable) {
        return bookingService.getBookingsByUserId(user.getId(), pageable);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("/{id}")
    @Operation(summary = "Get booking by id",
            description = "provides information about a specific booking")
    public BookingResponseDto getBookingById(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        return bookingService.getBookingByIdAndUserId(id, user.getId());
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PatchMapping("/{id}")
    @Operation(summary = "Update booking by id",
            description = "allows users to update their booking details")
    public BookingResponseDto updateBookingById(
            @PathVariable Long id,
            @RequestBody @Valid BookingUpdateRequestDto requestDto,
            @AuthenticationPrincipal User user) {
        return bookingService.updateBooking(id, user.getId(), requestDto);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel booking by id",
            description = "enables the cancellation of bookings")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelBookingById(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        bookingService.cancelBooking(id, user.getId());
    }
}
