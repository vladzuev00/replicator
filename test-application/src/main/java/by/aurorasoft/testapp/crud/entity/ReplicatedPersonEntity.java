package by.aurorasoft.testapp.crud.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import static jakarta.persistence.FetchType.LAZY;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
@Entity
@Table(name = "replicated_persons")
public class ReplicatedPersonEntity extends AbstractEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "address_id")
    @ToString.Exclude
    private ReplicatedAddressEntity address;
}
