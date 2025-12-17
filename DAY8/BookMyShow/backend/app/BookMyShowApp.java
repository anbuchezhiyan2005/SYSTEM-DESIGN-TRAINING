package DAY8.BookMyShow.backend.app;

import DAY8.BookMyShow.backend.config.MongoDBConnectionManager;
import DAY8.BookMyShow.backend.models.*;
import DAY8.BookMyShow.backend.enums.SeatType;
import DAY8.BookMyShow.backend.repositories.*;
import DAY8.BookMyShow.backend.services.*;
import DAY8.BookMyShow.backend.strategy.UpiPaymentStrategy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class BookMyShowApp {
    
    public static void main(String[] args) {
        // Initialize MongoDB connection
        MongoDBConnectionManager.initialize("mongodb://localhost:27017", "bookmyshow");

        // Add shutdown hook to close MongoDB connection
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nClosing MongoDB connection...");
            MongoDBConnectionManager.close();
        }));

        // Clear database before starting
        clearDatabase();

        // Initialize Repositories
        UserRepository userRepository = new UserRepositoryImpl();
        MovieRepository movieRepository = new MovieRepositoryImpl();
        TheatreRepository theatreRepository = new TheatreRepositoryImpl();
        ShowRepository showRepository = new ShowRepositoryImpl();
        SeatRepository seatRepository = new SeatRepositoryImpl();
        BookingRepository bookingRepository = new BookingRepositoryImpl();
        PaymentRepository paymentRepository = new PaymentRepositoryImpl();
        ReceiptRepository receiptRepository = new ReceiptRepositoryImpl();

        // Initialize Services
        UserService userService = new UserService(userRepository);
        MovieService movieService = new MovieService(movieRepository);
        TheatreService theatreService = new TheatreService(theatreRepository, showRepository);
        ShowService showService = new ShowService(showRepository);
        SeatService seatService = new SeatService(seatRepository);
        BookingService bookingService = new BookingService(bookingRepository, showRepository, seatService);
        PaymentService paymentService = new PaymentService(paymentRepository, bookingRepository);
        ReceiptService receiptService = new ReceiptService(receiptRepository, bookingRepository, 
                                                           userRepository, showRepository, movieRepository, 
                                                           theatreRepository, paymentRepository);

        try {
            System.out.println("========================================");
            System.out.println("   BOOKMYSHOW - END TO END DEMO");
            System.out.println("========================================\n");

            // STEP 1: Register User
            System.out.println("STEP 1: Register User");
            System.out.println("----------------------------------------");
            User user = userService.registerUser("Arun", "arun@gmail.com", "password123", "9876543210");
            System.out.println("✓ User registered: " + user.getName() + " (" + user.getEmail() + ")\n");

            // STEP 2: Add Movie
            System.out.println("STEP 2: Add Movie");
            System.out.println("----------------------------------------");
            Movie movie = new Movie("MOVIE001", "Inception", "A mind-bending thriller", 148, 
                                   "Sci-Fi", "English", LocalDate.of(2010, 7, 16));
            movieService.addMovie(movie);
            System.out.println("✓ Movie added: " + movie.getTitle() + "\n");

            // STEP 3: Add Theatre
            System.out.println("STEP 3: Add Theatre");
            System.out.println("----------------------------------------");
            Theatre theatre = new Theatre("THEATRE001", "PVR Cinemas", "Phoenix Mall", "Bangalore", 100);
            theatreService.addTheatre(theatre);
            System.out.println("✓ Theatre added: " + theatre.getName() + " at " + theatre.getCity() + "\n");

            // STEP 4: Add Show
            System.out.println("STEP 4: Add Show");
            System.out.println("----------------------------------------");
            Show show = new Show("SHOW001", "MOVIE001", "THEATRE001", 
                               LocalDateTime.now().plusDays(1), 250.0, 10);
            showService.addShow(show);
            System.out.println("✓ Show added for movie: " + movie.getTitle());
            System.out.println("  Show Time: " + show.getShowTime());
            System.out.println("  Price: ?" + show.getPrice() + " per seat\n");

            // STEP 5: Add Seats for the Show
            System.out.println("STEP 5: Add Seats");
            System.out.println("----------------------------------------");
            List<String> seatIds = Arrays.asList("SEAT001", "SEAT002", "SEAT003", "SEAT004", "SEAT005",
                                                 "SEAT006", "SEAT007", "SEAT008", "SEAT009", "SEAT010");
            for (int i = 0; i < 10; i++) {
                Seat seat = new Seat(seatIds.get(i), "SHOW001", "A" + (i+1), SeatType.REGULAR);
                seatService.addSeat(seat);
            }
            System.out.println("✓ 10 seats added for the show\n");

            // STEP 6: Browse Available Seats
            System.out.println("STEP 6: Browse Available Seats");
            System.out.println("----------------------------------------");
            List<Seat> availableSeats = seatService.getAvailableSeats("SHOW001");
            System.out.println("Available seats: " + availableSeats.size());
            availableSeats.forEach(seat -> System.out.println("  - " + seat.getSeatNumber() + " (" + seat.getSeatType() + ")"));
            System.out.println();

            // STEP 7: Create Booking
            System.out.println("STEP 7: Create Booking");
            System.out.println("----------------------------------------");
            List<String> selectedSeats = Arrays.asList("SEAT001", "SEAT002", "SEAT003", "SEAT004");
            Booking booking = bookingService.createBooking(user.getUserId(), "SHOW001", selectedSeats);
            System.out.println("✓ Booking created!");
            System.out.println("  Booking ID: " + booking.getBookingId());
            System.out.println("  Total Amount: ?" + booking.getTotalAmount());
            System.out.println("  Status: " + booking.getBookingStatus() + "\n");

            // STEP 8: Process Payment
            System.out.println("STEP 8: Process Payment");
            System.out.println("----------------------------------------");
            UpiPaymentStrategy upiPayment = new UpiPaymentStrategy("arun@upi");
            Payment payment = paymentService.processPayment(booking.getBookingId(), 
                                                           booking.getTotalAmount(), upiPayment);
            System.out.println("\n✓ Payment processed!");
            System.out.println("  Payment ID: " + payment.getPaymentId());
            System.out.println("  Transaction ID: " + payment.getTransactionId());
            System.out.println("  Status: " + payment.getPaymentStatus() + "\n");

            // STEP 9: Confirm Booking
            System.out.println("STEP 9: Confirm Booking");
            System.out.println("----------------------------------------");
            Booking confirmedBooking = bookingService.confirmBooking(booking.getBookingId());
            System.out.println("✓ Booking confirmed!");
            System.out.println("  Status: " + confirmedBooking.getBookingStatus() + "\n");

            // STEP 10: Generate Receipt
            System.out.println("STEP 10: Generate Receipt");
            System.out.println("----------------------------------------");
            Receipt receipt = receiptService.generateReceipt(booking.getBookingId());
            System.out.println(receipt.getReceiptDetails());

            // Verification
            System.out.println("\n\nVERIFICATION");
            System.out.println("========================================");
            System.out.println("Total Users: " + MongoDBConnectionManager.getDatabase().getCollection("users").countDocuments());
            System.out.println("Total Movies: " + MongoDBConnectionManager.getDatabase().getCollection("movies").countDocuments());
            System.out.println("Total Theatres: " + MongoDBConnectionManager.getDatabase().getCollection("theatres").countDocuments());
            System.out.println("Total Shows: " + MongoDBConnectionManager.getDatabase().getCollection("shows").countDocuments());
            System.out.println("Total Seats: " + MongoDBConnectionManager.getDatabase().getCollection("seats").countDocuments());
            System.out.println("Total Bookings: " + MongoDBConnectionManager.getDatabase().getCollection("bookings").countDocuments());
            System.out.println("Total Payments: " + MongoDBConnectionManager.getDatabase().getCollection("payments").countDocuments());
            System.out.println("Total Receipts: " + MongoDBConnectionManager.getDatabase().getCollection("receipts").countDocuments());
            System.out.println("========================================");

        } catch (Exception e) {
            System.out.println("\nERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void clearDatabase() {
        System.out.println("Clearing existing database...");
        MongoDBConnectionManager.getDatabase().drop();
        System.out.println("✓ Database cleared\n");
    }
}
