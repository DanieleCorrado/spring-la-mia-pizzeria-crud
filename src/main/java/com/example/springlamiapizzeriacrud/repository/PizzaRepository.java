package com.example.springlamiapizzeriacrud.repository;

import com.example.springlamiapizzeriacrud.model.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PizzaRepository extends JpaRepository<Pizza, Integer> {

    List<Pizza> findByNameContainingIgnoreCase(String titleKeyword);
}
