package DAY8.BookMyShow.backend.repositories;

import DAY8.BookMyShow.backend.models.Booking;
import DAY8.BookMyShow.backend.enums.BookingStatus;
import DAY8.BookMyShow.backend.enums.PaymentStatus;

import java.util.List;

public interface BookingRepository {
    Booking save(Booking booking);
    Booking findById(String bookingId);
    List<Booking> findAll();
    List<Booking> findByUserId(String userId);
    boolean updateBookingStatus(String bookingId, BookingStatus status);
    boolean updatePaymentStatus(String bookingId, PaymentStatus status);
    boolean delete(String bookingId);
}
