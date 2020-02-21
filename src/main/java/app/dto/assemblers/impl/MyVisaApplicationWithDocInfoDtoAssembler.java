package app.dto.assemblers.impl;

import app.controllers.ClientPageController;
import app.domain.VisaApplication;
import app.dto.VisaApplicationWithDocInfoDto;
import app.dto.assemblers.DtoAssemblerInterface;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MyVisaApplicationWithDocInfoDtoAssembler
        extends RepresentationModelAssemblerSupport<VisaApplication, VisaApplicationWithDocInfoDto>
        implements DtoAssemblerInterface<VisaApplication, VisaApplicationWithDocInfoDto> {

    public MyVisaApplicationWithDocInfoDtoAssembler() {
        super(ClientPageController.class, VisaApplicationWithDocInfoDto.class);
    }

    @Override
    public VisaApplicationWithDocInfoDto toModel(VisaApplication visaApplication) {
        VisaApplicationWithDocInfoDto dto = instantiateModel(visaApplication);

        dto.setRequiredVisaType(visaApplication.getRequiredVisaType().toString());
        dto.setCity(visaApplication.getCity().toString());
        dto.setAppointmentDate(new SimpleDateFormat("yyyy-MM-dd").format(visaApplication.getAppointmentDate()));
        dto.setAppointmentTime(visaApplication.getAppointmentTime());
        dto.setVisaApplicationStatus(visaApplication.getVisaApplicationStatus().toString());

        dto.add(linkTo(methodOn(ClientPageController.class)
                .getLoggedClientVisaApplication(null))
                .withSelfRel());

        return dto;
    }

}