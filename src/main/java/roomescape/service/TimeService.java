package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.TimeDAO;
import roomescape.domain.Time;
import roomescape.dto.TimeDto;
import roomescape.dto.TimeResDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimeService {
    private final TimeDAO timeDAO;

    public TimeService(TimeDAO timeDAO) {
        this.timeDAO = timeDAO;
    }

    public TimeResDto addTime(TimeDto timeDto) {
        Time time = new Time(0, timeDto.time());
        timeDAO.insert(time);
        int id = timeDAO.getId(time);
        return new TimeResDto(id, time.getTime());
    }

    public List<TimeResDto> getAllTimes() {
        List<Time> times = timeDAO.findAll();
        return times.stream()
                .map(TimeResDto::from)
                .collect(Collectors.toList());
    }

    public void deleteTime(int id) {
        timeDAO.delete(id);
    }
    public TimeResDto findByTime(String time) {
        Time times = timeDAO.findByTime(time);
        return TimeResDto.from(times);

    }

}
