package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.RoomDAO;
import roomescape.domain.Reservation;

@Service
public class ReservationService {
    private final RoomDAO roomDAO;

    public ReservationService(RoomDAO roomDAO) {
        this.roomDAO = roomDAO;
    }

    public void addReservation(Reservation reservation) {
        roomDAO.insert(reservation);
    }
}
