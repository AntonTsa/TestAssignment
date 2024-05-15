package ua.anton.tsa.testassignment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

import static jakarta.persistence.GenerationType.SEQUENCE;

/**
 * Main Host domain biz object
 */
@Getter
@Setter
@ToString
@Table(name = "usr")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    @Id
    @SequenceGenerator(
            name = "user_id_sequence",
            sequenceName = "user_id_sequence"
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "user_id_sequence"
    )
    private Long id;
    @NotBlank
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotNull
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;

}
