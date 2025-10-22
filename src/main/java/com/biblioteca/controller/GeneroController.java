package com.biblioteca.controller;

import com.biblioteca.domain.Genero;
import com.biblioteca.domain.Libro;
import com.biblioteca.services.GeneroService;
import jakarta.validation.Valid;
import java.util.Locale;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/genero")
public class GeneroController {

    @Autowired
    private GeneroService generoService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/listado")
    public String listado(Model model) {
        var generos = generoService.getLibros(false);
        model.addAttribute("generos", generos);
        model.addAttribute("totalGeneros", generos.size());
        return "/genero/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Genero genero, @RequestParam MultipartFile imagenFile, RedirectAttributes redirectAttributes) {
        Libro libro = null;
        generoService.save(libro, imagenFile);
        redirectAttributes.addFlashAttribute("todoOk", messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault()));
        return "redirect:/genero/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idGenero, RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String detalle = "mensaje.eliminado";
        try {
            generoService.delete(idGenero);
        } catch (IllegalArgumentException e) {
            titulo = "error";
            detalle = "genero.error01";
        } catch (IllegalStateException e) {
            titulo = "error";
            detalle = "genero.error02";
        } catch (Exception e) {
            titulo = "error";
            detalle = "genero.error03";
        }
        redirectAttributes.addFlashAttribute(titulo, messageSource.getMessage(detalle, null, Locale.getDefault()));
        return "redirect:/genero/listado";
    }

    @GetMapping("/modificar/{idGenero}")
    public String modificar(@PathVariable("idGenero") Integer idGenero, Model model, RedirectAttributes redirectAttributes) {
        Optional<Genero> generoOpt = generoService.getGenero(idGenero);
        if (generoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("genero.error01", null, Locale.getDefault()));
            return "redirect:/genero/listado";
        }
        model.addAttribute("genero", generoOpt.get());
        return "/genero/modifica";
    }
}
