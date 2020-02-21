package app.dto.assemblers.impl;

import app.controllers.ClientPageController;
import app.domain.Client;
import app.dto.ClientDto;
import app.dto.assemblers.DtoAssemblerInterface;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MyProfileDtoAssembler
        extends RepresentationModelAssemblerSupport<Client, ClientDto>
        implements DtoAssemblerInterface<Client, ClientDto> {

    public MyProfileDtoAssembler() {
        super(ClientPageController.class, ClientDto.class);
    }

    @Override
    public ClientDto toModel(Client client) {
        ClientDto dto = instantiateModel(client);

        dto.setFirstName(client.getFirstName());
        dto.setLastName(client.getLastName());
        dto.setEmail(client.getEmail());
        dto.setPhoneNumber(client.getPhoneNumber());
        dto.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").format(client.getDateOfBirth()));
        dto.setPassportId(client.getPassportId());
        dto.setOccupation(client.getOccupation().toString());

        dto.add(linkTo(methodOn(ClientPageController.class)
                .getLoggedClient(null))
                .withSelfRel());

        return dto;
    }

}