package bg.fmi.web.marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private final int status;
    private final String message;
    private final LocalDate timestamp = LocalDate.now();
}
