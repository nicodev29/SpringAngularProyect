package com.company.inventory.model;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Data
@Table(name = "category")
public class Category implements Serializable {

    @Serial
    private static final long serialVersionUID = -4232222492375316210L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

}
