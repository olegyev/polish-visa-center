package app.services.impl;

import app.domain.VisaDocumentsInfo;
import app.domain.enums.ClientOccupation;
import app.domain.enums.VisaType;
import app.exceptions.BadRequestException;
import app.exceptions.NotFoundException;
import app.repos.VisaDocumentsInfoRepo;
import app.repos.specs.VisaDocumentsInfoJpaSpecification;
import app.services.VisaDocumentsInfoServiceInterface;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisaDocumentsInfoServiceImpl implements VisaDocumentsInfoServiceInterface {

    private final static Logger log = LogManager.getLogger(VisaDocumentsInfoServiceImpl.class);

    private final VisaDocumentsInfoRepo docsInfoRepo;

    @Autowired
    public VisaDocumentsInfoServiceImpl(final VisaDocumentsInfoRepo docsInfoRepo) {
        this.docsInfoRepo = docsInfoRepo;
    }

    @Override
    public VisaDocumentsInfo create(VisaDocumentsInfo visaDocumentsInfo) {
        if (!bodyIsOk(visaDocumentsInfo)) {
            log.error("Attempt to add new visa documents' information failed due to the incorrect form filling.");
            throw new BadRequestException("The form filled incorrectly.");
        }

        VisaDocumentsInfo createdVisaDocumentsInfo = docsInfoRepo.save(visaDocumentsInfo);
        log.info("New visa documents' information added to a database with ID = {}.", createdVisaDocumentsInfo.getId());
        return createdVisaDocumentsInfo;
    }

    @Override
    public Page<VisaDocumentsInfo> readAll(VisaType visaType, ClientOccupation occupation, String docDescription, Pageable pageable) {
        Specification<VisaDocumentsInfo> spec = Specification
                .where(visaType == null ? null : VisaDocumentsInfoJpaSpecification.visaTypeContains(visaType))
                .and(occupation == null ? null : VisaDocumentsInfoJpaSpecification.occupationContains(occupation))
                .and(docDescription == null ? null : VisaDocumentsInfoJpaSpecification.docDescriptionContains(docDescription));

        Page<VisaDocumentsInfo> docsInfo = readAll(spec, pageable);

        if (docsInfo.isEmpty()) {
            NotFoundException exception = new NotFoundException("Cannot find visa documents' information.");
            log.error(exception);
            throw exception;
        }

        return docsInfo;
    }

    @Override
    public Page<VisaDocumentsInfo> readAll(Specification<VisaDocumentsInfo> spec, Pageable pageable) {
        return docsInfoRepo.findAll(spec, pageable);
    }

    @Override
    public VisaDocumentsInfo readById(long id) {
        VisaDocumentsInfo docsInfo = docsInfoRepo.findById(id).orElse(null);

        if (docsInfo == null) {
            NotFoundException exception = new NotFoundException("Cannot find visa documents' information with ID = " + id + ".");
            log.error(exception);
            throw exception;
        }

        return docsInfo;
    }

    @Override
    public VisaDocumentsInfo update(long id, VisaDocumentsInfo newVisaDocumentsInfo) {
        if (!bodyIsOk(newVisaDocumentsInfo)) {
            log.error("Attempt to update visa documents' information with ID = {}  failed due to the incorrect form filling.", id);
            throw new BadRequestException("The form filled incorrectly.");
        }

        VisaDocumentsInfo visaDocumentsInfoFromDb = readById(id);
        BeanUtils.copyProperties(newVisaDocumentsInfo, visaDocumentsInfoFromDb, "id");

        log.info("Updated visa documents' information with ID = {}.", id);

        return docsInfoRepo.save(visaDocumentsInfoFromDb);
    }

    @Override
    public void delete(long id) {
        VisaDocumentsInfo docsInfo = readById(id);
        log.info("Deleted visa documents' information with ID = {}.", id);
        docsInfoRepo.delete(docsInfo);
    }

    @Override
    public List<VisaDocumentsInfo> readByVisaTypeAndAndOccupation(VisaType visaType, ClientOccupation occupation) {
        log.info("Found visa documents' information by visa type = {} and client's occupation = {}.", visaType, occupation);
        return docsInfoRepo.findByVisaTypeAndOccupation(visaType, occupation);
    }

    private boolean bodyIsOk(VisaDocumentsInfo body) {
        return body.getVisaType() != null
                && body.getOccupation() != null
                && body.getDocDescription() != null;
    }

}