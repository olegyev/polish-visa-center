package app.dto.assemblers;

import app.domain.Entity;
import app.dto.AbstractDto;

import org.springframework.hateoas.server.RepresentationModelAssembler;

public interface DtoAssemblerInterface<T extends Entity, S extends AbstractDto> extends RepresentationModelAssembler<T, S> {

    S toModel(T t);

}