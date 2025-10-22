package com.biblioteca.services;

import com.biblioteca.domain.Libro;
import com.biblioteca.repository.LibroRepository;
import java.io.IOException;
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

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    @Transactional(readOnly = true)
    public List<Libro> getLibros(boolean activo) {
        if (activo) {
            return libroRepository.findByActivoTrue();
        }
        return libroRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Libro> getLibro(Integer idLibro) {
        return libroRepository.findById(idLibro);
    }

    @Transactional
    public void save(Libro libro, MultipartFile imagenFile) {
        libro = libroRepository.save(libro);
        if (!imagenFile.isEmpty()) {
            try {
                String rutaImagen = firebaseStorageService.uploadImage(
                        imagenFile, "libro",
                        libro.getIdLibro());
                libro.setRutaImagen(rutaImagen);
                libroRepository.save(libro);
            } catch (IOException e) {
                // Manejo silencioso del error
            }
        }
    }

    @Transactional
    public void delete(Integer idLibro) {
        if (!libroRepository.existsById(idLibro)) {
            throw new IllegalArgumentException("El libro con ID " + idLibro + " no existe.");
        }
        try {
