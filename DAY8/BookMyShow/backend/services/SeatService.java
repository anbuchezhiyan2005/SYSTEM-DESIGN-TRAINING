package DAY8.BookMyShow.backend.services;

import DAY8.BookMyShow.backend.models.Seat;
import DAY8.BookMyShow.backend.repositories.SeatRepository;

import java.util.List;

public class SeatService {
    private SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public Seat addSeat(Seat seat) {
        return seatRepository.save(seat);
    }

    public List<Seat> getSeatsByShow(String showId) {
        return seatRepository.findByShowId(showId);
    }

    public List<Seat> getAvailableSeats(String showId) {
        return seatRepository.findAvailableByShowId(showId);
    }

    public boolean blockSeats(List<String> seatIds) {
        boolean allBlocked = true;
        for (String seatId : seatIds) {
            Seat seat = seatRepository.findById(seatId);
            if (seat == null || !seat.isAvailable()) {
                allBlocked = false;
                break;
            }
            seatRepository.updateAvailability(seatId, false);
        }
        return allBlocked;
    }

    public boolean releaseSeats(List<String> seatIds) {
        boolean allReleased = true;
        for (String seatId : seatIds) {
            boolean updated = seatRepository.updateAvailability(seatId, true);
            if (!updated) {
                allReleased = false;
            }
        }
        return allReleased;
    }
}
