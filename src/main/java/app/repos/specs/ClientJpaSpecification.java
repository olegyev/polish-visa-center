package app.repos.specs;

import app.domain.Client;

import org.springframework.data.jpa.domain.Specification;

import java.text.MessageFormat;

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

    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }

}