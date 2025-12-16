package study;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import java.util.function.Function;

import java.time.LocalDate;

@Configuration
public class Functions {

    @Description("Calculate a date after adding days from today")
    @Bean
    public Function<AddDayRequest, DateResponse> addDaysFromToday() {
        return request ->
                new DateResponse(
                        LocalDate.now().plusDays(request.days())
                );
    }
}

