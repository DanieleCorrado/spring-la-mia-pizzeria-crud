package com.example.springlamiapizzeriacrud.controller;

import com.example.springlamiapizzeriacrud.model.Pizza;
import com.example.springlamiapizzeriacrud.repository.PizzaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

    // metodo che mostra il form di creazione del book
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("pizza", new Pizza());
        return "pizzas/create";
    }

    @PostMapping("/create")
    public String doCreate(@Valid @ModelAttribute("pizza") Pizza formPizza, BindingResult bindingResult) {

        // validare che i dati siano corretti
        if (bindingResult.hasErrors()) {
            // ci sono errori, devo ricaricare il form
            return "pizzas/create";
        }
        // salvo la pizza su database
        Pizza savedPizza = pizzaRepository.save(formPizza);

        return "redirect:/pizzas/show/" + savedPizza.getId();
    }
}
