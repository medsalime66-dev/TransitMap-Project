package com.transitmap.controller;

import com.transitmap.dto.TrajetDto;
import com.transitmap.repository.LigneRepository;
import com.transitmap.service.TrajetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/trajets")
@RequiredArgsConstructor
public class TrajetController{

    private final TrajetService trajetService;
    private final LigneRepository ligneRepository;

    @GetMapping
    public String list(Model model){
        model.addAttribute("trajets",trajetService.findAll());
        return "trajets/list";
    }

    @GetMapping("/create")
    public String createForm(Model model){
        model.addAttribute("trajet",new TrajetDto());
        model.addAttribute("lignes",ligneRepository.findAll());
        return "trajets/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute TrajetDto dto,BindingResult result,Model model){
        if(result.hasErrors()){
            model.addAttribute("lignes",ligneRepository.findAll());
            return "trajets/create";
        }
        trajetService.create(dto);
        return "redirect:/trajets";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id,Model model){
        model.addAttribute("trajet",trajetService.findById(id));
        model.addAttribute("lignes",ligneRepository.findAll());
        return "trajets/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,@Valid @ModelAttribute TrajetDto dto,BindingResult result,Model model){
        if(result.hasErrors()){
            model.addAttribute("lignes",ligneRepository.findAll());
            return "trajets/edit";
        }
        trajetService.update(id,dto);
        return "redirect:/trajets";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        trajetService.delete(id);
        return "redirect:/trajets";
    }
}