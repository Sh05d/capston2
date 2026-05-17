package org.example.capston2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Check(constraints = "gender='FEMALE' or gender='MALE'")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(30) not null")
    @NotEmpty(message = "username can't be empty")
    private String username;

    @Column(columnDefinition = "varchar(12) not null unique")
    @NotEmpty(message = "Phone Number can't be empty")
    @Pattern(regexp = "^9665\\d{8}$", message = "Number should be valid Saudi number start with 966")
    private String phoneNumber;

    @Column(columnDefinition = "varchar(35) not null unique")
    @NotEmpty(message = "Email can't be empty")
    private String email;

    @Column(columnDefinition = "varchar(30) not null")
    @NotEmpty(message = "Password can't be empty")
    @Size(min = 9, max = 30, message = "password should be more than 8 characters and less than or equal 30 characters")
    private String password;

    @Column(columnDefinition = "varchar(6) not null")
    @NotEmpty(message = "Gender can't be empty")
    private String gender;

    @Column(columnDefinition = "decimal(8,2) not null")
    private Double balance;

}
