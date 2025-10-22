package com.biblioteca.services;

import com.biblioteca.domain.Libro;
import com.biblioteca.repository.LibroRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    @Transactional(readOnly = true)
    public List<Libro> getLibros(boolean activo) {
        if (activo) {
            return libroRepository.findByActivoTrue();
        }
        return libroRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Libro> getLibrosPorGenero(Integer idGenero) {
        return libroRepository.findByGeneroIdGenero(idGenero);
    }

    @Transactional(readOnly = true)
    public List<Libro> buscarPorTitulo(String titulo) {
        return libroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    @Transactional(readOnly = true)
    public Optional<Libro> getLibro(Integer idLibro) {
        return libroRepository.findById(idLibro);
    }

    @Transactional
    public void save(Libro libro, MultipartFile imagenFile) {
        libroRepository.save(libro);
    }

    @Transactional
    public void delete(Integer idLibro) {
        if (!libroRepository.existsById(idLibro)) {
            throw new IllegalArgumentException("El libro con ID " + idLibro + " no existe.");
        }
        try {
            libroRepository.deleteById(idLibro);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar el libro. Tiene datos asociados.", e);
        }
    }
}
