package app.dto.assemblers.impl;

import app.controllers.VisaController;
import app.domain.ClientVisa;
import app.dto.VisaDto;
import app.dto.assemblers.DtoAssemblerInterface;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class VisaDtoAssembler
        extends RepresentationModelAssemblerSupport<ClientVisa, VisaDto>
        implements DtoAssemblerInterface<ClientVisa, VisaDto> {

    public VisaDtoAssembler() {
        super(VisaController.class, VisaDto.class);
    }

    @Override
    public VisaDto toModel(ClientVisa visa) {
        VisaDto dto = instantiateModel(visa);

        dto.setVisaNumber(visa.getVisaNumber());
        dto.setVisaType(visa.getVisaType().toString());
        dto.setIssueDate(new SimpleDateFormat("yyyy-MM-dd").format(visa.getIssueDate()));
        dto.setExpiryDate(new SimpleDateFormat("yyyy-MM-dd").format(visa.getExpiryDate()));

        dto.add(linkTo(methodOn(VisaController.class)
                .getClientVisa(visa.getClient().getId(), visa.getId()))
                .withSelfRel());

        dto.add(linkTo(methodOn(VisaController.class)
                .getVisas(null, null, null, null,
                        null, null, null, null, null,
                        null, null, null))
                .withRel("visas"));

        return dto;
    }

}