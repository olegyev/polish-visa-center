package app.controllers;

import app.domain.Client;
import app.domain.VisaApplication;
import app.domain.VisaDocumentsInfo;
import app.domain.enums.City;
import app.domain.enums.VisaApplicationStatus;
import app.dto.*;
import app.dto.assemblers.DtoAssemblerInterface;
import app.services.ClientServiceInterface;
import app.services.VisaApplicationServiceInterface;
import app.services.VisaDocumentsInfoServiceInterface;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RolesAllowed("ROLE_CLIENT")
public class ClientPageController {

    private final static Logger log = LogManager.getLogger(ClientPageController.class);

    private final ClientServiceInterface clientService;
    private final VisaApplicationServiceInterface visaApplicationService;
    private final VisaDocumentsInfoServiceInterface docsInfoService;
    private final DtoAssemblerInterface<Client, ClientDto> clientAssembler;
    private final DtoAssemblerInterface<VisaApplication, VisaApplicationDto> visaApplicationAssembler;
    private final DtoAssemblerInterface<VisaApplication, VisaApplicationWithDocInfoDto> visaApplicationWithDocsAssembler;

    @Autowired
    public ClientPageController(final ClientServiceInterface clientService,
                                final VisaApplicationServiceInterface visaApplicationService,
                                final VisaDocumentsInfoServiceInterface docsInfoService,
                                final @Qualifier("myProfileDtoAssembler") DtoAssemblerInterface<Client, ClientDto> clientAssembler,
                                final @Qualifier("myVisaApplicationDtoAssembler") DtoAssemblerInterface<VisaApplication, VisaApplicationDto> visaApplicationAssembler,
                                final @Qualifier("myVisaApplicationWithDocInfoDtoAssembler") DtoAssemblerInterface<VisaApplication, VisaApplicationWithDocInfoDto> visaApplicationWithDocsAssembler) {
        this.clientService = clientService;
        this.visaApplicationService = visaApplicationService;
        this.docsInfoService = docsInfoService;
        this.clientAssembler = clientAssembler;
        this.visaApplicationAssembler = visaApplicationAssembler;
        this.visaApplicationWithDocsAssembler = visaApplicationWithDocsAssembler;
    }

    @GetMapping("my-profile")
    @Transactional
    public ResponseEntity<ClientDto> getLoggedClient(Principal principal) {
        User loggedUserInfo = (User) ((Authentication) principal).getPrincipal();
        Client loggedClient = clientService.readByEmail(loggedUserInfo.getUsername());
        ClientDto dto = clientAssembler.toModel(loggedClient);
        return ResponseEntity.ok(dto);
    }

