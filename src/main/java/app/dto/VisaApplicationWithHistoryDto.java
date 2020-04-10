package app.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VisaApplicationWithHistoryDto extends VisaApplicationDto {

    private List<ApplicationStatusHistoryDto> history;

}