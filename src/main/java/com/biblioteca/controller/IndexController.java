package com.biblioteca.controller;


import com.biblioteca.services.LibroService;
import com.biblioteca.services.GeneroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    private final LibroService libroService;
    private final GeneroService generoService;

    public IndexController(LibroService libroService, GeneroService generoService) {
        this.libroService = libroService;
        this.generoService = generoService;
    }

    @GetMapping("/")
    public String cargarPaginaInicio(Model model) {
        var libros = libroService.getLibros(true);
        model.addAttribute("libros", libros);
        var generos = generoService.getLibros(true);
        model.addAttribute("generos", generos);
        return "index";
    }

    @GetMapping("/consultas/{idGenero}")
    public String listarGenero(@PathVariable("idGenero") Integer idGenero, Model model) {
        model.addAttribute("idGeneroActual", idGenero);
        var generoOptional = generoService.getGenero(idGenero);
        if (generoOptional.isEmpty()) {
            model.addAttribute("libros", java.util.Collections.emptyList());
        } else {
            var libros = libroService.getLibrosPorGenero(idGenero);
            model.addAttribute("libros", libros);
        }
        var generos = generoService.getLibros(true);
        model.addAttribute("generos", generos);
        return "index";
    }

    @GetMapping("/buscar")
    public String buscarPorTitulo(@RequestParam("titulo") String titulo, Model model) {
        var libros = libroService.buscarPorTitulo(titulo);
        model.addAttribute("libros", libros);
        var generos = generoService.getLibros(true);
        model.addAttribute("generos", generos);
        return "index";
    }
}
