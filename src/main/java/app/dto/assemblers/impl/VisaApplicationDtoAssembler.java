package app.dto.assemblers.impl;

import app.controllers.VisaApplicationController;
import app.domain.VisaApplication;
import app.dto.VisaApplicationDto;
import app.dto.assemblers.DtoAssemblerInterface;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class VisaApplicationDtoAssembler
        extends RepresentationModelAssemblerSupport<VisaApplication, VisaApplicationDto>
        implements DtoAssemblerInterface<VisaApplication, VisaApplicationDto> {

    public VisaApplicationDtoAssembler() {
        super(VisaApplicationController.class, VisaApplicationDto.class);
    }

    @Override
    public VisaApplicationDto toModel(VisaApplication visaApplication) {
        VisaApplicationDto dto = instantiateModel(visaApplication);

        dto.setRequiredVisaType(visaApplication.getRequiredVisaType().toString());
        dto.setCity(visaApplication.getCity().toString());
        dto.setAppointmentDate(new SimpleDateFormat("yyyy-MM-dd").format(visaApplication.getAppointmentDate()));
        dto.setAppointmentTime(visaApplication.getAppointmentTime());
        dto.setVisaApplicationStatus(visaApplication.getVisaApplicationStatus().toString());

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