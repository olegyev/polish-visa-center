package app.dto.assemblers.impl;

import app.controllers.VisaApplicationController;
import app.domain.ApplicationStatusHistory;
import app.domain.Client;
import app.domain.VisaApplication;
import app.dto.ApplicationStatusHistoryDto;
import app.dto.ClientVisaApplicationWithHistoryDto;
import app.dto.VisaApplicationWithHistoryDto;
import app.dto.assemblers.DtoAssemblerInterface;
import app.services.VisaApplicationServiceInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ClientVisaApplicationWithHistoryDtoAssembler
        extends RepresentationModelAssemblerSupport<VisaApplication, ClientVisaApplicationWithHistoryDto>
        implements DtoAssemblerInterface<VisaApplication, ClientVisaApplicationWithHistoryDto> {

    private final VisaApplicationServiceInterface visaApplicationService;

    @Autowired
    public ClientVisaApplicationWithHistoryDtoAssembler(final VisaApplicationServiceInterface visaApplicationService) {
        super(VisaApplicationController.class, ClientVisaApplicationWithHistoryDto.class);
        this.visaApplicationService = visaApplicationService;
    }

    @Override
    public ClientVisaApplicationWithHistoryDto toModel(VisaApplication visaApplication) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ClientVisaApplicationWithHistoryDto dto = instantiateModel(visaApplication);
        Client client = visaApplication.getClient();

        dto.setFirstName(client.getFirstName());
        dto.setLastName(client.getLastName());
        dto.setEmail(client.getEmail());
        dto.setPhoneNumber(client.getPhoneNumber());
        dto.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").format(client.getDateOfBirth()));
        dto.setPassportId(client.getPassportId());
        dto.setOccupation(client.getOccupation().toString());

        VisaApplicationWithHistoryDto visaApplicationWithHistoryDto = new VisaApplicationWithHistoryDto();
        visaApplicationWithHistoryDto.setRequiredVisaType(visaApplication.getRequiredVisaType().toString());
        visaApplicationWithHistoryDto.setCity(visaApplication.getCity().toString());
        visaApplicationWithHistoryDto.setAppointmentDate(visaApplication.getAppointmentDate().toString());
        visaApplicationWithHistoryDto.setAppointmentTime(visaApplication.getAppointmentTime());
        visaApplicationWithHistoryDto.setVisaApplicationStatus(visaApplication.getVisaApplicationStatus().toString());

        List<ApplicationStatusHistory> history = visaApplicationService.readVisaApplicationHistory(userDetails, visaApplication);

        List<ApplicationStatusHistoryDto> historyDtoList = new ArrayList<>();
        history.forEach(u -> {
            ApplicationStatusHistoryDto historyDto = new ApplicationStatusHistoryDto();
            historyDto.setApplicationStatus(u.getApplicationStatus());
            historyDto.setSettingDate(u.getSettingDate());
            URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/employees/" + u.getOperatorId()).build().toUri();
            historyDto.setOperatorLink(uri);
            historyDtoList.add(historyDto);
        });

        visaApplicationWithHistoryDto.setHistory(historyDtoList);

        dto.setApplication(visaApplicationWithHistoryDto);

        dto.add(linkTo(methodOn(VisaApplicationController.class)
                .getClientApplicationHistory(client.getId(), visaApplication.getId()))
                .withSelfRel());

        dto.add(linkTo(methodOn(VisaApplicationController.class)
                .getApplications(null, null, null, null,
                        null, null, null, null, null,
                        null, null, null, null))
                .withRel("applications"));

        return dto;
    }

}