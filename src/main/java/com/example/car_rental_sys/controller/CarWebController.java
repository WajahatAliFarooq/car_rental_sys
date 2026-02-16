package com.example.car_rental_sys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.car_rental_sys.dto.CarDTO;
import com.example.car_rental_sys.service.CarService;

@Controller
@RequestMapping("/")
public class CarWebController {

	private static final String REDIRECT_HOME = "redirect:/";

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

	    model.addAttribute("formAction", "/");
		return "car-form";
	}

	@PostMapping
	public String createCar(@ModelAttribute("car") CarDTO carDTO) {
		carService.createCar(carDTO);
		return REDIRECT_HOME;
	}

	@GetMapping("/{id}/edit")
	public String showEditForm(@PathVariable Long id, Model model) {

		CarDTO car = carService.getCarById(id);

		model.addAttribute("car", car);
		model.addAttribute("formAction", "/" + id);

		return "car-form";
	}

	@PostMapping("/{id}")
	public String updateCar(@PathVariable Long id, @ModelAttribute("car") CarDTO carDTO) {
		carService.updateCar(id, carDTO);
		return REDIRECT_HOME;
	}

	@PostMapping("/{id}/delete")
	public String deleteCar(@PathVariable Long id) {
		carService.deleteCar(id);
		return REDIRECT_HOME;
	}
}
