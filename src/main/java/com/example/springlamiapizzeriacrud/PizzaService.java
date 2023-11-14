package com.example.springlamiapizzeriacrud;

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

//        Optional<Pizza> result = pizzaRepository.findById(id);
//
//        // Verifico se il risultato è presente
//
//        if(result.isPresent()) {
//
//            return result.get();
//
//        } else {
//
//            // Se non trovo il libro sollevo un eccezione
//
//            throw new PizzaNotFoundException("Pizza with id " + id + " not found");
//        }

       /* EQUIVALENTE
       */
        return pizzaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id " + id + " not found"));

    }

    // Creazione di un nuovo libro

    public Pizza createPizza(Pizza pizza) {

        try {
            return pizzaRepository.save(pizza);
        } catch (RuntimeException e) {

            // Nome non univoco

            throw new NameUniqueException(pizza.getName());
        }
    }

    // Modificare un libro con un id

    public Pizza editPizza(Pizza pizza) throws PizzaNotFoundException{

        Pizza pizzaToEdit = getPizzaById(pizza.getId());


        // Se lo trovo modifico solo gli attributi che erano campi del form

        pizzaToEdit.setName(pizza.getName());
        pizzaToEdit.setDescription(pizza.getDescription());
        pizzaToEdit.setPrice(pizza.getPrice());

        // Se non ci sono errori salvo il libro

        Pizza savedPizza = pizzaRepository.save(pizzaToEdit);

        return pizzaRepository.save(savedPizza);
    }

    // Eliminare un libro dal database
    public void deletePizza(Integer id) {
        pizzaRepository.deleteById(id);
    }
}
