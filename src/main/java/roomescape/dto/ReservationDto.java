package roomescape.dto;

public record ReservationDto(
        String name, String date, String time
) {
    public static ReservationDto from(String name, String date, String time) {
        return new ReservationDto(name, date, time);
    }
}

