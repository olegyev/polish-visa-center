package app.dto.assemblers.impl;

import app.controllers.ClientController;
import app.domain.Client;
import app.dto.ClientDtoResponse;
import app.dto.assemblers.DtoAssemblerInterface;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ClientDtoAssembler
        extends RepresentationModelAssemblerSupport<Client, ClientDtoResponse>
        implements DtoAssemblerInterface<Client, ClientDtoResponse> {

    public ClientDtoAssembler() {
        super(ClientController.class, ClientDtoResponse.class);
    }

    @Override
    public ClientDtoResponse toModel(Client client) {
        ClientDtoResponse dto = createModelWithId(client.getId(), client);

        dto.setFirstName(client.getFirstName());
        dto.setLastName(client.getLastName());
        dto.setEmail(client.getEmail());
        dto.setPhoneNumber(client.getPhoneNumber());
        dto.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").format(client.getDateOfBirth()));
        dto.setPassportId(client.getPassportId());
        dto.setOccupation(client.getOccupation().toString());

        dto.add(linkTo(methodOn(ClientController.class)
                .getClients(null, null, null, null, null,
                        null, null, null, null,
                        null, null, null, null,
                        null, null, null, null, null))
                .withRel("clients"));

        return dto;
    }

}