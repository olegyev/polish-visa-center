package app.dto.assemblers.impl;

import app.controllers.VisaApplicationController;
import app.domain.Client;
import app.domain.VisaApplication;
import app.domain.VisaDocumentsInfo;
import app.dto.ClientVisaApplicationWithDocInfoDto;
import app.dto.VisaApplicationDto;
import app.dto.assemblers.DtoAssemblerInterface;
import app.services.VisaDocumentsInfoServiceInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ClientVisaApplicationWithDocInfoDtoAssembler
        extends RepresentationModelAssemblerSupport<VisaApplication, ClientVisaApplicationWithDocInfoDto>
        implements DtoAssemblerInterface<VisaApplication, ClientVisaApplicationWithDocInfoDto> {

    private final VisaDocumentsInfoServiceInterface docsInfoService;

    @Autowired
    public ClientVisaApplicationWithDocInfoDtoAssembler(final VisaDocumentsInfoServiceInterface docsInfoService) {
        super(VisaApplication.class, ClientVisaApplicationWithDocInfoDto.class);
        this.docsInfoService = docsInfoService;
    }

    @Override
    public ClientVisaApplicationWithDocInfoDto toModel(VisaApplication visaApplication) {
        ClientVisaApplicationWithDocInfoDto dto = instantiateModel(visaApplication);
        Client client = visaApplication.getClient();

        dto.setFirstName(client.getFirstName());
        dto.setLastName(client.getLastName());
        dto.setEmail(client.getEmail());
        dto.setPhoneNumber(client.getPhoneNumber());
        dto.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").format(client.getDateOfBirth()));
        dto.setPassportId(client.getPassportId());
        dto.setOccupation(client.getOccupation().toString());

        VisaApplicationDto visaApplicationDto = new VisaApplicationDto();
        visaApplicationDto.setRequiredVisaType(visaApplication.getRequiredVisaType().toString());
        visaApplicationDto.setCity(visaApplication.getCity().toString());
        visaApplicationDto.setAppointmentDate(new SimpleDateFormat("yyyy-MM-dd").format(visaApplication.getAppointmentDate()));
        visaApplicationDto.setAppointmentTime(visaApplication.getAppointmentTime());
        visaApplicationDto.setVisaApplicationStatus(visaApplication.getVisaApplicationStatus().toString());

        dto.setVisaApplication(visaApplicationDto);

        List<VisaDocumentsInfo> docInfo = docsInfoService.readByVisaTypeAndAndOccupation(visaApplication.getRequiredVisaType(), client.getOccupation());

        List<String> requiredDocs = docInfo.stream()
                .map(VisaDocumentsInfo::getDocDescription)
                .collect(Collectors.toList());

        dto.setRequiredDocs(requiredDocs);

        dto.add(linkTo(methodOn(VisaApplicationController.class)
                .getClientApplication(visaApplication.getClient().getId(), visaApplication.getId()))
                .withSelfRel());

        dto.add(linkTo(methodOn(VisaApplicationController.class)
                .getApplications(null, null, null, null,
                        null, null, null, null, null,
                        null, null, null, null))
                .withRel("applications"));

        return dto;
    }

}