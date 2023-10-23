package com.company.inventory.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Data
@Table(name = "product")
public class Product implements Serializable {


    @Serial
    private static final long serialVersionUID = -2193656506534590294L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Category category;


    @Basic(fetch = FetchType.LAZY)
    @Column(name = "image" , columnDefinition = "LONGBLOB")
    private byte[] image;

}

