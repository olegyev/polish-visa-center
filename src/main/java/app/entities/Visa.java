package app.entities;

import app.entities.enums.VisaType;

import java.util.Date;
import java.util.Objects;

public class Visa extends Entity {
    private long id;
    private User client;
    private String visaNum;
    private VisaType visaType;
    private Date issueDate;
    private Date expiryDate;

    public Visa() {
        super();
    }

    public Visa(User client, String visaNum, VisaType visaType, Date issueDate, Date expiryDate) {
        this.client = client;
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

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
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
                getVisaType() == that.getVisaType() &&
                Objects.equals(getClient(), that.getClient()) &&
                Objects.equals(getVisaNum(), that.getVisaNum()) &&
                Objects.equals(getIssueDate(), that.getIssueDate()) &&
                Objects.equals(getExpiryDate(), that.getExpiryDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getClient(), getVisaNum(), getVisaType(), getIssueDate(), getExpiryDate());
    }

    @Override
    public String toString() {
        return "Visa{" +
                "id=" + id +
                ", client=" + client +
                ", visaNum='" + visaNum + '\'' +
                ", visaType=" + visaType +
                ", issueDate=" + issueDate +
                ", expiryDate=" + expiryDate +
                '}';
    }
}