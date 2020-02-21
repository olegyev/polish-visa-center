package app.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class VisaApplicationWithHistoryDto extends VisaApplicationDto {

    private List<ApplicationStatusHistoryDto> history;

}