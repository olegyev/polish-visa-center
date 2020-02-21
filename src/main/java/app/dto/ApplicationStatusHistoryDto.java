package app.dto;

import app.domain.enums.VisaApplicationStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ApplicationStatusHistoryDto {

    private VisaApplicationStatus applicationStatus;
    private Date settingDate;
    private URI operatorLink;

}