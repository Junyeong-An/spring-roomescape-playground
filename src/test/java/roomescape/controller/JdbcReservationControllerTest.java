package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import roomescape.domain.Reservation;
import roomescape.service.ReservationService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("jdbc")
public class JdbcReservationControllerTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ReservationService reservationService;

	@Test
	void JDBC_연결_성공() {
		try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
			assertThat(connection).isNotNull();
			assertThat(connection.getCatalog()).isEqualTo("DATABASE");
			assertThat(connection.getMetaData().getTables(null, null, "RESERVATION", null).next()).isTrue();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	void JDBC_예약_조회_동기화() {
		jdbcTemplate.update("INSERT INTO reservation (name, date, time) VALUES (?, ?, ?)", "브라운", "2023-08-05",
				"15:40");

		List<Reservation> reservations = RestAssured.given().log().all()
				.when().get("/reservations")
				.then().log().all()
				.statusCode(200).extract()
				.jsonPath().getList(".", Reservation.class);

		Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);

		assertThat(reservations.size()).isEqualTo(count);
	}

	@Test
	void JDBC_예약_추가_이후_삭제() {
		Map<String, String> params = new HashMap<>();
		params.put("name", "브라운");
		params.put("date", "2023-08-05");
		params.put("time", "10:00");

		RestAssured.given().log().all()
				.contentType(ContentType.JSON)
				.body(params)
				.when().post("/reservations")
				.then().log().all()
				.statusCode(201)
				.header("Location", "/reservations/1");

		Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
		assertThat(count).isEqualTo(1);

		RestAssured.given().log().all()
				.when().delete("/reservations/1")
				.then().log().all()
				.statusCode(204);

		Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
		assertThat(countAfterDelete).isEqualTo(0);
	}
}
