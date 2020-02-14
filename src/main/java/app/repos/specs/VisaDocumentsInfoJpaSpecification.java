package app.repos.specs;

import app.domain.VisaDocumentsInfo;
import app.domain.enums.ClientOccupation;
import app.domain.enums.VisaType;

import org.springframework.data.jpa.domain.Specification;

import java.text.MessageFormat;

public final class VisaDocumentsInfoJpaSpecification {

    public static Specification<VisaDocumentsInfo> visaTypeContains(VisaType visaType) {
        return (root, query, builder) -> builder.equal(root.get("visaType"), contains(visaType));
    }

    public static Specification<VisaDocumentsInfo> occupationContains(ClientOccupation occupation) {
        return (root, query, builder) -> builder.equal(root.get("occupation"), contains(occupation));
    }

    public static Specification<VisaDocumentsInfo> docDescriptionContains(String docDescription) {
        return (root, query, builder) -> builder.like(builder.upper(root.get("docDescription")), contains(docDescription));
    }

    private static VisaType contains(VisaType visaType) {
        return VisaType.valueOf(MessageFormat.format("{0}", visaType));
    }

    private static ClientOccupation contains(ClientOccupation occupation) {
        return ClientOccupation.valueOf(MessageFormat.format("{0}", occupation));
    }

    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression).toUpperCase();
    }

}