package org.example.capston2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Check(constraints = "rate>=0 and rate<=5")
public class Studio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(30) not null")
    @NotEmpty(message = "Name can't be empty")
    private String name;

    @Column(columnDefinition = "varchar(40) not null unique")
    @NotEmpty(message = "Email can't be empty")
    @Email
    private String email;

    @Column(columnDefinition = "varchar(25) not null")
    @NotEmpty(message = "City can't be empty")
    private String city;

    @Column(columnDefinition = "varchar(40) not null")
    @NotEmpty(message = "location can't be empty")
    private String location;

    @Column(columnDefinition = "decimal(3,2) not null")
    private Double rate;

    @Column(columnDefinition = "int not null")
    private Integer numberOfRating;

}
