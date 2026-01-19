package com.example.car_rental_sys.controller;

import com.example.car_rental_sys.dto.RentalDTO;
import com.example.car_rental_sys.service.RentalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rentals")
public class RentalWebController {

    private final RentalService rentalService;

    public RentalWebController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    public String listRentals(Model model) {
        model.addAttribute("rentals", rentalService.getAllRentals());
        return "rental-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("rental", new RentalDTO());
        return "rental-form";
    }

    @PostMapping
    public String createRental(@ModelAttribute("rental") RentalDTO rentalDTO) {
        rentalService.createRental(rentalDTO);
        return "redirect:/rentals";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("rental",
                rentalService.getAllRentals()
                        .stream()
                        .filter(r -> r.getId().equals(id))
                        .findFirst()
                        .orElseThrow());
        return "rental-form";
    }

    @PostMapping("/{id}")
    public String updateRental(@PathVariable Long id,
                               @ModelAttribute("rental") RentalDTO rentalDTO) {
        rentalService.updateRental(id, rentalDTO);
        return "redirect:/rentals";
    }

    @PostMapping("/{id}/delete")
    public String deleteRental(@PathVariable Long id) {
        rentalService.deleteRental(id);
        return "redirect:/rentals";
    }
}

