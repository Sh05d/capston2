package org.example.capston2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Check(constraints = "status='CONFIRM' or status='CANCEL'")
public class WorkshopBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "User id can't be empty")
    private Integer userId;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "Workshop id can't be empty")
    private Integer workshopId;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "Number of seat can't be null")
    @Positive
    private Integer numberOfSeats;

    @Column(columnDefinition = "decimal(8,2) not null")
    private Double totalPrice;

    @Column(columnDefinition = "varchar(20) not null default 'CONFIRM'")
    private String status;
}
