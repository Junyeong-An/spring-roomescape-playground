package roomescape;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoomDAO {
    private static JdbcTemplate jdbcTemplate;

    public RoomDAO(JdbcTemplate jdbcTemplate) {
        RoomDAO.jdbcTemplate = jdbcTemplate;
    }
    public List<Reservation> findAll() {
        return jdbcTemplate.query("SELECT * FROM reservation", (resultSet, rowNum) -> new Reservation(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("date"),
                resultSet.getString("time")
        ));
    }

}
