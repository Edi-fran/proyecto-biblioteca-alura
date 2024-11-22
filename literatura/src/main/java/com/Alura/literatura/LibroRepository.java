package com.Alura.literatura;




import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findByTituloAndIdioma(String titulo, String idioma);
    List<Libro> findByIdioma(String idioma);
}
