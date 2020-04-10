package app.dto;

import app.domain.enums.VisaApplicationStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ApplicationStatusHistoryDto extends AbstractDto {

    private VisaApplicationStatus applicationStatus;

    @JsonFormat(timezone = "Europe/Minsk")
    private Date settingDate;

    private URI operatorLink;

}