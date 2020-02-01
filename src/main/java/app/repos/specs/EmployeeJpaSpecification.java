package app.repos.specs;

import app.domain.Employee;
import app.domain.enums.City;
import app.domain.enums.EmployeePosition;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class EmployeeJpaSpecification implements Specification<Employee> {

    private final Employee filter;

    public EmployeeJpaSpecification(final Employee filter) {
        super();
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        City city = filter.getCity();
        EmployeePosition position = filter.getPosition();
        String lastName = filter.getLastName();

        Predicate predicate = builder.disjunction();

        if (city != null && position == null && lastName == null) {
            predicate.getExpressions().add(builder.equal(root.get("city"), city));

        } else if (city == null && position != null && lastName == null) {
            predicate.getExpressions().add(builder.equal(root.get("position"), position));

        } else if (city == null && position == null && lastName != null) {
            predicate.getExpressions().add(builder.equal(root.get("lastName"), lastName));

        } else if (city != null && position != null && lastName == null) {
            predicate.getExpressions().add(builder.and(
                    builder.equal(root.get("city"), city),
                    builder.equal(root.get("position"), position)
            ));

        } else if (city != null && position == null) {
            predicate.getExpressions().add(builder.and(
                    builder.equal(root.get("city"), city),
                    builder.equal(root.get("lastName"), lastName)
            ));

        } else if (city == null && position != null) {
            predicate.getExpressions().add(builder.and(
                    builder.equal(root.get("position"), position),
                    builder.equal(root.get("lastName"), filter.getLastName())
            ));

        } else {
            predicate.getExpressions().add(builder.and(
                    builder.equal(root.get("city"), city),
                    builder.equal(root.get("position"), position),
                    builder.equal(root.get("lastName"), lastName)
            ));
        }

        return predicate;
    }

}