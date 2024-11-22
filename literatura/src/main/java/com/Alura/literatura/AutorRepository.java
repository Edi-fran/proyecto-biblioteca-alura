package com.Alura.literatura;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    // Método para buscar un autor por nombre
    Optional<Autor> findByNombre(String nombre);

    // Método para buscar autores que están vivos o que fallecieron después de un año específico
    List<Autor> findByAnioFallecimientoIsNullOrAnioFallecimientoGreaterThan(int anio);
}
