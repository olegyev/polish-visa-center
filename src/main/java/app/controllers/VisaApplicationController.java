package app.controllers;

import app.domain.VisaApplication;
import app.domain.enums.City;
import app.domain.enums.VisaApplicationStatus;
import app.domain.enums.VisaType;
import app.dto.ClientVisaApplicationDto;
import app.dto.ClientVisaApplicationWithHistoryDto;
import app.dto.VisaApplicationDto;
import app.dto.assemblers.DtoAssemblerInterface;
import app.services.VisaApplicationServiceInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class VisaApplicationController {

    private final VisaApplicationServiceInterface visaApplicationService;
    private final DtoAssemblerInterface<VisaApplication, VisaApplicationDto> visaApplicationAssembler;
    private final DtoAssemblerInterface<VisaApplication, ClientVisaApplicationDto> clientApplicationAssembler;
    private final DtoAssemblerInterface<VisaApplication, ClientVisaApplicationWithHistoryDto> historyAssembler;
    private final PagedResourcesAssembler<VisaApplication> pagedResourcesAssembler;

    @Autowired
    public VisaApplicationController(final VisaApplicationServiceInterface visaApplicationService,
                                     final @Qualifier("visaApplicationDtoAssembler") DtoAssemblerInterface<VisaApplication, VisaApplicationDto> visaApplicationAssembler,
                                     final @Qualifier("clientVisaApplicationDtoAssembler") DtoAssemblerInterface<VisaApplication, ClientVisaApplicationDto> clientApplicationAssembler,
                                     final @Qualifier("clientVisaApplicationWithHistoryDtoAssembler") DtoAssemblerInterface<VisaApplication, ClientVisaApplicationWithHistoryDto> historyAssembler) {
        this.visaApplicationService = visaApplicationService;
        this.visaApplicationAssembler = visaApplicationAssembler;
        this.clientApplicationAssembler = clientApplicationAssembler;
        this.historyAssembler = historyAssembler;
        this.pagedResourcesAssembler = new PagedResourcesAssembler<>(null, null);
    }

    /* !!! Parameters 'requiredVisaType', 'appointmentCity', 'appointmentDate', 'visaApplicationStatus'
     * should be given in uppercase => exact search !!! */
    /* !!! Parameters 'size', 'page' and 'sort' are used only for JSON representation - by default (implicitly) they are available in Pageable interface !!! */
    @GetMapping("applications")
    @RolesAllowed({"ROLE_OPERATOR", "ROLE_MANAGER"})
    @Transactional
    public ResponseEntity<PagedModel<VisaApplicationDto>> getApplications(@RequestParam(required = false) VisaType requiredVisaType,
                                                                          @RequestParam(required = false) City appointmentCity,
                                                                          @RequestParam(required = false) String appointmentDate,
                                                                          @RequestParam(required = false) String appointmentTime,
                                                                          @RequestParam(required = false) VisaApplicationStatus visaApplicationStatus,
                                                                          @RequestParam(required = false) String lastName,
                                                                          @RequestParam(required = false) String passportId,
                                                                          @RequestParam(required = false) String email,
                                                                          @RequestParam(required = false) String phoneNumber,
                                                                          @RequestParam(required = false) Integer page,
                                                                          @RequestParam(required = false) Integer size,
                                                                          @RequestParam(required = false) String sort,
                                                                          @PageableDefault(sort = {"requiredVisaType"}, direction = Sort.Direction.ASC) Pageable defaultPageable) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Pageable pageable;
        if (page == null || size == null || sort == null) {
            pageable = defaultPageable;
        } else {
            pageable = PageRequest.of(page, size, defaultPageable.getSort());
        }

        Page<VisaApplication> visaApplications = visaApplicationService.readAll(userDetails,
                requiredVisaType, appointmentCity, appointmentDate, appointmentTime, visaApplicationStatus,
                lastName, passportId, email, phoneNumber, pageable
        );
        PagedModel<VisaApplicationDto> dto = pagedResourcesAssembler.toModel(visaApplications, visaApplicationAssembler);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("clients/{clientId}/applications/{applicationId}")
    @RolesAllowed({"ROLE_OPERATOR", "ROLE_MANAGER"})
    @Transactional
    public ResponseEntity<ClientVisaApplicationDto> getClientApplication(@PathVariable long clientId,
                                                                         @PathVariable long applicationId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        VisaApplication visaApplication = visaApplicationService.readByClientIdAndApplicationId(userDetails, clientId, applicationId);
        ClientVisaApplicationDto dto = clientApplicationAssembler.toModel(visaApplication);
        return ResponseEntity.ok(dto);
    }

    /* !!! If status == BOOKED and application is in future operator can update all the fields !!!
     * !!! If status == DOCS_RECEIVED, PENDING, CONFIRMED or DENIED operator can update only status
     *  - other filled fields will be ignored and remain the same as in database !!!
     * !!! If status == DID_NOT_COME or ISSUED visa application is archived and cannot be updated !!!
     * !!! If status == DOCS_INCOMPLETE and visa application's appointment date is not gone only its status can be updated,
     * otherwise it is archived and cannot be updated !!!
     * !!! Status DID_NOT_COME is set automatically everyday at 23:59 to the visa applications, which status remained as BOOKED
     * till that time of today (see class app.services.util.VisitChecker, is called from app.Runner) !!!
     */
    @PutMapping("clients/{clientId}/applications/{applicationId}")
    @RolesAllowed("ROLE_OPERATOR")
    @Transactional
    public ResponseEntity<ClientVisaApplicationDto> updateClientApplication(@PathVariable long clientId,
                                                                            @PathVariable long applicationId,
                                                                            @Valid @RequestBody VisaApplication newVisaApplication) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        VisaApplication updatedVisaApplication = visaApplicationService.update(userDetails, clientId, applicationId, newVisaApplication);
        ClientVisaApplicationDto dto = clientApplicationAssembler.toModel(updatedVisaApplication);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @DeleteMapping("clients/{clientId}/applications/{applicationId}")
    @RolesAllowed("ROLE_OPERATOR")
    @Transactional
    public ResponseEntity<HttpStatus> deleteClientApplication(@PathVariable long clientId,
                                                              @PathVariable long applicationId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        visaApplicationService.delete(userDetails, clientId, applicationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("clients/{clientId}/applications/{applicationId}/history")
    @RolesAllowed({"ROLE_OPERATOR", "ROLE_MANAGER"})
    @Transactional
    public ResponseEntity<ClientVisaApplicationWithHistoryDto> getClientApplicationHistory(@PathVariable long clientId,
                                                                                           @PathVariable long applicationId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ClientVisaApplicationWithHistoryDto dto = historyAssembler.toModel(
                visaApplicationService.readByClientIdAndApplicationId(
                        userDetails, clientId, applicationId
                ));
        return ResponseEntity.ok(dto);
    }

}