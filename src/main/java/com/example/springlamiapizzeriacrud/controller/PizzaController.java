package com.example.springlamiapizzeriacrud.controller;

import com.example.springlamiapizzeriacrud.PizzaService;
import com.example.springlamiapizzeriacrud.exceptions.NameUniqueException;
import com.example.springlamiapizzeriacrud.exceptions.PizzaNotFoundException;
import com.example.springlamiapizzeriacrud.model.Pizza;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/pizzas")
public class PizzaController {

    // ATTRIBUTI

    @Autowired
    private PizzaService pizzaService;

    @GetMapping
    public String index(@RequestParam Optional<String> search, Model model) {

        model.addAttribute("pizzaList", pizzaService.getPizzaList(search));

        return "pizzas/list";
    }

    @GetMapping("/show/{id}")
    public String show(@PathVariable Integer id, Model model) {

        try {
            Pizza pizza = pizzaService.getPizzaById(id);
            model.addAttribute("pizza", pizza);

            return "pizzas/show";
        } catch (PizzaNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // metodo che mostra il form di creazione della pizza
    @GetMapping("/create")
    public String create(Model model) {

        model.addAttribute("pizza", new Pizza());

        return "pizzas/form";
    }

    @PostMapping("/create")
    public String doCreate(@Valid @ModelAttribute("pizza") Pizza formPizza, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "pizzas/form";
        }

        Pizza savedPizza = null;

        try {
            savedPizza = pizzaService.createPizza(formPizza);

            return "redirect:/pizzas/show/" + savedPizza.getId();
        } catch (NameUniqueException e) {
            // aggiungo un errore di validazione per isbn
            bindingResult.addError(new FieldError("pizza", "name", e.getMessage(), false, null, null,
                    "Name must be unique"));

            // ti rimando alla pagina col form
            return "/pizzas/form";
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {

        try {
            // Proseguo a restituire la pagina di modifica

            model.addAttribute("pizza", pizzaService.getPizzaById(id));

            return "/pizzas/form";
        } catch (PizzaNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Metodo che riceve il submit del form di edit e salva la pizza

    @PostMapping("/edit/{id}")
    public String doEdit(@PathVariable Integer id, @ModelAttribute("pizza") Pizza formPizza, BindingResult bindingResult) {

        // Valido la pizza

        if(bindingResult.hasErrors()) {

            // Se ci sono errori ricarico il form

            return "/pizzas/form";

        }

        try {
            Pizza savedPizza = pizzaService.editPizza(formPizza);

            return "redirect:/pizzas/show/" + savedPizza.getId();
        } catch (PizzaNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Metodo per eliminare una pizza dal database

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {

        // Recupero la pizza tramite id

        Pizza pizzaToDelete = null;
        try {
            pizzaToDelete = pizzaService.getPizzaById(id);
            pizzaService.deletePizza(id);
            redirectAttributes.addFlashAttribute("message", "pizza '" + pizzaToDelete.getName() + "' deleted");

            return "redirect:/pizzas";
        } catch (PizzaNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
