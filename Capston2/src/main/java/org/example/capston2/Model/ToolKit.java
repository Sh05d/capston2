package org.example.capston2.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Check(constraints = "category='PAINTING' or category='POTTERY' or category='PHOTOGRAPHY'")
@Check(constraints = "pickup_method='PICKUP' or pickup_method='DELIVERY'")
public class ToolKit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(30) not null")
    @NotEmpty(message = "Name can't be empty")
    @Size(max = 30)
    private String name;

    @Column(columnDefinition = "varchar(200) not null")
    @NotEmpty(message = "Description can't be empty")
    @Size(max = 200)
    private String description;

    @Column(columnDefinition = "varchar(20) not null")
    @NotEmpty(message = "Category can't be empty")
    @Pattern(regexp = "(?i)^(Painting|Pottery|Photography)")
    private String category;

    @Column(columnDefinition = "decimal(8,2) not null")
    @NotNull(message = "Price per day can't be null")
    @Positive
    private Double pricePerDay;

    @Column(columnDefinition = "decimal(8,2) not null")
    @NotNull(message = "Insurance fee can't be null")
    @Positive
    private Double insuranceFeePrtItem;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "Quantity can't be null")
    @Positive
    private Integer quantity;

    @Column(columnDefinition = "varchar(15) not null")
    @NotEmpty(message = "Pickup method can't be empty")
    @Pattern(regexp = "(?i)^(Pickup|Delivery)")
    private String pickupMethod;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "Studio id can't be empty")
    private Integer studioId;

}
