package app.dto.assemblers.impl;

import app.controllers.VisaDocumentsInfoController;
import app.domain.VisaDocumentsInfo;
import app.dto.VisaDocumentsInfoDto;
import app.dto.assemblers.DtoAssemblerInterface;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class VisaDocumentsInfoDtoAssembler
        extends RepresentationModelAssemblerSupport<VisaDocumentsInfo, VisaDocumentsInfoDto>
        implements DtoAssemblerInterface<VisaDocumentsInfo, VisaDocumentsInfoDto> {

    public VisaDocumentsInfoDtoAssembler() {
        super(VisaDocumentsInfoController.class, VisaDocumentsInfoDto.class);
    }

    @Override
    public VisaDocumentsInfoDto toModel(VisaDocumentsInfo docsInfo) {
        VisaDocumentsInfoDto dto = createModelWithId(docsInfo.getId(), docsInfo);

        dto.setVisaType(docsInfo.getVisaType().toString());
        dto.setOccupation(docsInfo.getOccupation().toString());
        dto.setDocDescription(docsInfo.getDocDescription());

        dto.add(linkTo(methodOn(VisaDocumentsInfoController.class)
                .getVisaDocumentsInfo(null, null, null, null, null, null, null, null))
                .withRel("visaDocumentsInfo"));

        return dto;
    }

}