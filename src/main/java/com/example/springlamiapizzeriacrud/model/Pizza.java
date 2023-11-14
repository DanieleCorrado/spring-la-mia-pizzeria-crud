package com.example.springlamiapizzeriacrud.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pizzas")
public class Pizza {

    // ATTRIBUTI

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Name must not be blank")
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank(message = "Description must not be blank")
    @Lob
    private String description;

    @NotBlank(message = "Photo url must not be blank")
    @URL(message = "Insert a valid URL")
    private String photo;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be grater than 0.0")
    @Digits(integer=3, fraction=2, message = "Insert max 2 digits after .")
    @NotNull(message = "Price must not be blank")
    private BigDecimal price;

    @CreationTimestamp
    private LocalDateTime created_at;

    // GETTER E SETTER


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String text) {
        this.description = text;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
