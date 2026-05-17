package org.example.capston2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class StudioFollow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "User id can't be empty")
    private Integer userId;

    @Column(columnDefinition = "int not null")
    @NotNull(message = "Studio id can't be empty")
    private Integer studioId;

    @Column(columnDefinition = "boolean")
    private Boolean notify;
}
