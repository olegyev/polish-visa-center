package app.entities;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
public abstract class User extends Entity {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    @NonNull
    @Column(name = "first_name")
    protected String firstName;

    @NonNull
    @Column(name = "last_name")
    protected String lastName;

    @NonNull
    protected String email;

    @NonNull
    @Column(name = "phone_number")
    protected String phoneNumber;

    @NonNull
    protected String password;
}