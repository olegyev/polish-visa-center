package app.repos;

import app.domain.VisaDocumentsInfo;
import app.domain.enums.ClientOccupation;
import app.domain.enums.VisaType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisaDocumentsInfoRepo extends JpaRepository<VisaDocumentsInfo, Long>, JpaSpecificationExecutor<VisaDocumentsInfo> {

    List<VisaDocumentsInfo> findByVisaTypeAndOccupation(VisaType visaType, ClientOccupation occupation);

}