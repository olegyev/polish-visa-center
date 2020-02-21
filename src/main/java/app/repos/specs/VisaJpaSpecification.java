package app.repos.specs;

import app.domain.Client;
import app.domain.ClientVisa;
import app.domain.enums.VisaType;
import app.exceptions.BadRequestException;

import org.springframework.data.jpa.domain.Specification;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class VisaJpaSpecification {

    public static Specification<ClientVisa> visaNumberContains(String visaNumber) {
        return (root, query, builder) -> builder.like(root.<Client, ClientVisa>join("visas").get("visaNumber"), contains(visaNumber));
    }

    public static Specification<ClientVisa> issuedVisaTypeContains(VisaType issuedVisaType) {
        return (root, query, builder) -> builder.equal(root.<Client, ClientVisa>join("visas").get("visaType"), contains(issuedVisaType));
    }

    public static Specification<ClientVisa> issueDateContains(String issueDate) {
        return (root, query, builder) -> builder.equal(root.<Client, ClientVisa>join("visas").get("issueDate"), containsDate(issueDate));
    }

    public static Specification<ClientVisa> expiryDateContains(String expiryDate) {
        return (root, query, builder) -> builder.equal(root.<Client, ClientVisa>join("visas").get("expiryDate"), containsDate(expiryDate));
    }

    public static Specification<ClientVisa> lastNameContains(String lastName) {
        return (root, query, builder) -> builder.like(root.get("lastName"), contains(lastName));
    }

    public static Specification<ClientVisa> passportIdContains(String passportId) {
        return (root, query, builder) -> builder.like(root.get("passportId"), contains(passportId));
    }

    public static Specification<ClientVisa> emailContains(String email) {
        return (root, query, builder) -> builder.like(root.get("email"), contains(email));
    }

    public static Specification<ClientVisa> phoneNumberContains(String phoneNumber) {
        return (root, query, builder) -> builder.like(root.get("phoneNumber"), contains(phoneNumber));
    }

    private static VisaType contains(VisaType visaType) {
        return VisaType.valueOf(MessageFormat.format("{0}", visaType));
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