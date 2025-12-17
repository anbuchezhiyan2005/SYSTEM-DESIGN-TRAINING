package DAY8.BookMyShow.backend.repositories;

import DAY8.BookMyShow.backend.models.Theatre;
import java.util.List;

public interface TheatreRepository {
    Theatre save(Theatre theatre);
    Theatre findById(String theatreId);
    List<Theatre> findAll();
    List<Theatre> findByCity(String city);
    boolean delete(String theatreId);
}
