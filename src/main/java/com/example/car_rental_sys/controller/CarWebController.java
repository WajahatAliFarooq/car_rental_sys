package com.example.car_rental_sys.controller;

import com.example.car_rental_sys.dto.CarDTO;
import com.example.car_rental_sys.service.CarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CarWebController {

    private final CarService carService;

    public CarWebController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public String listCars(Model model) {
        model.addAttribute("cars", carService.getAllCars());
        return "car-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("car", new CarDTO());
        return "car-form";
    }

    @PostMapping
    public String createCar(@ModelAttribute("car") CarDTO carDTO) {
        carService.createCar(carDTO);
        return "redirect:/";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("car", carService.getAllCars()
                .stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow());
        return "car-form";
    }

    @PostMapping("/{id}")
    public String updateCar(@PathVariable Long id,
                            @ModelAttribute("car") CarDTO carDTO) {
        carService.updateCar(id, carDTO);
        return "redirect:/";
    }

    @PostMapping("/{id}/delete")
    public String deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return "redirect:/";
    }
}

