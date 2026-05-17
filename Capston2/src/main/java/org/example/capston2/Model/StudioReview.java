package org.example.capston2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Check(constraints = "rate>=0 and rate<=5")
public class StudioReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(200) not null")
    @NotEmpty(message = "Content can't be empty")
    private String content;

    @Column(columnDefinition = "decimal(3,2) not null")
    @NotNull(message = "Rate can't be empty")
    @Min(1)
    @Max(5)
    private Double rate;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "User id can't be empty")
    private Integer userId;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "Studio id can't be empty")
    private Integer studioId;


}
