package bookingapp.mapper;

import bookingapp.config.MapperConfig;
import bookingapp.dto.payment.PaymentRequestDto;
import bookingapp.dto.payment.PaymentResponse;
import bookingapp.model.payment.Payment;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface PaymentMapper {

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "booking.id", source = "requestDto.bookingId")
    @Mapping(target = "sessionUrl", ignore = true)
    Payment toModel(PaymentRequestDto requestDto);

    @Mapping(target = "paymentStatus", source = "payment.status.status")
    @Mapping(target = "bookingId", source = "booking.id")
    @Mapping(target = "amount", source = "amountToPay")
    PaymentResponse toDto(Payment payment);

    List<PaymentResponse> toDto(Iterable<Payment> payments);
}
