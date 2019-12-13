package app.entities;

import app.entities.enums.ClientOccupation;

import java.util.Date;
import java.util.Objects;

public class Client extends User {
    private Date dateOfBirth;
    private ClientOccupation occupation;
    private boolean personalDataProcAgreement;

    public Client() {
        super();
    }

    public Client(String firstName, String lastName, Date dateOfBirth, ClientOccupation occupation, String email,
                  String phoneNumber, String password, boolean personalDataProcAgreement) {
        super(firstName, lastName, email, phoneNumber, password);
        this.dateOfBirth = dateOfBirth;
        this.occupation = occupation;
        this.personalDataProcAgreement = personalDataProcAgreement;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public ClientOccupation getOccupation() {
        return occupation;
    }

    public void setOccupation(ClientOccupation occupation) {
        this.occupation = occupation;
    }

    public boolean getPersonalDataProcAgreement() {
        return personalDataProcAgreement;
    }

    public void setPersonalDataProcAgreement(boolean personalDataProcAgreement) {
        this.personalDataProcAgreement = personalDataProcAgreement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client that = (Client) o;
        return getId() == that.getId() &&
                getPersonalDataProcAgreement() == that.getPersonalDataProcAgreement() &&
                Objects.equals(getFirstName(), that.getFirstName()) &&
                Objects.equals(getLastName(), that.getLastName()) &&
                Objects.equals(getDateOfBirth(), that.getDateOfBirth()) &&
                Objects.equals(getOccupation(), that.getOccupation()) &&
                Objects.equals(getEmail(), that.getEmail()) &&
                Objects.equals(getPhoneNumber(), that.getPhoneNumber()) &&
                Objects.equals(getPassword(), that.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getLastName(), getDateOfBirth(), getOccupation(), getEmail(),
                getPhoneNumber(), getPassword(), getPersonalDataProcAgreement());
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", occupation='" + occupation + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                ", personalDataProcAgreement=" + personalDataProcAgreement +
                '}';
    }
}