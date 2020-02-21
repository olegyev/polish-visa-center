package app.repos.specs;

import app.domain.Client;
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

public final class VisaApplicationJpaSpecification {

    public static Specification<VisaApplication> requiredVisaTypeContains(VisaType requiredVisaType) {
        return (root, query, builder) -> builder.equal(root.get("requiredVisaType"), contains(requiredVisaType));
    }

    public static Specification<VisaApplication> appointmentCityContains(City appointmentCity) {
        return (root, query, builder) -> builder.equal(root.get("city"), contains(appointmentCity));
    }

    public static Specification<VisaApplication> appointmentDateContains(String appointmentDate) {
        return (root, query, builder) -> builder.equal(root.get("appointmentDate"), containsDate(appointmentDate));
    }

    public static Specification<VisaApplication> appointmentTimeContains(String appointmentTime) {
        return (root, query, builder) -> builder.like(root.get("appointmentTime"), contains(appointmentTime));
    }

    public static Specification<VisaApplication> visaApplicationStatusContains(VisaApplicationStatus visaApplicationStatus) {
        return (root, query, builder) -> builder.equal(root.get("visaApplicationStatus"), contains(visaApplicationStatus));
    }

    public static Specification<VisaApplication> lastNameContains(String lastName) {
        return (root, query, builder) -> builder.like(root.<VisaApplication, Client>join("client").get("lastName"), contains(lastName));
    }

    public static Specification<VisaApplication> passportIdContains(String passportId) {
        return (root, query, builder) -> builder.like(root.<VisaApplication, Client>join("client").get("passportId"), contains(passportId));
    }

    public static Specification<VisaApplication> emailContains(String email) {
        return (root, query, builder) -> builder.like(root.<VisaApplication, Client>join("client").get("email"), contains(email));
    }

    public static Specification<VisaApplication> phoneNumberContains(String phoneNumber) {
        return (root, query, builder) -> builder.like(root.<VisaApplication, Client>join("client").get("phoneNumber"), contains(phoneNumber));
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

    private static Date containsDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(MessageFormat.format("{0}", date));
        } catch (ParseException e) {
            throw new BadRequestException("Date is incorrect.");
        }
    }

}