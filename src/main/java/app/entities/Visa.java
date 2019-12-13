package app.entities;

import app.entities.enums.VisaType;

import java.util.Date;
import java.util.Objects;

public class Visa extends Entity {
    private long id;
    private long clientId;
    private String visaNum;
    private VisaType visaType;
    private Date issueDate;
    private Date expiryDate;

    public Visa() {
    }

    public Visa(long id, long clientId, String visaNum, VisaType visaType, Date issueDate, Date expiryDate) {
        this.id = id;
        this.clientId = clientId;
        this.visaNum = visaNum;
        this.visaType = visaType;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
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

    public String getVisaNum() {
        return visaNum;
    }

    public void setVisaNum(String visaNum) {
        this.visaNum = visaNum;
    }

    public VisaType getVisaType() {
        return visaType;
    }

    public void setVisaType(VisaType visaType) {
        this.visaType = visaType;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Visa that = (Visa) o;
        return getId() == that.getId() &&
                getClientId() == that.getClientId() &&
                getVisaType() == that.getVisaType() &&
                Objects.equals(getVisaNum(), that.getVisaNum()) &&
                Objects.equals(getIssueDate(), that.getIssueDate()) &&
                Objects.equals(getExpiryDate(), that.getExpiryDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getClientId(), getVisaNum(), getVisaType(), getIssueDate(), getExpiryDate());
    }

    @Override
    public String toString() {
        return "Visa{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", visaNum='" + visaNum + '\'' +
                ", visaType=" + visaType +
                ", issueDate=" + issueDate +
                ", expiryDate=" + expiryDate +
                '}';
    }
}