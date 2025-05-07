package com.tarea10.dgmt10e03.servicios;

import java.util.List;

import com.tarea10.dgmt10e03.domain.Empleado;
import com.tarea10.dgmt10e03.modelos.Genero;

public interface eService {

    List<Empleado>listAll();
    List<Empleado>listSorted(Genero genero,String val);
    Empleado getById(long id);
    Empleado agregar(Empleado empleado);
    Empleado modificar(Empleado empleado);
    boolean borrarPorId(long id);
}
