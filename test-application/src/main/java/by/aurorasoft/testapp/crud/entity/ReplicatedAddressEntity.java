package by.aurorasoft.testapp.crud.entity;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
@Entity
@Table(name = "replicated_addresses")
public class ReplicatedAddressEntity extends AbstractEntity<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;
}

