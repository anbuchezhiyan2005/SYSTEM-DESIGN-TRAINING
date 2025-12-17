package DAY8.BookMyShow.backend.repositories;

import DAY8.BookMyShow.backend.models.Receipt;

public interface ReceiptRepository {
    Receipt save(Receipt receipt);
    Receipt findById(String receiptId);
    Receipt findByBookingId(String bookingId);
    boolean delete(String receiptId);
}
