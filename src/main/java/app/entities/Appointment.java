package app.entities;

import java.util.Date;
import java.util.Objects;

public class Appointment extends Entity {
    private long id;
    private long clientId;
    private Date appointmentDate;
    private long appointmentTime;
    private String city;

    public Appointment() {
        super();
    }

    public Appointment(long id, long clientId, String city, Date appointmentDate, long appointmentTime) {
        this.id = id;
        this.clientId = clientId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.city = city;
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


    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public long getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(long appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return getId() == that.getId() &&
                getClientId() == that.getClientId() &&
                getAppointmentTime() == that.getAppointmentTime() &&
                Objects.equals(getAppointmentDate(), that.getAppointmentDate()) &&
                Objects.equals(getCity(), that.getCity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getClientId(), getAppointmentDate(), getAppointmentTime(),  getCity());
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", appointmentDate=" + appointmentDate +
                ", appointmentTime=" + appointmentTime +
                ", city='" + city + '\'' +
                '}';
    }
}