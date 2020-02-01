package app.dto;

import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

public abstract class AbstractDto extends RepresentationModel<AbstractDto> implements Serializable, Cloneable {
}