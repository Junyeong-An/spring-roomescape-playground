package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.RoomDAO;
import roomescape.domain.Reservation;
import roomescape.domain.Time;
import roomescape.dto.ReservationDto;
import roomescape.dto.TimeResDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    private final RoomDAO roomDAO;
    private final TimeService timeService;

    public ReservationService(RoomDAO roomDAO, TimeService timeService) {
        this.roomDAO = roomDAO;
        this.timeService = timeService;
    }

    public List<ReservationDto> getAllReservations() {
        return roomDAO.findAll().stream()
                .map(reservation -> new ReservationDto(reservation.getName(), reservation.getDate(), reservation.getTime()))
                .collect(Collectors.toList());
    }

    public Reservation addReservation(ReservationDto reservationDto) {
        String name = reservationDto.name();
        String date = reservationDto.date();
        String timeString = reservationDto.time().getTime();

        TimeResDto timeResDto = timeService.findByTime(timeString);
        Time time = new Time(timeResDto.id(), timeResDto.time());


        Reservation reservation = new Reservation(0, name, date, time);
        roomDAO.insert(reservation);
        return reservation;
    }

    public void deleteReservation(int id) {
        if (roomDAO.findById(id) != null) {
            roomDAO.deletebyId(id);
        } else {
            throw new IllegalArgumentException("삭제할 예약이 없습니다.");
        }
    }
}
