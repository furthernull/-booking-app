package bookingapp.controller;

import bookingapp.dto.booking.BookingRequestDto;
import bookingapp.dto.booking.BookingResponseDto;
import bookingapp.dto.booking.BookingUpdateRequestDto;
import bookingapp.model.user.User;
import bookingapp.service.BookingService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDto create(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid BookingRequestDto requestDto) {
        return bookingService.createBooking(user, requestDto);
    }

    @GetMapping
    public List<BookingResponseDto> getBookings(
            @PageableDefault Pageable pageable) {
        return bookingService.getAll(pageable);
    }

    @GetMapping("/my")
    public List<BookingResponseDto> getBookingsByUserId(
            @AuthenticationPrincipal User user,
            @PageableDefault Pageable pageable) {
        return bookingService.getBookingsByUserId(user.getId(), pageable);
    }

    @GetMapping("/{id}")
    public BookingResponseDto getBookingById(
            @PathVariable Long id) {
        return bookingService.getBookingById(id);
    }

    @PatchMapping("/{id}")
    public BookingResponseDto updateBookingById(
            @PathVariable Long id,
            @RequestBody @Valid BookingUpdateRequestDto requestDto,
            @AuthenticationPrincipal User user) {
        return bookingService.updateBooking(id, user.getId(), requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelBookingById(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        bookingService.cancelBooking(id, user.getId());
    }
}
