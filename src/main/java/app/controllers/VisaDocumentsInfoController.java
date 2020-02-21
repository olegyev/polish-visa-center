package app.controllers;

import app.domain.VisaDocumentsInfo;
import app.domain.enums.ClientOccupation;
import app.domain.enums.VisaType;
import app.dto.VisaDocumentsInfoDto;
import app.dto.assemblers.DtoAssemblerInterface;
import app.services.VisaDocumentsInfoServiceInterface;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RestController
@RequestMapping("documents-info")
@RolesAllowed("ROLE_DIRECTOR")
public class VisaDocumentsInfoController {

    private final VisaDocumentsInfoServiceInterface docsInfoService;
    private final DtoAssemblerInterface<VisaDocumentsInfo, VisaDocumentsInfoDto> assembler;
    private final PagedResourcesAssembler<VisaDocumentsInfo> pagedResourcesAssembler;

    @Autowired
    public VisaDocumentsInfoController(final VisaDocumentsInfoServiceInterface docsInfoService,
                                       final DtoAssemblerInterface<VisaDocumentsInfo, VisaDocumentsInfoDto> assembler) {
        this.docsInfoService = docsInfoService;
        this.assembler = assembler;
        this.pagedResourcesAssembler = new PagedResourcesAssembler<>(null,null);
    }

    /* !!! Parameters 'visaType' and 'occupation' should be given in uppercase => exact search !!! */
    /* !!! Parameters 'size', 'page' and 'sort' are used only for JSON representation - by default (implicitly) they are available in Pageable interface !!! */
    @GetMapping
    @Transactional
    public ResponseEntity<PagedModel<VisaDocumentsInfoDto>> getVisaDocumentsInfo(
            @RequestParam(required = false) VisaType visaType,
            @RequestParam(required = false) ClientOccupation occupation,
            @RequestParam(required = false) String docDescription,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort,
            @PageableDefault(sort = {"visaType"}, direction = Sort.Direction.ASC) Pageable defaultPageable) {
        Pageable pageable;
        if (page == null || size == null || sort == null) {
            pageable = defaultPageable;
        } else {
            pageable = PageRequest.of(page, size, defaultPageable.getSort());
        }

        Page<VisaDocumentsInfo> docsInfo = docsInfoService.readAll(visaType, occupation, docDescription, pageable);
        PagedModel<VisaDocumentsInfoDto> dto = pagedResourcesAssembler.toModel(docsInfo, assembler);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("{id}")
    @Transactional
    public ResponseEntity<VisaDocumentsInfoDto> getVisaDocumentsInfoById(@PathVariable long id) {
        VisaDocumentsInfo docsInfo = docsInfoService.readById(id);
        VisaDocumentsInfoDto dto = assembler.toModel(docsInfo);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<VisaDocumentsInfoDto> addVisaDocumentsInfo(@Valid @RequestBody VisaDocumentsInfo docsInfo) {
        VisaDocumentsInfo createdDocsInfo = docsInfoService.create(docsInfo);
        VisaDocumentsInfoDto dto = assembler.toModel(createdDocsInfo);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @Transactional
    public ResponseEntity<VisaDocumentsInfoDto> updateVisaDocumentsInfo(@PathVariable long id, @Valid @RequestBody VisaDocumentsInfo newDocsInfo) {
        VisaDocumentsInfo updatedDocsInfo = docsInfoService.update(id, newDocsInfo);
        VisaDocumentsInfoDto dto = assembler.toModel(updatedDocsInfo);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity<HttpStatus> deleteVisaDocumentsInfo(@PathVariable long id) {
        docsInfoService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}