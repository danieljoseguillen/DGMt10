package com.tarea10.dgmt10e03.Repositorios;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tarea10.dgmt10e03.domain.Empleado;
import com.tarea10.dgmt10e03.modelos.Genero;
import com.tarea10.dgmt10e03.modelos.Estado;


public interface EmpleadoRepository extends JpaRepository <Empleado, Long> {
    List<Empleado> findByEstado(Estado estado);
    List<Empleado> findByGenero(Genero genero);
    List<Empleado> findByNombreContainingIgnoreCase(String nombre);
    List<Empleado> findByGeneroAndNombreContainingIgnoreCase(Genero genero, String nombre);
}
