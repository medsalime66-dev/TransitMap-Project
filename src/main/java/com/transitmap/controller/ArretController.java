package com.transitmap.controller;

import com.transitmap.dto.ArretDto;
import com.transitmap.repository.LigneRepository;
import com.transitmap.service.ArretService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/arrets")
@RequiredArgsConstructor
public class ArretController{

    private final ArretService arretService;
    private final LigneRepository ligneRepository;

    @GetMapping
    public String list(Model model){
        model.addAttribute("arrets",arretService.findAll());
        return "arrets/list";
    }

    @GetMapping("/create")
    public String createForm(Model model){
        model.addAttribute("arret",new ArretDto());
        model.addAttribute("lignes",ligneRepository.findAll());
        return "arrets/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("arret") ArretDto dto,BindingResult result,Model model){
        if(result.hasErrors()){
            model.addAttribute("lignes",ligneRepository.findAll());
            return "arrets/create";
        }
        arretService.create(dto);
        return "redirect:/arrets";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id,Model model){
        model.addAttribute("arret",arretService.findById(id));
        model.addAttribute("lignes",ligneRepository.findAll());
        return "arrets/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,@Valid @ModelAttribute("arret") ArretDto dto,BindingResult result,Model model){
        if(result.hasErrors()){
            model.addAttribute("lignes",ligneRepository.findAll());
            return "arrets/edit";
        }
        arretService.update(id,dto);
        return "redirect:/arrets";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        arretService.delete(id);
        return "redirect:/arrets";
    }
}