package org.example.capston2.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Check(constraints = "status='CONFIRM' or status='HANDOVER' or status='RETURNED' or status='CANCEL'")
public class ToolRental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "User id can't be empty")
    private Integer userId;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "ToolKit id can't be empty")
    private Integer toolKitId;

    @Column(columnDefinition = "datetime not null")
    @NotNull(message = "Start date can't be null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "Rent days can't be null")
    @Positive
    private Integer rentDays;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "Quantity can't be null")
    @Positive
    private Integer quantity;

    @Column(columnDefinition = "datetime not null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    @Column(columnDefinition = "varchar(20) not null default 'CONFIRM'")
    private String status;

    @Column(columnDefinition = "decimal(10,2) not null")
    private Double totalPrice;

    @Column(columnDefinition = "boolean")
    private Boolean insuranceRefunded;
}
