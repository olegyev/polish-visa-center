package app.repos.specs;

import app.domain.Client;
import app.domain.ClientVisa;
import app.domain.VisaApplication;
import app.domain.enums.City;
import app.domain.enums.VisaApplicationStatus;
import app.domain.enums.VisaType;
import app.exceptions.BadRequestException;

import org.springframework.data.jpa.domain.Specification;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class ClientJpaSpecification {

    public static Specification<Client> lastNameContains(String lastName) {
        return (root, query, builder) -> builder.like(root.get("lastName"), contains(lastName));
    }

    public static Specification<Client> passportIdContains(String passportId) {
        return (root, query, builder) -> builder.like(root.get("passportId"), contains(passportId));
    }

    public static Specification<Client> emailContains(String email) {
        return (root, query, builder) -> builder.like(root.get("email"), contains(email));
    }

    public static Specification<Client> phoneNumberContains(String phoneNumber) {
        return (root, query, builder) -> builder.like(root.get("phoneNumber"), contains(phoneNumber));
    }

    public static Specification<Client> requiredVisaTypeContains(VisaType requiredVisaType) {
        return (root, query, builder) -> builder.equal(root.<Client, VisaApplication>join("applications").get("requiredVisaType"), contains(requiredVisaType));
    }

    public static Specification<Client> appointmentCityContains(City appointmentCity) {
        return (root, query, builder) -> builder.equal(root.<Client, VisaApplication>join("applications").get("city"), contains(appointmentCity));
    }

    public static Specification<Client> appointmentDateContains(String appointmentDate) {
        return (root, query, builder) -> builder.equal(root.<Client, VisaApplication>join("applications").get("appointmentDate"), containsDate(appointmentDate));
    }

    public static Specification<Client> appointmentTimeContains(String appointmentTime) {
        return (root, query, builder) -> builder.like(root.<Client, VisaApplication>join("applications").get("appointmentTime"), contains(appointmentTime));
    }

    public static Specification<Client> visaApplicationStatusContains(VisaApplicationStatus visaApplicationStatus) {
        return (root, query, builder) -> builder.equal(root.<Client, VisaApplication>join("applications").get("visaApplicationStatus"), contains(visaApplicationStatus));
    }

    public static Specification<Client> visaNumberContains(String visaNumber) {
        return (root, query, builder) -> builder.like(root.<Client, ClientVisa>join("visas").get("visaNumber"), contains(visaNumber));
    }

    public static Specification<Client> issuedVisaTypeContains(VisaType issuedVisaType) {
        return (root, query, builder) -> builder.equal(root.<Client, ClientVisa>join("visas").get("visaType"), contains(issuedVisaType));
    }

    public static Specification<Client> issueDateContains(String issueDate) {
        return (root, query, builder) -> builder.equal(root.<Client, ClientVisa>join("visas").get("issueDate"), containsDate(issueDate));
    }

    public static Specification<Client> expiryDateContains(String expiryDate) {
        return (root, query, builder) -> builder.equal(root.<Client, ClientVisa>join("visas").get("expiryDate"), containsDate(expiryDate));
    }

    private static VisaType contains(VisaType visaType) {
        return VisaType.valueOf(MessageFormat.format("{0}", visaType));
    }

    private static City contains(City appointmentCity) {
        return City.valueOf(MessageFormat.format("{0}", appointmentCity));
    }

    private static VisaApplicationStatus contains(VisaApplicationStatus visaApplicationStatus) {
        return VisaApplicationStatus.valueOf(MessageFormat.format("{0}", visaApplicationStatus));
    }

    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }

    private static Date containsDate(String expression) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(MessageFormat.format("{0}", expression));
        } catch (ParseException e) {
            throw new BadRequestException("Date is incorrect.");
        }
    }

}