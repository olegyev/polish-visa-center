package app.dto.assemblers.impl;

import app.controllers.VisaApplicationController;
import app.controllers.VisaController;
import app.domain.Client;
import app.domain.VisaApplication;
import app.dto.ClientVisaApplicationDto;
import app.dto.VisaApplicationDto;
import app.dto.assemblers.DtoAssemblerInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class ClientVisaApplicationDtoAssembler
        extends RepresentationModelAssemblerSupport<VisaApplication, ClientVisaApplicationDto>
        implements DtoAssemblerInterface<VisaApplication, ClientVisaApplicationDto> {

    private final DtoAssemblerInterface<VisaApplication, VisaApplicationDto> visaApplicationDtoAssembler;

    @Autowired
    public ClientVisaApplicationDtoAssembler(final @Qualifier("visaApplicationDtoAssembler") DtoAssemblerInterface<VisaApplication, VisaApplicationDto> visaApplicationDtoAssembler) {
        super(VisaApplicationController.class, ClientVisaApplicationDto.class);
        this.visaApplicationDtoAssembler = visaApplicationDtoAssembler;
    }

    @Override
    public ClientVisaApplicationDto toModel(VisaApplication visaApplication) {
        ClientVisaApplicationDto dto = instantiateModel(visaApplication);
        Client client = visaApplication.getClient();

        dto.setFirstName(client.getFirstName());
        dto.setLastName(client.getLastName());
        dto.setEmail(client.getEmail());
        dto.setPhoneNumber(client.getPhoneNumber());
        dto.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").format(client.getDateOfBirth()));
        dto.setPassportId(client.getPassportId());
        dto.setOccupation(client.getOccupation().toString());
        dto.setVisaApplication(visaApplicationDtoAssembler.toModel(visaApplication));

        return dto;
    }

}