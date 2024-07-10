package roomescape.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.Reservation;
import roomescape.dao.RoomDAO;
import roomescape.dto.ReservationDto;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class RoomescapeController {

    private final roomescape.dao.RoomDAO RoomDAO;

    public RoomescapeController(RoomDAO roomDAO) {
        RoomDAO = roomDAO;
    }

    @GetMapping("/reservation")
    public String reservation() {
        return "reservation";
    }

    @GetMapping("/reservations")
    @ResponseBody
    public ResponseEntity<List<ReservationDto>> getAllReservations(){
        List<ReservationDto> reservations = RoomDAO.findAll().stream()
                .map(reservation -> new ReservationDto(reservation.getName(), reservation.getDate(), reservation.getTime()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @PostMapping("/reservations")
    @ResponseBody
    public ResponseEntity<ReservationDto> createReservation( @RequestBody ReservationDto reservationDto) {
        if (reservationDto.name() == null || reservationDto.date() == null || reservationDto.time() == null) {
            throw new IllegalArgumentException("예약 정보가 부족합니다.");
        }
        Reservation reservation = new Reservation(0, reservationDto.name(), reservationDto.date(), reservationDto.time());
        RoomDAO.insert(reservation);
        HttpHeaders headers = new HttpHeaders();

        headers.add("Location", "/reservations/" + RoomDAO.getId(reservation));
        return new ResponseEntity<>(reservationDto,headers, HttpStatus.CREATED);
    }

    @DeleteMapping("/reservations/{id}")
    @ResponseBody
    public ResponseEntity<Reservation> deleteReservation(@PathVariable int id){
            if (RoomDAO.findById(id) != null) {
                RoomDAO.delete(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        // 예약이 없는 경우 Exception 발생
        throw new IllegalArgumentException("삭제할 예약이 없습니다.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
