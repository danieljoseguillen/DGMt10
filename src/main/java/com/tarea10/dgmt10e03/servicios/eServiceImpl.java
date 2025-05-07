package com.tarea10.dgmt10e03.servicios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tarea10.dgmt10e03.Repositorios.EmpleadoRepository;
import com.tarea10.dgmt10e03.domain.Empleado;
import com.tarea10.dgmt10e03.modelos.Estado;
import com.tarea10.dgmt10e03.modelos.Genero;

@Service
public class eServiceImpl implements eService {

    @Autowired
    EmpleadoRepository repositorio;

    public List<Empleado> listAll() {
        List<Empleado> listaActivos = new ArrayList<>();

        List<Empleado> empleados = repositorio.findAll();
        for (Empleado empleado : empleados) {
            if (empleado.getEstado().equals(Estado.ACTIVO)) {
                listaActivos.add(empleado);
            }
        }
        return listaActivos;

    }

    public List<Empleado> listSorted(Genero genero, String val) {
        if (genero == null) {
            return repositorio.findByNombreContainingIgnoreCase(val);
        } else if (val.isEmpty() || val == null) {
            return repositorio.findByGenero(genero);
        } else {
            return repositorio.findByGeneroAndNombreContainingIgnoreCase(genero, val);
        }
    }

    public List<Empleado> listGender(Genero genero) {
        return repositorio.findByGenero(genero);
    }

    public Empleado getById(long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontraron empleados con la ID indicada."));
    }

    public Empleado agregar(Empleado empleado) {
        try {
            return repositorio.save(empleado);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo agregar el empleado."+ e.getMessage());
        }

    }

    public Empleado modificar(Empleado empleado) {
        try {
            return repositorio.save(empleado);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo modificar el empleado.");
        }

    }

    public boolean borrarPorId(long id) {

        if (repositorio.findById(id).isPresent()) {
            repositorio.deleteById(id);
            return true;
        }

        throw new RuntimeException("No existe el empleado.");
    }

}
