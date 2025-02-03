package bookingapp.test;

import bookingapp.dto.accommodation.AccommodationDto;
import bookingapp.dto.accommodation.AccommodationRequestDto;
import bookingapp.dto.address.AddressRequestDto;
import bookingapp.dto.booking.BookingFilterParameters;
import bookingapp.dto.booking.BookingRequestDto;
import bookingapp.dto.booking.BookingResponseDto;
import bookingapp.dto.booking.BookingUpdateRequestDto;
import bookingapp.dto.payment.PaymentRequestDto;
import bookingapp.dto.payment.PaymentResponse;
import bookingapp.dto.user.UserLoginRequestDto;
import bookingapp.dto.user.UserRegistrationRequestDto;
import bookingapp.dto.user.UserResponseDto;
import bookingapp.dto.user.UserUpdateRequestDto;
import bookingapp.dto.user.UserUpdateRoleRequestDto;
import bookingapp.model.accommodation.Accommodation;
import bookingapp.model.accommodation.AccommodationType;
import bookingapp.model.accommodation.Address;
import bookingapp.model.accommodation.AmenityType;
import bookingapp.model.booking.Booking;
import bookingapp.model.booking.BookingStatus;
import bookingapp.model.payment.Payment;
import bookingapp.model.payment.PaymentStatus;
import bookingapp.model.telegram.TelegramChat;
import bookingapp.model.user.Role;
import bookingapp.model.user.User;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class TestUtils {
    public static final int AVAILABILITY_COUNT = 1;

    public static final BigDecimal DEFAULT_DAILY_RATE = BigDecimal.TEN;
    public static final BigDecimal DEFAULT_DAILY_RATE_2 = BigDecimal.valueOf(25.00);

    public static final Long DEFAULT_ID_ONE = 1L;
    public static final Long DEFAULT_ID_TWO = 2L;
    public static final Long DEFAULT_ID_THREE = 3L;
    public static final Long DEFAULT_ID_FOUR = 4L;
    public static final Long DEFAULT_ID_FIVE = 5L;
    public static final Long DEFAULT_ID_SIX = 6L;
    public static final Long DEFAULT_ID_SEVEN = 7L;
    public static final Long DEFAULT_ID_EIGHT = 8L;

    public static final String ADDRESS_FIELD = "Address";
    public static final String CITY_FIELD = "City";
    public static final String STATE_FIELD = "State";
    public static final String ZIP_FIELD = "12345";
    public static final String COUNTRY_FIELD = "Country";

    public static final String ADMIN_FIRST_NAME_FIELD = "Admin";
    public static final String ADMIN_LAST_NAME_FIELD = "Admin";
    public static final String ADMIN_EMAIL_FIELD = "admin@bookingapp.com";
    public static final String ADMIN_PASSWORD_FIELD = "Qwerty&0";

    public static final String CUSTOMER_FIRST_NAME_FIELD = "CustomerFirstName";
    public static final String CUSTOMER_LAST_NAME_FIELD = "CustomerLastName";
    public static final String CUSTOMER_EMAIL_FIELD = "customer@gmail.com";
    public static final String CUSTOMER_PASSWORD_FIELD = "Qwerty&0";
    public static final String CUSTOMER_PASSWORD_CONFIRMATION_FIELD = "Qwerty&0";
    public static final String CUSTOMER_ENCODED_PASSWORD =
            "$2a$10$4c3FoiAa1RINf6IMfJ.MXOPKkbapFI/EWuuLq2QCR2GHGj9FDTYke";

    public static final String ANOTHER_CUSTOMER_FIRST_NAME_FIELD = "AnotherCustomerFirstName";
    public static final String ANOTHER_CUSTOMER_LAST_NAME_FIELD = "AnotherCustomerLastName";
    public static final String ANOTHER_CUSTOMER_EMAIL_FIELD = "another.customer@email.com";
    public static final String ANOTHER_CUSTOMER_PASSWORD_FIELD = "AnotherCustomerPassword!1";
    public static final String ANOTHER_CUSTOMER_PASSWORD_CONFIRMATION_FIELD =
            "AnotherCustomerPassword!1";

    public static final String ACCOMMODATION_SIZE_STUDIO = "Studio";
    public static final String ACCOMMODATION_SIZE_ONE_BEDROOM = "1 Bedroom";
    public static final String ACCOMMODATION_SIZE_TWO_BEDROOM = "2 Bedroom";

    public static final AccommodationType.Type TYPE_APARTMENT = AccommodationType.Type.APARTMENT;
    public static final AccommodationType.Type TYPE_CONDO = AccommodationType.Type.CONDO;
    public static final AccommodationType.Type TYPE_HOUSE = AccommodationType.Type.HOUSE;

    public static final AccommodationType ACCOMMODATION_TYPE_APARTMENT =
            getAccommodationTypeApartment();
    public static final AccommodationType ACCOMMODATION_TYPE_CONDO = getAccommodationTypeCondo();
    public static final AccommodationType ACCOMMODATION_TYPE_HOUSE = getAccommodationTypeHouse();

    public static final AddressRequestDto ADDRESS_REQUEST_DTO = new AddressRequestDto(
            ADDRESS_FIELD, CITY_FIELD, STATE_FIELD, ZIP_FIELD, COUNTRY_FIELD
    );

    public static final Address ADDRESS = getAddress();

    public static final AmenityType.Type TYPE_PARKING = AmenityType.Type.PARKING;
    public static final AmenityType.Type TYPE_CAR_CHARGER = AmenityType.Type.CAR_CHARGER;
    public static final AmenityType.Type TYPE_PETS = AmenityType.Type.PETS;
    public static final AmenityType.Type TYPE_WI_FI = AmenityType.Type.WI_FI;
    public static final AmenityType.Type TYPE_POOL = AmenityType.Type.POOL;
    public static final AmenityType.Type TYPE_SPA = AmenityType.Type.SPA;
    public static final AmenityType.Type TYPE_GYM = AmenityType.Type.GYM;
    public static final AmenityType.Type TYPE_CAFE = AmenityType.Type.CAFE;

    public static final AmenityType AMENITY_TYPE_PARKING = getAmenityTypeParking();
    public static final AmenityType AMENITY_TYPE_CAR_CHARGER = getAmenityTypeCarCharger();
    public static final AmenityType AMENITY_TYPE_PETS = getAmenityTypePets();
    public static final AmenityType AMENITY_TYPE_WI_FI = getAmenityTypeWiFi();
    public static final AmenityType AMENITY_TYPE_POOL = getAmenityTypePool();
    public static final AmenityType AMENITY_TYPE_SPA = getAmenityTypeSpa();
    public static final AmenityType AMENITY_TYPE_GYM = getAmenityTypeGym();
    public static final AmenityType AMENITY_TYPE_CAFE = getAmenityTypeCafe();

    public static final Set<Long> AMENITY_IDS_SET = Set.of(
            DEFAULT_ID_ONE,
            DEFAULT_ID_TWO,
            DEFAULT_ID_THREE,
            DEFAULT_ID_FOUR,
            DEFAULT_ID_FIVE,
            DEFAULT_ID_SIX,
            DEFAULT_ID_SEVEN,
            DEFAULT_ID_EIGHT
    );

    public static final Set<AmenityType> AMENITY_TYPE_SET = Set.of(
            AMENITY_TYPE_PARKING,
            AMENITY_TYPE_CAR_CHARGER,
            AMENITY_TYPE_PETS,
            AMENITY_TYPE_WI_FI,
            AMENITY_TYPE_POOL,
            AMENITY_TYPE_SPA,
            AMENITY_TYPE_GYM,
            AMENITY_TYPE_CAFE
    );

    public static final AccommodationRequestDto ACCOMMODATION_REQUEST_DTO_STUDIO =
            new AccommodationRequestDto(
                    DEFAULT_ID_ONE,
                    ADDRESS_REQUEST_DTO,
                    ACCOMMODATION_SIZE_STUDIO,
                    AMENITY_IDS_SET,
                    DEFAULT_DAILY_RATE,
                    AVAILABILITY_COUNT
            );

    public static final AccommodationRequestDto ACCOMMODATION_UPDATE_REQUEST_DTO_HOUSE =
            new AccommodationRequestDto(
                    DEFAULT_ID_THREE,
                    ADDRESS_REQUEST_DTO,
                    ACCOMMODATION_SIZE_TWO_BEDROOM,
                    AMENITY_IDS_SET,
                    DEFAULT_DAILY_RATE_2,
                    AVAILABILITY_COUNT
            );

    public static final Accommodation ACCOMMODATION_STUDIO = getAccommodationStudio();
    public static final Accommodation ACCOMMODATION_UPDATED_TO_HOUSE =
            getAccommodationUpdatedToHouse();
    public static final Accommodation ACCOMMODATION_CONDO = getAccommodationCondo();

    public static final AccommodationDto ACCOMMODATION_DTO_STUDIO = getAccommodationDtoStudio();
    public static final AccommodationDto ACCOMMODATION_DTO_UPDATED_TO_HOUSE =
            getAccommodationDtoUpdatedToHouse();

    public static final Pageable PAGEABLE = PageRequest.of(0, 10);
    public static final List<Accommodation> ACCOMMODATION_LIST = List.of(ACCOMMODATION_STUDIO);
    public static final Page<Accommodation> ACCOMMODATION_PAGE = new PageImpl<>(ACCOMMODATION_LIST);

    public static final Role.RoleName ROLE_NAME_ADMIN = Role.RoleName.ADMIN;
    public static final Role.RoleName ROLE_NAME_CUSTOMER = Role.RoleName.CUSTOMER;

    public static final Role ROLE_ADMIN = getRoleAdmin();
    public static final Role ROLE_CUSTOMER = getRoleCustomer();

    public static final UserRegistrationRequestDto USER_REGISTRATION_REQUEST_DTO =
            new UserRegistrationRequestDto(
                    CUSTOMER_EMAIL_FIELD,
                    CUSTOMER_FIRST_NAME_FIELD,
                    CUSTOMER_LAST_NAME_FIELD,
                    CUSTOMER_PASSWORD_FIELD,
                    CUSTOMER_PASSWORD_CONFIRMATION_FIELD
            );

    public static final UserUpdateRoleRequestDto USER_UPDATE_ROLE_REQUEST_DTO =
            new UserUpdateRoleRequestDto(Role.RoleName.ADMIN);

    public static final UserUpdateRequestDto USER_UPDATE_REQUEST_DTO =
            new UserUpdateRequestDto(
                    ANOTHER_CUSTOMER_EMAIL_FIELD,
                    ANOTHER_CUSTOMER_FIRST_NAME_FIELD,
                    ANOTHER_CUSTOMER_LAST_NAME_FIELD,
                    ANOTHER_CUSTOMER_PASSWORD_FIELD,
                    ANOTHER_CUSTOMER_PASSWORD_CONFIRMATION_FIELD
            );

    public static final UserLoginRequestDto LOGIN_ADMIN_REQUEST_DTO = new UserLoginRequestDto(
            ADMIN_EMAIL_FIELD,
            ADMIN_PASSWORD_FIELD
    );

    public static final User USER_ADMIN = getUserAdmin();
    public static final User USER_CUSTOMER = getUserCustomer();
    public static final User USER_CUSTOMER_2 = getUserCustomer2();
    public static final User USER_CUSTOMER_UPDATED_ROLE = getUserCustomerUpdatedRole();

    public static final UserResponseDto USER_ADMIN_RESPONSE_DTO = new UserResponseDto(
            DEFAULT_ID_ONE,
            ADMIN_FIRST_NAME_FIELD,
            ADMIN_LAST_NAME_FIELD,
            ADMIN_EMAIL_FIELD
    );

    public static final UserResponseDto USER_RESPONSE_DTO = new UserResponseDto(
            DEFAULT_ID_TWO,
            CUSTOMER_FIRST_NAME_FIELD,
            CUSTOMER_LAST_NAME_FIELD,
            CUSTOMER_EMAIL_FIELD
    );

    public static final UserResponseDto USER_UPDATED_RESPONSE_DTO = new UserResponseDto(
            DEFAULT_ID_THREE,
            ANOTHER_CUSTOMER_FIRST_NAME_FIELD,
            ANOTHER_CUSTOMER_LAST_NAME_FIELD,
            ANOTHER_CUSTOMER_EMAIL_FIELD
    );

    public static final BookingStatus.Status STATUS_PENDING = BookingStatus.Status.PENDING;
    public static final BookingStatus.Status STATUS_CONFIRMED = BookingStatus.Status.CONFIRMED;
    public static final BookingStatus.Status STATUS_CANCELLED = BookingStatus.Status.CANCELLED;
    public static final BookingStatus.Status STATUS_EXPIRED = BookingStatus.Status.EXPIRED;

    public static final BookingStatus BOOKING_STATUS_PENDING = getBookingStatusPending();
    public static final BookingStatus BOOKING_STATUS_CONFIRMED = getBookingStatusConfirmed();
    public static final BookingStatus BOOKING_STATUS_CANCELLED = getBookingStatusCancelled();
    public static final BookingStatus BOOKING_STATUS_EXPIRED = getBookingStatusExpired();

    public static final LocalDate DEFAULT_CHECK_IN_DATE = LocalDate.now().plusDays(1);
    public static final LocalDate DEFAULT_CHECK_OUT_DATE = LocalDate.now().plusMonths(1);
    public static final LocalDate UPDATE_CHECK_IN_DATE = LocalDate.now().plusWeeks(2);
    public static final LocalDate UPDATE_CHECK_OUT_DATE = LocalDate.now().plusYears(1);
    public static final LocalDate EXPIRED_CHECK_IN_DATE = LocalDate.now();
    public static final LocalDate EXPIRED_CHECK_OUT_DATE = LocalDate.now().plusDays(1);

    public static final BookingRequestDto BOOKING_STUDIO_REQUEST_DTO = new BookingRequestDto(
            DEFAULT_ID_ONE,
            DEFAULT_CHECK_IN_DATE,
            DEFAULT_CHECK_OUT_DATE
    );

    public static final Booking BOOKING_STUDIO_PENDING = getBookingStudioPending();
    public static final Booking BOOKING_STUDIO_CONFIRMED = getBookingStudioConfirmed();

    public static final BookingResponseDto BOOKING_STUDIO_RESPONSE_DTO =
            new BookingResponseDto(
                DEFAULT_ID_ONE,
                DEFAULT_CHECK_IN_DATE,
                DEFAULT_CHECK_OUT_DATE,
                DEFAULT_ID_ONE,
                DEFAULT_ID_TWO,
                DEFAULT_ID_ONE
    );

    public static final BookingFilterParameters BOOKING_FILTER_PARAMETERS =
            new BookingFilterParameters(
                    USER_CUSTOMER.getId(),
                    BOOKING_STUDIO_PENDING.getStatus().getStatus()
            );

    public static final List<Booking> BOOKING_LIST = List.of(BOOKING_STUDIO_PENDING);
    public static final Page<Booking> BOOKING_PAGE = new PageImpl<>(BOOKING_LIST);

    public static final BookingUpdateRequestDto BOOKING_UPDATE_REQUEST_DTO =
            new BookingUpdateRequestDto(
                    UPDATE_CHECK_IN_DATE,
                    UPDATE_CHECK_OUT_DATE
            );

    public static final Booking BOOKING_STUDIO_UPDATED = getUpdatedBookingStudio();

    public static final BookingResponseDto BOOKING_STUDIO_UPDATED_RESPONSE_DTO =
            new BookingResponseDto(
                    DEFAULT_ID_ONE,
                    UPDATE_CHECK_IN_DATE,
                    UPDATE_CHECK_OUT_DATE,
                    DEFAULT_ID_ONE,
                    DEFAULT_ID_TWO,
                    DEFAULT_ID_ONE
            );

    public static final Booking CONFLICTING_BOOKING = getConflictingBooking();

    public static final Booking BOOKING_STUDIO_CANCELED = getCanceledBookingStudio();

    public static final Booking BOOKING_CONDO_EXPIRED = getExpiredBookingCondo();

    public static final PaymentStatus.Status PENDING = PaymentStatus.Status.PENDING;
    public static final PaymentStatus.Status PAID = PaymentStatus.Status.PAID;

    public static final PaymentStatus PAYMENT_STATUS_PENDING = getPaymentStatusPending();
    public static final PaymentStatus PAYMENT_STATUS_PAID = getPaymentStatusPaid();

    public static final String SESSION_URL = "https://www.example.com";
    public static final String SESSION_ID = "sessionId";
    public static final String INVALID_SESSION_ID = "InvalidSessionId";

    public static final BigDecimal PAYMENT_AMOUNT = BigDecimal.valueOf(100);

    public static final PaymentRequestDto PAYMENT_REQUEST_DTO = new PaymentRequestDto(
            DEFAULT_ID_ONE
    );

    public static final Payment PAYMENT_PENDING = getPaymentPending();
    public static final Payment PAYMENT_PAID = getPaymentPaid();

    public static final PaymentResponse PAYMENT_PENDING_RESPONSE = getPaymentPendingResponse();
    public static final PaymentResponse PAYMENT_PAID_RESPONSE = getPaymentPaidResponse();

    public static final Page<Payment> PAYMENT_PAGE = new PageImpl<>(List.of(PAYMENT_PENDING));

    public static final TelegramChat SUBSCRIBED_TELEGRAM_CHAT = getSubscribedTelegramChat();
    public static final TelegramChat UNSUBSCRIBE_TELEGRAM_CHAT = getUnsubscribedTelegramChat();

    private TestUtils() {
    }

    private static AccommodationType getAccommodationTypeApartment() {
        AccommodationType accommodationType = new AccommodationType();
        accommodationType.setId(DEFAULT_ID_ONE);
        accommodationType.setName(TYPE_APARTMENT);
        return accommodationType;
    }

    private static AccommodationType getAccommodationTypeCondo() {
        AccommodationType accommodationType = new AccommodationType();
        accommodationType.setId(DEFAULT_ID_TWO);
        accommodationType.setName(TYPE_CONDO);
        return accommodationType;
    }

    private static AccommodationType getAccommodationTypeHouse() {
        AccommodationType accommodationType = new AccommodationType();
        accommodationType.setId(DEFAULT_ID_THREE);
        accommodationType.setName(TYPE_HOUSE);
        return accommodationType;
    }

    private static Address getAddress() {
        Address address = new Address();
        address.setId(DEFAULT_ID_ONE);
        address.setAddress(ADDRESS_FIELD);
        address.setCity(CITY_FIELD);
        address.setState(STATE_FIELD);
        address.setZipCode(ZIP_FIELD);
        address.setCountry(COUNTRY_FIELD);
        return address;
    }

    private static AmenityType getAmenityTypeParking() {
        AmenityType amenityType = new AmenityType();
        amenityType.setId(DEFAULT_ID_ONE);
        amenityType.setName(TYPE_PARKING);
        return amenityType;
    }

    private static AmenityType getAmenityTypeCarCharger() {
        AmenityType amenityType = new AmenityType();
        amenityType.setId(DEFAULT_ID_TWO);
        amenityType.setName(TYPE_CAR_CHARGER);
        return amenityType;
    }

    private static AmenityType getAmenityTypePets() {
        AmenityType amenityType = new AmenityType();
        amenityType.setId(DEFAULT_ID_THREE);
        amenityType.setName(TYPE_PETS);
        return amenityType;
    }

    private static AmenityType getAmenityTypeWiFi() {
        AmenityType amenityType = new AmenityType();
        amenityType.setId(DEFAULT_ID_FOUR);
        amenityType.setName(TYPE_WI_FI);
        return amenityType;
    }

    private static AmenityType getAmenityTypePool() {
        AmenityType amenityType = new AmenityType();
        amenityType.setId(DEFAULT_ID_FIVE);
        amenityType.setName(TYPE_POOL);
        return amenityType;
    }

    private static AmenityType getAmenityTypeSpa() {
        AmenityType amenityType = new AmenityType();
        amenityType.setId(DEFAULT_ID_SIX);
        amenityType.setName(TYPE_SPA);
        return amenityType;
    }

    private static AmenityType getAmenityTypeGym() {
        AmenityType amenityType = new AmenityType();
        amenityType.setId(DEFAULT_ID_SEVEN);
        amenityType.setName(TYPE_GYM);
        return amenityType;
    }

    private static AmenityType getAmenityTypeCafe() {
        AmenityType amenityType = new AmenityType();
        amenityType.setId(DEFAULT_ID_EIGHT);
        amenityType.setName(TYPE_CAFE);
        return amenityType;
    }

    private static Accommodation getAccommodationStudio() {
        Accommodation accommodation = new Accommodation();
        accommodation.setId(DEFAULT_ID_ONE);
        accommodation.setType(ACCOMMODATION_TYPE_APARTMENT);
        accommodation.setLocation(ADDRESS);
        accommodation.setSize(ACCOMMODATION_SIZE_STUDIO);
        accommodation.setAmenities(AMENITY_TYPE_SET);
        accommodation.setDailyRate(DEFAULT_DAILY_RATE);
        accommodation.setAvailability(AVAILABILITY_COUNT);
        return accommodation;
    }

    private static Accommodation getAccommodationUpdatedToHouse() {
        Accommodation accommodation = new Accommodation();
        accommodation.setId(DEFAULT_ID_ONE);
        accommodation.setType(ACCOMMODATION_TYPE_HOUSE);
        accommodation.setLocation(ADDRESS);
        accommodation.setSize(ACCOMMODATION_SIZE_TWO_BEDROOM);
        accommodation.setAmenities(AMENITY_TYPE_SET);
        accommodation.setDailyRate(DEFAULT_DAILY_RATE_2);
        accommodation.setAvailability(AVAILABILITY_COUNT);
        return accommodation;
    }

    private static Accommodation getAccommodationCondo() {
        Accommodation accommodation = new Accommodation();
        accommodation.setId(DEFAULT_ID_TWO);
        accommodation.setType(ACCOMMODATION_TYPE_CONDO);
        accommodation.setLocation(ADDRESS);
        accommodation.setSize(ACCOMMODATION_SIZE_ONE_BEDROOM);
        accommodation.setAmenities(AMENITY_TYPE_SET);
        accommodation.setDailyRate(DEFAULT_DAILY_RATE);
        accommodation.setAvailability(AVAILABILITY_COUNT);
        return accommodation;
    }

    private static AccommodationDto getAccommodationDtoStudio() {
        AccommodationDto accommodationDto = new AccommodationDto();
        accommodationDto.setId(DEFAULT_ID_ONE);
        accommodationDto.setType(ACCOMMODATION_TYPE_APARTMENT.getName().name());
        accommodationDto.setLocation(ADDRESS.toString());
        accommodationDto.setSize(ACCOMMODATION_SIZE_STUDIO);
        accommodationDto.setAmenityIds(AMENITY_IDS_SET);
        accommodationDto.setDailyRate(DEFAULT_DAILY_RATE);
        accommodationDto.setAvailability(AVAILABILITY_COUNT);
        return accommodationDto;
    }

    private static AccommodationDto getAccommodationDtoUpdatedToHouse() {
        AccommodationDto accommodationDto = new AccommodationDto();
        accommodationDto.setId(DEFAULT_ID_ONE);
        accommodationDto.setType(ACCOMMODATION_TYPE_HOUSE.getName().name());
        accommodationDto.setLocation(ADDRESS.toString());
        accommodationDto.setSize(ACCOMMODATION_SIZE_TWO_BEDROOM);
        accommodationDto.setAmenityIds(AMENITY_IDS_SET);
        accommodationDto.setDailyRate(DEFAULT_DAILY_RATE_2);
        accommodationDto.setAvailability(AVAILABILITY_COUNT);
        return accommodationDto;
    }

    private static Role getRoleAdmin() {
        Role role = new Role();
        role.setId(DEFAULT_ID_ONE);
        role.setRole(ROLE_NAME_ADMIN);
        return role;
    }

    private static Role getRoleCustomer() {
        Role role = new Role();
        role.setId(DEFAULT_ID_TWO);
        role.setRole(ROLE_NAME_CUSTOMER);
        return role;
    }

    private static User getUserAdmin() {
        User user = new User();
        user.setId(DEFAULT_ID_ONE);
        user.setFirstName(ADMIN_FIRST_NAME_FIELD);
        user.setLastName(ADMIN_LAST_NAME_FIELD);
        user.setEmail(ADMIN_EMAIL_FIELD);
        user.setPassword(ADMIN_PASSWORD_FIELD);
        user.setRoles(Set.of(ROLE_ADMIN));
        return user;
    }

    private static User getUserCustomer() {
        User user = new User();
        user.setId(DEFAULT_ID_TWO);
        user.setFirstName(CUSTOMER_FIRST_NAME_FIELD);
        user.setLastName(CUSTOMER_LAST_NAME_FIELD);
        user.setEmail(CUSTOMER_EMAIL_FIELD);
        user.setPassword(CUSTOMER_PASSWORD_FIELD);
        user.setRoles(Set.of(ROLE_CUSTOMER));
        return user;
    }

    private static User getUserCustomer2() {
        User user = new User();
        user.setId(DEFAULT_ID_THREE);
        user.setFirstName(ANOTHER_CUSTOMER_FIRST_NAME_FIELD);
        user.setLastName(ANOTHER_CUSTOMER_LAST_NAME_FIELD);
        user.setEmail(ANOTHER_CUSTOMER_EMAIL_FIELD);
        user.setPassword(ANOTHER_CUSTOMER_PASSWORD_FIELD);
        user.setRoles(Set.of(ROLE_CUSTOMER));
        return user;
    }

    private static User getUserCustomerUpdatedRole() {
        User user = new User();
        user.setId(DEFAULT_ID_TWO);
        user.setFirstName(CUSTOMER_FIRST_NAME_FIELD);
        user.setLastName(CUSTOMER_LAST_NAME_FIELD);
        user.setEmail(CUSTOMER_EMAIL_FIELD);
        user.setPassword(CUSTOMER_PASSWORD_FIELD);
        user.setRoles(Set.of(ROLE_ADMIN));
        return user;
    }

    private static BookingStatus getBookingStatusPending() {
        BookingStatus bookingStatus = new BookingStatus();
        bookingStatus.setId(DEFAULT_ID_ONE);
        bookingStatus.setStatus(STATUS_PENDING);
        return bookingStatus;
    }

    private static BookingStatus getBookingStatusConfirmed() {
        BookingStatus bookingStatus = new BookingStatus();
        bookingStatus.setId(DEFAULT_ID_TWO);
        bookingStatus.setStatus(STATUS_CONFIRMED);
        return bookingStatus;
    }

    private static BookingStatus getBookingStatusCancelled() {
        BookingStatus bookingStatus = new BookingStatus();
        bookingStatus.setId(DEFAULT_ID_THREE);
        bookingStatus.setStatus(STATUS_CANCELLED);
        return bookingStatus;
    }

    private static BookingStatus getBookingStatusExpired() {
        BookingStatus bookingStatus = new BookingStatus();
        bookingStatus.setId(DEFAULT_ID_FOUR);
        bookingStatus.setStatus(STATUS_EXPIRED);
        return bookingStatus;
    }

    private static Booking getBookingStudioPending() {
        Booking booking = new Booking();
        booking.setId(DEFAULT_ID_ONE);
        booking.setCheckInDate(DEFAULT_CHECK_IN_DATE);
        booking.setCheckOutDate(DEFAULT_CHECK_OUT_DATE);
        booking.setAccommodation(ACCOMMODATION_STUDIO);
        booking.setUser(USER_CUSTOMER);
        booking.setStatus(BOOKING_STATUS_PENDING);
        return booking;
    }

    private static Booking getBookingStudioConfirmed() {
        Booking booking = new Booking();
        booking.setId(DEFAULT_ID_ONE);
        booking.setCheckInDate(DEFAULT_CHECK_IN_DATE);
        booking.setCheckOutDate(DEFAULT_CHECK_OUT_DATE);
        booking.setAccommodation(ACCOMMODATION_STUDIO);
        booking.setUser(USER_CUSTOMER);
        booking.setStatus(BOOKING_STATUS_CONFIRMED);
        return booking;
    }

    private static Booking getUpdatedBookingStudio() {
        Booking booking = new Booking();
        booking.setId(DEFAULT_ID_ONE);
        booking.setCheckInDate(UPDATE_CHECK_IN_DATE);
        booking.setCheckOutDate(UPDATE_CHECK_OUT_DATE);
        booking.setAccommodation(ACCOMMODATION_STUDIO);
        booking.setUser(USER_CUSTOMER);
        booking.setStatus(BOOKING_STATUS_PENDING);
        return booking;
    }

    private static Booking getConflictingBooking() {
        Booking booking = new Booking();
        booking.setId(DEFAULT_ID_TWO);
        booking.setCheckInDate(UPDATE_CHECK_IN_DATE);
        booking.setCheckOutDate(UPDATE_CHECK_OUT_DATE);
        booking.setAccommodation(ACCOMMODATION_STUDIO);
        booking.setUser(USER_CUSTOMER_2);
        booking.setStatus(BOOKING_STATUS_PENDING);
        return booking;
    }

    private static Booking getCanceledBookingStudio() {
        Booking booking = new Booking();
        booking.setId(DEFAULT_ID_ONE);
        booking.setCheckInDate(DEFAULT_CHECK_IN_DATE);
        booking.setCheckOutDate(DEFAULT_CHECK_OUT_DATE);
        booking.setAccommodation(ACCOMMODATION_STUDIO);
        booking.setUser(USER_CUSTOMER);
        booking.setStatus(BOOKING_STATUS_CANCELLED);
        return booking;
    }

    private static Booking getExpiredBookingCondo() {
        Booking booking = new Booking();
        booking.setId(DEFAULT_ID_THREE);
        booking.setCheckInDate(EXPIRED_CHECK_IN_DATE);
        booking.setCheckOutDate(EXPIRED_CHECK_OUT_DATE);
        booking.setAccommodation(ACCOMMODATION_CONDO);
        booking.setUser(USER_CUSTOMER_2);
        booking.setStatus(BOOKING_STATUS_EXPIRED);
        return booking;
    }

    private static PaymentStatus getPaymentStatusPending() {
        PaymentStatus paymentStatus = new PaymentStatus();
        paymentStatus.setId(DEFAULT_ID_ONE);
        paymentStatus.setStatus(PENDING);
        return paymentStatus;
    }

    private static PaymentStatus getPaymentStatusPaid() {
        PaymentStatus paymentStatus = new PaymentStatus();
        paymentStatus.setId(DEFAULT_ID_TWO);
        paymentStatus.setStatus(PAID);
        return paymentStatus;
    }

    @SneakyThrows
    private static Payment getPaymentPending() {
        Payment payment = new Payment();
        payment.setId(DEFAULT_ID_ONE);
        payment.setStatus(PAYMENT_STATUS_PENDING);
        payment.setBooking(BOOKING_STUDIO_PENDING);
        payment.setSessionUrl(new URL(SESSION_URL));
        payment.setSessionId(SESSION_ID);
        payment.setAmountToPay(PAYMENT_AMOUNT);
        return payment;
    }

    @SneakyThrows
    private static Payment getPaymentPaid() {
        Payment payment = new Payment();
        payment.setId(DEFAULT_ID_ONE);
        payment.setStatus(PAYMENT_STATUS_PAID);
        payment.setBooking(BOOKING_STUDIO_CONFIRMED);
        payment.setSessionUrl(new URL(SESSION_URL));
        payment.setSessionId(SESSION_ID);
        payment.setAmountToPay(PAYMENT_AMOUNT);
        return payment;
    }

    private static PaymentResponse getPaymentPendingResponse() {
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setId(DEFAULT_ID_ONE);
        paymentResponse.setPaymentStatus(PENDING.name());
        paymentResponse.setBookingId(String.valueOf(DEFAULT_ID_ONE));
        paymentResponse.setSessionUrl(SESSION_URL);
        paymentResponse.setSessionId(SESSION_ID);
        paymentResponse.setAmount(String.valueOf(PAYMENT_AMOUNT));
        return paymentResponse;
    }

    private static PaymentResponse getPaymentPaidResponse() {
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setId(DEFAULT_ID_ONE);
        paymentResponse.setPaymentStatus(PAID.name());
        paymentResponse.setBookingId(String.valueOf(DEFAULT_ID_ONE));
        paymentResponse.setSessionUrl(SESSION_URL);
        paymentResponse.setSessionId(SESSION_ID);
        paymentResponse.setAmount(String.valueOf(PAYMENT_AMOUNT));
        return paymentResponse;
    }

    private static TelegramChat getSubscribedTelegramChat() {
        TelegramChat telegramChat = new TelegramChat();
        telegramChat.setChatId(DEFAULT_ID_SEVEN);
        telegramChat.setUser(USER_CUSTOMER);
        telegramChat.setSubscribed(true);
        return telegramChat;
    }

    private static TelegramChat getUnsubscribedTelegramChat() {
        TelegramChat telegramChat = new TelegramChat();
        telegramChat.setChatId(DEFAULT_ID_SEVEN);
        telegramChat.setUser(USER_CUSTOMER);
        telegramChat.setSubscribed(false);
        return telegramChat;
    }
}
