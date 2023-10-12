package com.company.inventory.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "product")
public class Product {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
