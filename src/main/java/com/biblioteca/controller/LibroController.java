package com.biblioteca;

import com.biblioteca.domain.Libro;
import com.biblioteca.services.CategoriaService;
import com.biblioteca.services.LibroService;
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
@RequestMapping("/libro")
public class LibroController {

    @Autowired
    private LibroService libroService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/listado")
    public String listado(Model model) {
        var libros = libroService.getLibros(false);
        model.addAttribute("libros", libros);
        model.addAttribute("totalLibros", libros.size());
        var categorias = categoriaService.getCategorias(true);
        model.addAttribute("categorias", categorias);
        return "/libro/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Libro libro, @RequestParam MultipartFile imagenFile, RedirectAttributes redirectAttributes) {
        libroService.save(libro, imagenFile);
        redirectAttributes.addFlashAttribute("todoOk", messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault()));
        return "redirect:/libro/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idLibro, RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String detalle = "mensaje.eliminado";
        try {
            libroService.delete(idLibro);
        } catch (IllegalArgumentException e) {
            titulo = "error";
            detalle = "categoria.error01";
        } catch (IllegalStateException e) {
            titulo = "error";
            detalle = "categoria.error02";
        } catch (Exception e) {
            titulo = "error";
            detalle = "categoria.error03";
        }
        redirectAttributes.addFlashAttribute(titulo, messageSource.getMessage(detalle, null, Locale.getDefault()));
        return "redirect:/libro/listado";
    }

    @GetMapping("/modificar/{idLibro}")
    public String modificar(@PathVariable("idLibro") Integer idLibro, Model model, RedirectAttributes redirectAttributes) {
        Optional<Libro> libroOpt = libroService.getLibro(idLibro);
        if (libroOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error