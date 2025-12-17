package services;

import models.Booking;
import models.Show;
import enums.BookingStatus;
import enums.PaymentStatus;
import repositories.BookingRepository;
import repositories.ShowRepository;

import java.util.List;
import java.util.UUID;

public class BookingService {
    private BookingRepository bookingRepository;
    private ShowRepository showRepository;
    private SeatService seatService;

    public BookingService(BookingRepository bookingRepository, ShowRepository showRepository, SeatService seatService) {
        this.bookingRepository = bookingRepository;
        this.showRepository = showRepository;
        this.seatService = seatService;
    }

    public Booking createBooking(String userId, String showId, List<String> seatIds) {
        Show show = showRepository.findById(showId);
        if (show == null) {
            throw new RuntimeException("Show not found with ID: " + showId);
        }

        // Check if enough seats available
        if (show.getAvailableSeats() < seatIds.size()) {
            throw new RuntimeException("Not enough seats available!");
        }

        // Block seats
        boolean seatsBlocked = seatService.blockSeats(seatIds);
        if (!seatsBlocked) {
            throw new RuntimeException("Failed to block seats. Some seats may be already booked!");
        }

        // Calculate total amount
        double totalAmount = calculateTotalAmount(showId, seatIds);

        // Create booking
        String bookingId = "BOOK_" + UUID.randomUUID().toString().substring(0, 8);
        Booking booking = new Booking(bookingId, userId, showId, totalAmount);
        booking.setSeatIds(seatIds);

        // Update show available seats using update method instead of save
        int newAvailableSeats = show.getAvailableSeats() - seatIds.size();
        showRepository.updateAvailableSeats(showId, newAvailableSeats);

        return bookingRepository.save(booking);
    }

    public Booking confirmBooking(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId);
        if (booking == null) {
            throw new RuntimeException("Booking not found with ID: " + bookingId);
        }

        booking.setBookingStatus(BookingStatus.CONFIRMED);
        booking.setPaymentStatus(PaymentStatus.SUCCESS);
        
        bookingRepository.updateBookingStatus(bookingId, BookingStatus.CONFIRMED);
        bookingRepository.updatePaymentStatus(bookingId, PaymentStatus.SUCCESS);

        return booking;
    }

    public boolean cancelBooking(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId);
        if (booking == null) {
            throw new RuntimeException("Booking not found with ID: " + bookingId);
        }

        // Release seats
        seatService.releaseSeats(booking.getSeatIds());

        // Update show available seats
        Show show = showRepository.findById(booking.getShowId());
        int newAvailableSeats = show.getAvailableSeats() + booking.getSeatIds().size();
        showRepository.updateAvailableSeats(booking.getShowId(), newAvailableSeats);

        // Update booking status
        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.updateBookingStatus(bookingId, BookingStatus.CANCELLED);

        return true;
    }

    public Booking getBookingById(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId);
        if (booking == null) {
            throw new RuntimeException("Booking not found with ID: " + bookingId);
        }
        return booking;
    }

    public List<Booking> getUserBookings(String userId) {
        return bookingRepository.findByUserId(userId);
    }

    public double calculateTotalAmount(String showId, List<String> seatIds) {
        Show show = showRepository.findById(showId);
        if (show == null) {
            throw new RuntimeException("Show not found with ID: " + showId);
        }
        return show.getPrice() * seatIds.size();
    }
}
