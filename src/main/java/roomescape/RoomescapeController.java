package roomescape;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RoomescapeController {

    private final RoomDAO RoomDAO;

    public RoomescapeController(RoomDAO roomDAO) {
        RoomDAO = roomDAO;
    }

    @GetMapping("/reservation")
    public String reservation() {
        return "reservation";
    }

    @GetMapping("/reservations")
    @ResponseBody
    public ResponseEntity<List<Reservation>> getAllReservations(){
        return new ResponseEntity<>(RoomDAO.findAll(), HttpStatus.OK);
    }

    @PostMapping("/reservations")
    @ResponseBody
    public ResponseEntity<Reservation> createReservation( @RequestBody Reservation reservation) {
        RoomDAO.insert(reservation);
        HttpHeaders headers = new HttpHeaders();

        headers.add("Location", "/reservations/" + RoomDAO.getId(reservation));
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
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
