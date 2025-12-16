package study;

import java.time.LocalDate;


public class DateResponse {
    private LocalDate date;

    public DateResponse(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

}
