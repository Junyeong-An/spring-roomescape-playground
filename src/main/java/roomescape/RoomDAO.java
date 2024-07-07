package roomescape;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoomDAO {
    private final JdbcTemplate jdbcTemplate;

    public RoomDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public List<Reservation> findAll() {
        return jdbcTemplate.query("SELECT * FROM reservation", (resultSet, rowNum) -> new Reservation(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("date"),
                resultSet.getString("time")
        ));
    }

    public void insert(Reservation reservation) {
        jdbcTemplate.update("INSERT INTO reservation (name, date, time) VALUES (?, ?, ?)",
                reservation.getName(), reservation.getDate(), reservation.getTime());
    }
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }
    public int getId(Reservation reservation) {
        return jdbcTemplate.queryForObject("SELECT id FROM reservation WHERE name = ? AND date = ? AND time = ?",
                Integer.class, reservation.getName(), reservation.getDate(), reservation.getTime());
    }
    public Reservation findById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM reservation WHERE id = ?", (resultSet, rowNum) -> new Reservation(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("date"),
                resultSet.getString("time")
        ), id);
    }

}
