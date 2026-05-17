package org.example.capston2.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Check(constraints = "category='PAINTING' or category='POTTERY' or category='CRAFT' or category='PHOTOGRAPHY'")
@Check(constraints = "audience_gender='MIX' or audience_gender='MALE' or audience_gender='FEMALE'")
@Check(constraints = "status='SCHEDULE' or status='COMPLETE' or status='CANCEL'")
public class Workshop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(30) not null")
    @NotEmpty(message = "title can't be empty")
    @Size(max = 30)
    private String title;

    @Column(columnDefinition = "varchar(200) not null")
    @NotEmpty(message = "description can't be empty")
    @Size(max = 200)
    private String description;

    @Column(columnDefinition = "varchar(20) not null")
    @NotEmpty(message = "category can't be empty")
    @Pattern(regexp = "(?i)^(Painting|Pottery|Craft|Photography)")
    private String category;

    @Column(columnDefinition = "datetime not null")
    @NotNull(message = "Start date can't be null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @Column(columnDefinition = "datetime not null")
    @NotNull(message = "End date can't be null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    @Column(columnDefinition = "varchar(20) not null")
    @NotEmpty(message = "Audience gender can't be empty")
    @Pattern(regexp = "(?i)^(Mix|Male|Female)")
    private String audienceGender;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "Capacity can't be null")
    @PositiveOrZero
    private Integer capacity;

    @Column(columnDefinition = "decimal(6,2) not null")
    @NotNull(message = "Price can't be null")
    @Positive
    private Double price;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "Studio id can't be empty")
    private Integer studioId;

    @Column(columnDefinition = "varchar(20) not null")
    private String status;
}
