package app.dto.assemblers.impl;

import app.controllers.VisaController;
import app.domain.Client;
import app.domain.ClientVisa;
import app.domain.VisaApplication;
import app.dto.ClientVisaDto;
import app.dto.VisaDto;
import app.dto.assemblers.DtoAssemblerInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class ClientVisaDtoAssembler
        extends RepresentationModelAssemblerSupport<ClientVisa, ClientVisaDto>
        implements DtoAssemblerInterface<ClientVisa, ClientVisaDto> {

    private final DtoAssemblerInterface<ClientVisa, VisaDto> visaDtoAssembler;

    @Autowired
    public ClientVisaDtoAssembler(final @Qualifier("visaDtoAssembler") DtoAssemblerInterface<ClientVisa, VisaDto> visaDtoAssembler) {
        super(VisaController.class, ClientVisaDto.class);
        this.visaDtoAssembler = visaDtoAssembler;
    }

    @Override
    public ClientVisaDto toModel(ClientVisa visa) {
        ClientVisaDto dto = instantiateModel(visa);
        Client client = visa.getClient();

        dto.setFirstName(client.getFirstName());
        dto.setLastName(client.getLastName());
        dto.setEmail(client.getEmail());
        dto.setPhoneNumber(client.getPhoneNumber());
        dto.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").format(client.getDateOfBirth()));
        dto.setPassportId(client.getPassportId());
        dto.setOccupation(client.getOccupation().toString());
        dto.setVisa(visaDtoAssembler.toModel(visa));

        return dto;
    }

}