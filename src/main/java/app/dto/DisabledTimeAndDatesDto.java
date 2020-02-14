package app.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DisabledTimeAndDatesDto {

    private String city;
    private Map<String, String[]> disabledTimeByDate;
    private String[] disabledDates;

}