package com.biblioteca.repository;

import com.biblioteca.domain.Genero;
import com.biblioteca.domain.Libro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Integer> {

    List<Libro> findByActivoTrue();

    List<Libro> findByGeneroIdGenero(Integer idGenero);

    List<Libro> findByTituloContainingIgnoreCase(String titulo);

    public void save(Genero libro);
}
