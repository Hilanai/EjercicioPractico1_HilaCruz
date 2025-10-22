package com.biblioteca.repository;

import com.biblioteca.domain.Genero;
import com.biblioteca.domain.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneroRepository extends JpaRepository<Libro, Integer> {

    public void save(Genero genero);

}
