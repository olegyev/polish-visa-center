package app.entities;

import java.util.Objects;

public class DocumentsInfo extends Entity {
    private long id;
    private long clientId;
    private char requiredVisaType;
    private String passportNum;
    private String visaApplicationForm;
    private boolean isPhotoAttached;
    private boolean isPassportCopyAttached;
    private boolean isPrevVisasCopyAttached;
    private String medInsurance;
    private String returnTicket;
    private String employCertificate;
    private String financialMeansConfirm;
    private String residencePermit;
    private String minorsDocs;
    private String tripAimProof;
    private String polishOriginCertificate;

    public DocumentsInfo() {
        super();
    }

    public DocumentsInfo(long id, long clientId, char requiredVisaType, String passportNum, String visaApplicationForm,
                     boolean isPhotoAttached, boolean isPassportCopyAttached, boolean isPrevVisasCopyAttached,
                     String medInsurance, String returnTicket, String employCertificate, String financialMeansConfirm,
                     String residencePermit, String minorsDocs, String tripAimProof, String polishOriginCertificate) {
        this.id = id;
        this.clientId = clientId;
        this.requiredVisaType = requiredVisaType;
        this.passportNum = passportNum;
        this.visaApplicationForm = visaApplicationForm;
        this.isPhotoAttached = isPhotoAttached;
        this.isPassportCopyAttached = isPassportCopyAttached;
        this.isPrevVisasCopyAttached = isPrevVisasCopyAttached;
        this.medInsurance = medInsurance;
        this.returnTicket = returnTicket;
        this.employCertificate = employCertificate;
        this.financialMeansConfirm = financialMeansConfirm;
        this.residencePermit = residencePermit;
        this.minorsDocs = minorsDocs;
        this.tripAimProof = tripAimProof;
        this.polishOriginCertificate = polishOriginCertificate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public char getRequiredVisaType() {
        return requiredVisaType;
    }

    public void setRequiredVisaType(char requiredVisaType) {
        this.requiredVisaType = requiredVisaType;
    }

    public String getPassportNum() {
        return passportNum;
    }

    public void setPassportNum(String passportNum) {
        this.passportNum = passportNum;
    }

    public String getVisaApplicationForm() {
        return visaApplicationForm;
    }

    public void setVisaApplicationForm(String visaApplicationForm) {
        this.visaApplicationForm = visaApplicationForm;
    }

    public boolean isPhotoAttached() {
        return isPhotoAttached;
    }

    public void setPhotoAttached(boolean photoAttached) {
        isPhotoAttached = photoAttached;
    }

    public boolean isPassportCopyAttached() {
        return isPassportCopyAttached;
    }

    public void setPassportCopyAttached(boolean passportCopyAttached) {
        isPassportCopyAttached = passportCopyAttached;
    }

    public boolean isPrevVisasCopyAttached() {
        return isPrevVisasCopyAttached;
    }

    public void setPrevVisasCopyAttached(boolean prevVisasCopyAttached) {
        isPrevVisasCopyAttached = prevVisasCopyAttached;
    }

    public String getMedInsurance() {
        return medInsurance;
    }

    public void setMedInsurance(String medInsurance) {
        this.medInsurance = medInsurance;
    }

    public String getReturnTicket() {
        return returnTicket;
    }

    public void setReturnTicket(String returnTicket) {
        this.returnTicket = returnTicket;
    }

    public String getEmployCertificate() {
        return employCertificate;
    }

    public void setEmployCertificate(String employCertificate) {
        this.employCertificate = employCertificate;
    }

    public String getFinancialMeansConfirm() {
        return financialMeansConfirm;
    }

    public void setFinancialMeansConfirm(String financialMeansConfirm) {
        this.financialMeansConfirm = financialMeansConfirm;
    }

    public String getResidencePermit() {
        return residencePermit;
    }

    public void setResidencePermit(String residencePermit) {
        this.residencePermit = residencePermit;
    }

    public String getMinorsDocs() {
        return minorsDocs;
    }

    public void setMinorsDocs(String minorsDocs) {
        this.minorsDocs = minorsDocs;
    }

    public String getTripAimProof() {
        return tripAimProof;
    }

    public void setTripAimProof(String tripAimProof) {
        this.tripAimProof = tripAimProof;
    }

    public String getPolishOriginCertificate() {
        return polishOriginCertificate;
    }

    public void setPolishOriginCertificate(String polishOriginCertificate) {
        this.polishOriginCertificate = polishOriginCertificate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentsInfo that = (DocumentsInfo) o;
        return getId() == that.getId() &&
                getClientId() == that.getClientId() &&
                getRequiredVisaType() == that.getRequiredVisaType() &&
                isPhotoAttached() == that.isPhotoAttached() &&
                isPassportCopyAttached() == that.isPassportCopyAttached() &&
                isPrevVisasCopyAttached() == that.isPrevVisasCopyAttached() &&
                Objects.equals(getPassportNum(), that.getPassportNum()) &&
                Objects.equals(getVisaApplicationForm(), that.getVisaApplicationForm()) &&
                Objects.equals(getMedInsurance(), that.getMedInsurance()) &&
                Objects.equals(getReturnTicket(), that.getReturnTicket()) &&
                Objects.equals(getEmployCertificate(), that.getEmployCertificate()) &&
                Objects.equals(getFinancialMeansConfirm(), that.getFinancialMeansConfirm()) &&
                Objects.equals(getResidencePermit(), that.getResidencePermit()) &&
                Objects.equals(getMinorsDocs(), that.getMinorsDocs()) &&
                Objects.equals(getTripAimProof(), that.getTripAimProof()) &&
                Objects.equals(getPolishOriginCertificate(), that.getPolishOriginCertificate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getClientId(), getRequiredVisaType(), getPassportNum(), getVisaApplicationForm(),
                isPhotoAttached(), isPassportCopyAttached(), isPrevVisasCopyAttached(), getMedInsurance(),
                getReturnTicket(), getEmployCertificate(), getFinancialMeansConfirm(), getResidencePermit(),
                getMinorsDocs(), getTripAimProof(), getPolishOriginCertificate());
    }

    @Override
    public String toString() {
        return "DocumentsInfo{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", requiredVisaType=" + requiredVisaType +
                ", passportNum='" + passportNum + '\'' +
                ", visaApplicationForm='" + visaApplicationForm + '\'' +
                ", isPhotoAttached=" + isPhotoAttached +
                ", isPassportCopyAttached=" + isPassportCopyAttached +
                ", isPrevVisasCopyAttached=" + isPrevVisasCopyAttached +
                ", medInsurance='" + medInsurance + '\'' +
                ", returnTicket='" + returnTicket + '\'' +
                ", employCertificate='" + employCertificate + '\'' +
                ", financialMeansConfirm='" + financialMeansConfirm + '\'' +
                ", residencePermit='" + residencePermit + '\'' +
                ", minorsDocs='" + minorsDocs + '\'' +
                ", tripAimProof='" + tripAimProof + '\'' +
                ", polishOriginCertificate='" + polishOriginCertificate + '\'' +
                '}';
    }
}