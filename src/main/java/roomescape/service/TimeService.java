package roomescape.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.dao.TimeDAO;
import roomescape.domain.Time;
import roomescape.dto.TimeDto;
import roomescape.dto.TimeResDto;

@Service
public class TimeService {
    private final TimeDAO timeDAO;

    public TimeService(TimeDAO timeDAO) {
        this.timeDAO = timeDAO;
    }

    public TimeResDto addTime(TimeDto timeDto) {
        Time time = new Time(0, timeDto.time());
        timeDAO.insert(time);
        int id = timeDAO.getId(time)
                .orElseThrow(() -> new IllegalArgumentException("Time ID를 찾을 수 없습니다."));
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
        Time timeResult = timeDAO.findByTime(time)
                .orElseThrow(() -> new IllegalArgumentException("해당 시간이 존재하지 않습니다."));
        return TimeResDto.from(timeResult);
    }

}
