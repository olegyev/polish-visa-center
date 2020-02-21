package app.controllers;

import app.domain.ClientVisa;
import app.domain.enums.VisaType;
import app.dto.VisaDto;
import app.dto.assemblers.impl.VisaDtoAssembler;
import app.services.VisaServiceInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
public class VisaController {

    private final VisaServiceInterface visaService;
    private final VisaDtoAssembler visaDtoAssembler;
    private final PagedResourcesAssembler<ClientVisa> pagedResourcesAssembler;

    @Autowired
    public VisaController(final VisaServiceInterface visaService,
                          final VisaDtoAssembler visaDtoAssembler) {
        this.visaService = visaService;
        this.visaDtoAssembler = visaDtoAssembler;
        this.pagedResourcesAssembler = new PagedResourcesAssembler<>(null, null);
    }

    /* !!! Parameters 'issuedVisaType' should be given in uppercase => exact search !!! */
    /* !!! Parameters 'size', 'page' and 'sort' are used only for JSON representation - by default (implicitly) they are available in Pageable interface !!! */
    @GetMapping("visas")
    @RolesAllowed({"ROLE_OPERATOR", "ROLE_MANAGER", "ROLE_DIRECTOR"})
    @Transactional
    public ResponseEntity<PagedModel<VisaDto>> getVisas(@RequestParam(required = false) String visaNumber,
                                                        @RequestParam(required = false) VisaType issuedVisaType,
                                                        @RequestParam(required = false) String issueDate,
                                                        @RequestParam(required = false) String expiryDate,
                                                        @RequestParam(required = false) String lastName,
                                                        @RequestParam(required = false) String passportId,
                                                        @RequestParam(required = false) String email,
                                                        @RequestParam(required = false) String phoneNumber,
                                                        @RequestParam(required = false) Integer page,
                                                        @RequestParam(required = false) Integer size,
                                                        @RequestParam(required = false) String sort,
                                                        @PageableDefault(sort = {"expiryDate"}, direction = Sort.Direction.DESC) Pageable defaultPageable) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Pageable pageable;
        if (page == null || size == null || sort == null) {
            pageable = defaultPageable;
        } else {
            pageable = PageRequest.of(page, size, defaultPageable.getSort());
        }

        Page<ClientVisa> visas = visaService.readAll(userDetails,
                visaNumber, issuedVisaType, issueDate, expiryDate,
                lastName, passportId, email, phoneNumber, pageable
        );
        PagedModel<VisaDto> dto = pagedResourcesAssembler.toModel(visas, visaDtoAssembler);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("clients/{clientId}/visas/{visaId}")
    @RolesAllowed({"ROLE_OPERATOR", "ROLE_MANAGER", "ROLE_DIRECTOR"})
    @Transactional
    public ResponseEntity<VisaDto> getClientVisa(@PathVariable long clientId,
                                                 @PathVariable long visaId) {
        return null;
    }

}