    /* !!! Update all fields except password and agreement !!! */
    @PutMapping("my-profile")
    @Transactional
    public ResponseEntity<ClientDto> updateLoggedClient(@Valid @RequestBody ClientDtoRequest newClient, Principal principal) {
        User loggedUserInfo = (User) ((Authentication) principal).getPrincipal();
        Client loggedClient = clientService.readByEmail(loggedUserInfo.getUsername());
        Client updatedClient = clientService.update(loggedClient.getId(), newClient);
        ClientDto dto = clientAssembler.toModel(updatedClient);

        log.info("Client with ID = {} updated him-/herself.", loggedClient.getId());

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("my-visa-application")
    @Transactional
    public ResponseEntity<?> getLoggedClientVisaApplication(Principal principal) {
        User loggedUserInfo = (User) ((Authentication) principal).getPrincipal();
        Client loggedClient = clientService.readByEmail(loggedUserInfo.getUsername());
        VisaApplication lastVisaApplication = visaApplicationService.readLastVisaApplicationByClient(loggedClient);
        VisaApplicationStatus status = null;

        if (lastVisaApplication != null) {
            status = lastVisaApplication.getVisaApplicationStatus();
        }

        VisaApplicationDto dto;

        if (status == VisaApplicationStatus.DOCS_RECEIVED
                || status == VisaApplicationStatus.PENDING
                || status == VisaApplicationStatus.CONFIRMED
                || status == VisaApplicationStatus.DENIED) {
            /* Just last visa application with its status. */

            dto = visaApplicationAssembler.toModel(lastVisaApplication);
            return ResponseEntity.ok(dto);

        } else {
            int workDayBegin = 9;
            int workDayEnd = 17;
            int stepInMinutes = 15;

            List<DisabledTimeAndDatesDto> disabledTimeAndDates = new ArrayList<>();

            DisabledTimeAndDatesDto inMinsk = visaApplicationService.findTimeAndDatesToDisable(City.MINSK, workDayBegin, workDayEnd, stepInMinutes);
            DisabledTimeAndDatesDto inGomel = visaApplicationService.findTimeAndDatesToDisable(City.GOMEL, workDayBegin, workDayEnd, stepInMinutes);
            DisabledTimeAndDatesDto inMogilev = visaApplicationService.findTimeAndDatesToDisable(City.MOGILEV, workDayBegin, workDayEnd, stepInMinutes);
            DisabledTimeAndDatesDto inBrest = visaApplicationService.findTimeAndDatesToDisable(City.BREST, workDayBegin, workDayEnd, stepInMinutes);
            DisabledTimeAndDatesDto inGrodno = visaApplicationService.findTimeAndDatesToDisable(City.GRODNO, workDayBegin, workDayEnd, stepInMinutes);

            disabledTimeAndDates.add(inMinsk);
            disabledTimeAndDates.add(inGomel);
            disabledTimeAndDates.add(inMogilev);
            disabledTimeAndDates.add(inBrest);
            disabledTimeAndDates.add(inGrodno);

            if (status == VisaApplicationStatus.BOOKED) {
                /* Last visa application + documents list + disabled dates and time for update last booked visa application. */

                VisaApplicationWithDocInfoDto dtoWithDocs = visaApplicationWithDocsAssembler.toModel(lastVisaApplication);

                List<VisaDocumentsInfo> requiredDocs = docsInfoService.readByVisaTypeAndAndOccupation(
                        lastVisaApplication.getRequiredVisaType(), loggedClient.getOccupation());

                List<String> docsDescriptions = requiredDocs.stream()
                        .map(VisaDocumentsInfo::getDocDescription)
                        .collect(Collectors.toList());

                dtoWithDocs.setRequiredDocs(docsDescriptions);

                List<Object> dtoWithDocsAndDisabledTimeAndDates = new ArrayList<>();

                dtoWithDocsAndDisabledTimeAndDates.add(dtoWithDocs);
                dtoWithDocsAndDisabledTimeAndDates.add(disabledTimeAndDates);

                return ResponseEntity.ok(dtoWithDocsAndDisabledTimeAndDates);

            } else {
                /* Disabled dates and time for add new visa application. */

                return ResponseEntity.ok(disabledTimeAndDates);
            }
        }
    }

    @PostMapping("my-visa-application")
    @Transactional
    public ResponseEntity<VisaApplicationDto> addLoggedClientVisaApplication(
            @Valid @RequestBody VisaApplicationDtoRequest visaApplication, Principal principal) {
        User loggedUserInfo = (User) ((Authentication) principal).getPrincipal();
        Client loggedClient = clientService.readByEmail(loggedUserInfo.getUsername());
        VisaApplication createdVisaApplication = visaApplicationService.create(visaApplication, loggedClient);
        VisaApplicationDto dto = visaApplicationAssembler.toModel(createdVisaApplication);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping("my-visa-application")
    @Transactional
    public ResponseEntity<VisaApplicationDto> updateLoggedClientVisaApplication(
            @Valid @RequestBody VisaApplicationDtoRequest newVisaApplication, Principal principal) {
        User loggedUserInfo = (User) ((Authentication) principal).getPrincipal();
        Client loggedClient = clientService.readByEmail(loggedUserInfo.getUsername());
        VisaApplication updatedVisaApplication = visaApplicationService.update(newVisaApplication, loggedClient);
        VisaApplicationDto dto = visaApplicationAssembler.toModel(updatedVisaApplication);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @DeleteMapping("my-visa-application")
    @Transactional
    public ResponseEntity<HttpStatus> deleteLoggedClientVisaApplication(Principal principal) {
        User loggedUserInfo = (User) ((Authentication) principal).getPrincipal();
        Client loggedClient = clientService.readByEmail(loggedUserInfo.getUsername());
        visaApplicationService.delete(loggedClient);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}