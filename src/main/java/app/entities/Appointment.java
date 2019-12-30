package app.entities;

import app.entities.enums.City;

import java.util.Date;
import java.util.Objects;

public class Appointment extends Entity {
    private long id;
    private User client;
    private City city;
    private Date appointmentDate;
    private String appointmentTime;

    public Appointment() {
        super();
    }

    public Appointment(User client, City city, Date appointmentDate, String appointmentTime) {
        this.client = client;
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

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return getId() == that.getId() &&
                Objects.equals(getClient(), that.getClient()) &&
                Objects.equals(getCity(), that.getCity()) &&
                Objects.equals(getAppointmentDate(), that.getAppointmentDate()) &&
                Objects.equals(getAppointmentTime(), that.getAppointmentTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getClient(), getCity(), getAppointmentDate(), getAppointmentTime());
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", client=" + client +
                ", city='" + city + '\'' +
                ", appointmentDate=" + appointmentDate +
                ", appointmentTime=" + appointmentTime +
                '}';
    }
}