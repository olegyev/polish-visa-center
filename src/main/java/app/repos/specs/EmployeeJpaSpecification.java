package app.repos.specs;

import app.domain.Employee;
import app.domain.enums.City;
import app.domain.enums.EmployeePosition;

import org.springframework.data.jpa.domain.Specification;

import java.text.MessageFormat;

public final class EmployeeJpaSpecification {

    public static Specification<Employee> cityContains(City city) {
        return (root, query, builder) -> builder.equal(root.get("city"), contains(city));
    }

    public static Specification<Employee> positionContains(EmployeePosition position) {
        return (root, query, builder) -> builder.equal(root.get("position"), contains(position));
    }

    public static Specification<Employee> lastNameContains(String lastName) {
        return (root, query, builder) -> builder.like(root.get("lastName"), contains(lastName));
    }

    private static City contains(City city) {
        return City.valueOf(MessageFormat.format("{0}", city));
    }

    private static EmployeePosition contains(EmployeePosition position) {
        return EmployeePosition.valueOf(MessageFormat.format("{0}", position));
    }

    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }

}