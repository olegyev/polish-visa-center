package app.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ClientVisaApplicationWithHistoryDto extends ClientDto {

    private VisaApplicationWithHistoryDto application;

}
