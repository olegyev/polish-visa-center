package app.entities;

import app.entities.enums.AdminPosition;
import app.entities.enums.City;

import java.util.Objects;

public class Admin extends User {
    private AdminPosition position;
    private City city;

    public Admin() {
        super();
    }

    public Admin(String firstName, String lastName, AdminPosition position, City city, String email, String phoneNumber, String password) {
        super(firstName, lastName, email, phoneNumber, password);
        this.position = position;
        this.city = city;
    }

    public AdminPosition getPosition() {
        return position;
    }

    public void setPosition(AdminPosition position) {
        this.position = position;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Admin that = (Admin) o;
        return getId() == that.getId() &&
                Objects.equals(getFirstName(), that.getFirstName()) &&
                Objects.equals(getLastName(), that.getLastName()) &&
                Objects.equals(getPosition(), that.getPosition()) &&
                Objects.equals(getCity(), that.getCity()) &&
                Objects.equals(getEmail(), that.getEmail()) &&
                Objects.equals(getPhoneNumber(), that.getPhoneNumber()) &&
                Objects.equals(getPassword(), that.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getLastName(), getPosition(), getCity(), getEmail(),
                getPhoneNumber(), getPassword());
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", position='" + position + '\'' +
                ", city='" + city + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}