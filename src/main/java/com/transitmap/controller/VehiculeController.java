package com.transitmap.controller;

import com.transitmap.dto.VehiculeDto;
import com.transitmap.repository.LigneRepository;
import com.transitmap.service.VehiculeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vehicules")
@RequiredArgsConstructor
public class VehiculeController{

    private final VehiculeService vehiculeService;
    private final LigneRepository ligneRepository;

    @GetMapping
    public String list(Model model){
        model.addAttribute("vehicules",vehiculeService.findAll());
        return "vehicules/list";
    }

    @GetMapping("/create")
    public String createForm(Model model){
        model.addAttribute("vehicule",new VehiculeDto());
        model.addAttribute("lignes",ligneRepository.findAll());
        return "vehicules/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute VehiculeDto dto,BindingResult result,Model model){
        if(result.hasErrors()){
            model.addAttribute("lignes",ligneRepository.findAll());
            return "vehicules/create";
        }
        vehiculeService.create(dto);
        return "redirect:/vehicules";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id,Model model){
        model.addAttribute("vehicule",vehiculeService.findById(id));
        model.addAttribute("lignes",ligneRepository.findAll());
        return "vehicules/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,@Valid @ModelAttribute VehiculeDto dto,BindingResult result,Model model){
        if(result.hasErrors()){
            model.addAttribute("lignes",ligneRepository.findAll());
            return "vehicules/edit";
        }
        vehiculeService.update(id,dto);
        return "redirect:/vehicules";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        vehiculeService.delete(id);
        return "redirect:/vehicules";
    }
}