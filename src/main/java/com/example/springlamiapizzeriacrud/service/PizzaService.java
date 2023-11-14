package com.example.springlamiapizzeriacrud.service;

import com.example.springlamiapizzeriacrud.exceptions.PizzaNotFoundException;
import com.example.springlamiapizzeriacrud.exceptions.NameUniqueException;
import com.example.springlamiapizzeriacrud.model.Pizza;
import com.example.springlamiapizzeriacrud.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class PizzaService {

    // ATTRIBUTI

    @Autowired
    private PizzaRepository pizzaRepository;

    // METODI


    // Ottengo la lista di pizze presenti in database

    public List<Pizza> getPizzaList(Optional<String> search) {


        // Se il parametro di ricerca è presente filtro la lista delle pizze

        if (search.isPresent()) {
            return pizzaRepository.findByNameContainingIgnoreCase(search.get());
        } else  {

            // Altrimenti pizzaRepository recupera da database la lista di tutte le pizze

            return pizzaRepository.findAll();
        }

    }

    // Restituisce una pizza presa per id, se non la trova solleva un eccezione

    public Pizza getPizzaById(Integer id) throws PizzaNotFoundException {

        // Cerco la pizza per id nel caso in cui non sia presente lancio un eccezione PizzaNotFoundException

        return pizzaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id " + id + " not found"));

    }

    // Creazione di una nuova pizza

    public Pizza createPizza(Pizza pizza) {

        try {
            return pizzaRepository.save(pizza);
        } catch (RuntimeException e) {

            // Nome non univoco

            throw new NameUniqueException(pizza.getName());
        }
    }

    // Modificare una pizza per id

    public Pizza editPizza(Pizza pizza) throws PizzaNotFoundException{

        Pizza pizzaToEdit = getPizzaById(pizza.getId());


        // Se la trovo modifico solo gli attributi che erano campi del form

        pizzaToEdit.setName(pizza.getName());
        pizzaToEdit.setDescription(pizza.getDescription());
        pizzaToEdit.setPrice(pizza.getPrice());

        // Se non ci sono errori salvo la pizza

        Pizza savedPizza = pizzaRepository.save(pizzaToEdit);

        return pizzaRepository.save(savedPizza);
    }

    // Eliminare una pizza dal database

    public void deletePizza(Integer id) {
        pizzaRepository.deleteById(id);
    }
}
