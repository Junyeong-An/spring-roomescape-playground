package roomescape.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.Time;
import roomescape.dto.TimeDto;
import roomescape.dto.TimeResDto;
import roomescape.service.TimeService;

import java.util.List;

@RestController
@RequestMapping("/times")
public class TimeController {
    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<TimeResDto> addTime(@RequestBody TimeDto timeDto) {
        TimeResDto timeResDto = timeService.addTime(timeDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/times/" + timeResDto.id());
        return new ResponseEntity<>(timeResDto, headers, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<TimeResDto>> getAllTimes() {
        List<TimeResDto> times = timeService.getAllTimes();
        return new ResponseEntity<>(times, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable int id) {
        timeService.deleteTime(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
