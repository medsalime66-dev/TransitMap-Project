package com.transitmap.controller;

import com.transitmap.dto.LigneDto;

import com.transitmap.service.LigneService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;

@Controller

@RequestMapping("/lignes")

@RequiredArgsConstructor
public class LigneController {

    private final LigneService ligneService;

    @GetMapping
    public String listLignes(

            @RequestParam(
                    defaultValue = ""
            )
            String keyword,

            @RequestParam(
                    defaultValue = "0"
            )
            int page,

            @RequestParam(
                    defaultValue = "5"
            )
            int size,

            @RequestParam(
                    defaultValue = "nom"
            )
            String sortBy,

            Model model
    ) {

        Page<LigneDto> lignes =
                ligneService.search(
                        keyword,
                        page,
                        size,
                        sortBy
                );

        model.addAttribute(
                "lignes",
                lignes
        );

        model.addAttribute(
                "keyword",
                keyword
        );

        model.addAttribute(
                "sortBy",
                sortBy
        );

        return "lignes/list";
    }

    @GetMapping("/create")
    public String createForm(
            Model model
    ) {

        model.addAttribute(
                "ligne",
                new LigneDto()
        );

        return "lignes/create";
    }

    @PostMapping("/create")
    public String saveLigne(

            @Valid

            @ModelAttribute("ligne")
            LigneDto ligneDto,

            BindingResult result
    ) {

        if (result.hasErrors()) {

            return "lignes/create";
        }

        ligneService.create(
                ligneDto
        );

        return "redirect:/lignes";
    }

    @GetMapping("/edit/{id}")
    public String editForm(

            @PathVariable
            Long id,

            Model model
    ) {

        model.addAttribute(
                "ligne",
                ligneService.findById(id)
        );

        return "lignes/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateLigne(

            @PathVariable
            Long id,

            @Valid

            @ModelAttribute("ligne")
            LigneDto ligneDto,

            BindingResult result
    ) {

        if (result.hasErrors()) {

            return "lignes/edit";
        }

        ligneService.update(
                id,
                ligneDto
        );

        return "redirect:/lignes";
    }

    @GetMapping("/delete/{id}")
    public String deleteLigne(

            @PathVariable
            Long id
    ) {

        ligneService.delete(id);

        return "redirect:/lignes";
    }
}