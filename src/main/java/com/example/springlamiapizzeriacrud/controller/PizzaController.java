package com.example.springlamiapizzeriacrud.controller;

import com.example.springlamiapizzeriacrud.model.Pizza;
import com.example.springlamiapizzeriacrud.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/pizzas")
public class PizzaController {

    // ATTRIBUTI

    @Autowired
    private PizzaRepository pizzaRepository;

    @GetMapping
    public String index(@RequestParam Optional<String> search, Model model) {

        List<Pizza> pizzaList;

        // Se il parametro di ricerca è presente filtro la lista dei libri

        if (search.isPresent()) {
            pizzaList = pizzaRepository.findByNameContainingIgnoreCase(search.get());
        } else  {

            // bookRepository recupera da database la lista di tutti i libri

            pizzaList = pizzaRepository.findAll();
        }

            model.addAttribute("pizzaList", pizzaList);

            return "pizzas/list";
    }

    @GetMapping("/show/{id}")
    public String show(@PathVariable Integer id, Model model) {


        Pizza pizza = pizzaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id " + id + " not found"));
        model.addAttribute("pizza",pizza);

        return "pizzas/show";

    }
}
