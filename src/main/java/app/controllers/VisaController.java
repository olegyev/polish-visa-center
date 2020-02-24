package app.controllers;

import app.domain.ClientVisa;
import app.domain.enums.VisaType;
import app.dto.ClientVisaDto;
import app.dto.VisaDto;
import app.dto.assemblers.DtoAssemblerInterface;
import app.services.VisaServiceInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RestController
public class VisaController {

    private final VisaServiceInterface visaService;
    private final DtoAssemblerInterface<ClientVisa, VisaDto> visaDtoAssembler;
    private final DtoAssemblerInterface<ClientVisa, ClientVisaDto> clientVisaDtoAssembler;
    private final PagedResourcesAssembler<ClientVisa> pagedResourcesAssembler;

    @Autowired
    public VisaController(final VisaServiceInterface visaService,
                          final DtoAssemblerInterface<ClientVisa, VisaDto> visaDtoAssembler,
                          final DtoAssemblerInterface<ClientVisa, ClientVisaDto> clientVisaDtoAssembler) {
        this.visaService = visaService;
        this.visaDtoAssembler = visaDtoAssembler;
        this.clientVisaDtoAssembler = clientVisaDtoAssembler;
        this.pagedResourcesAssembler = new PagedResourcesAssembler<>(null, null);
    }

    /* !!! Parameter 'issuedVisaType' should be given in uppercase => exact search !!! */
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
    public ResponseEntity<ClientVisaDto> getClientVisa(@PathVariable long clientId,
                                                       @PathVariable long visaId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ClientVisa visa = visaService.readByClientIdAndApplicationId(userDetails, clientId, visaId);
        ClientVisaDto dto = clientVisaDtoAssembler.toModel(visa);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("clients/{clientId}/new-visa")
    @RolesAllowed("ROLE_OPERATOR")
    @Transactional
    public ResponseEntity<ClientVisaDto> addClientVisa(@Valid @RequestBody ClientVisa visa,
                                                       @PathVariable long clientId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ClientVisa createdVisa = visaService.create(userDetails, visa, clientId);
        ClientVisaDto dto = clientVisaDtoAssembler.toModel(createdVisa);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping("clients/{clientId}/visas/{visaId}")
    @RolesAllowed("ROLE_OPERATOR")
    @Transactional
    public ResponseEntity<ClientVisaDto> updateClientVisa(@PathVariable long clientId,
                                                          @PathVariable long visaId,
                                                          @Valid @RequestBody ClientVisa newVisa) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ClientVisa updatedVisa = visaService.update(userDetails, clientId, visaId, newVisa);
        ClientVisaDto dto = clientVisaDtoAssembler.toModel(updatedVisa);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @DeleteMapping("clients/{clientId}/visas/{visaId}")
    @RolesAllowed("ROLE_OPERATOR")
    @Transactional
    public ResponseEntity<HttpStatus> updateClientVisa(@PathVariable long clientId,
                                                       @PathVariable long visaId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        visaService.delete(userDetails, clientId, visaId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}