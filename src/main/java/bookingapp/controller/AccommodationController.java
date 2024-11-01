package bookingapp.controller;

import bookingapp.dto.accommodation.AccommodationDto;
import bookingapp.dto.accommodation.AccommodationRequestDto;
import bookingapp.service.AccommodationService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/accommodations")
public class AccommodationController {
    private final AccommodationService accommodationService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccommodationDto create(
            @RequestBody @Valid AccommodationRequestDto requestDto) {
        return accommodationService.create(requestDto);
    }

    @GetMapping
    public List<AccommodationDto> getAll(
            @PageableDefault() Pageable pageable) {
        return accommodationService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public AccommodationDto getById(@PathVariable Long id) {
        return accommodationService.findById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public AccommodationDto update(
            @PathVariable Long id,
            @RequestBody @Valid AccommodationRequestDto requestDto) {
        return accommodationService.update(id, requestDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        accommodationService.deleteById(id);
    }
}
