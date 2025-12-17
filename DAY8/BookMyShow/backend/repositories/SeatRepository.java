package repositories;

import models.Seat;
import java.util.List;

public interface SeatRepository {
    Seat save(Seat seat);
    Seat findById(String seatId);
    List<Seat> findByShowId(String showId);
    List<Seat> findAvailableByShowId(String showId);
    boolean updateAvailability(String seatId, boolean isAvailable);
    boolean delete(String seatId);
}
