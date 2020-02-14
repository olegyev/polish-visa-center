package app.services;

import app.domain.VisaDocumentsInfo;
import app.domain.enums.ClientOccupation;
import app.domain.enums.VisaType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VisaDocumentsInfoServiceInterface extends CrudServiceInterface<VisaDocumentsInfo> {

    Page<VisaDocumentsInfo> readAll(VisaType visaType, ClientOccupation occupation, String docDescription, Pageable pageable);

    List<VisaDocumentsInfo> readByVisaTypeAndAndOccupation(VisaType visaType, ClientOccupation occupation);

}