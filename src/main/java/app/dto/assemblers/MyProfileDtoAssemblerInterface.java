package app.dto.assemblers;

import app.dto.AbstractDto;

import org.springframework.hateoas.server.RepresentationModelAssembler;

public interface MyProfileDtoAssemblerInterface<Client, S extends AbstractDto> extends RepresentationModelAssembler<Client, S> {

    S toModel(Client client);

}