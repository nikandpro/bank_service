package github.nikandpro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "\"user\"")
@EqualsAndHashCode(exclude = {"account", "emails", "phones"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 500)
    private String name;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "password", nullable = false, length = 500)
    private String password;

    @OneToOne(mappedBy = "user")
    @ToString.Exclude
    private Account account;

    @OneToMany(mappedBy = "user")
    private Set<EmailData> emails = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<PhoneData> phones = new HashSet<>();

}
