package com.tarea10.dgmt10e03.servicios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.tarea10.dgmt10e03.Repositorios.EmpleadoRepository;
import com.tarea10.dgmt10e03.domain.Empleado;
import com.tarea10.dgmt10e03.modelos.Estado;
import com.tarea10.dgmt10e03.modelos.Genero;

import jakarta.validation.ConstraintViolationException;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class eServiceTest {
    @InjectMocks
    private eServiceImpl eServ;

    @Mock
    private EmpleadoRepository repositorio;

    ArrayList<Empleado> mockList;
    ArrayList<Empleado> mockSorted;
    Empleado empleadoSinId, empleadoConId, empleadoModificado;

    @BeforeAll
    public void init() {
        mockList = new ArrayList<>();
        mockList.add(new Empleado(1l, "Jose", "Jose@gmail.com", 30000d, Estado.ACTIVO, Genero.MASCULINO));
        mockList.add(new Empleado(2l, "Pedro", "pedrog@gmail.com", 32000d, Estado.ACTIVO, Genero.MASCULINO));
        mockList.add(new Empleado(3l, "Maria", "mcarolina@gmail.com", 35000d, Estado.INACTIVO, Genero.FEMENINO));
        empleadoSinId = new Empleado(null, "Sandra", "Sandra@mail.com", 35000d, Estado.ACTIVO, Genero.FEMENINO);
        empleadoConId = new Empleado(4l, "Sandra", "sandra@mail.com", 35000d, Estado.ACTIVO, Genero.FEMENINO);
        empleadoModificado = new Empleado(4l, "Sandra", "sandra2@mail.com", 35000d, Estado.ACTIVO, Genero.FEMENINO);
        mockSorted = new ArrayList<>();
        mockSorted.add(mockList.get(0));
    }

    @Test
    public void listAllTest() {
        when(repositorio.findAll()).thenReturn(mockList);
        List<Empleado> empList = eServ.listAll();
        assertEquals(2, empList.size());
        assertEquals(Estado.ACTIVO, empList.get(0).getEstado());
        assertEquals(Estado.ACTIVO, empList.get(1).getEstado());
        verify(repositorio, times(1)).findAll();
    }

    @Test
    public void listSortedTest() {
        when(repositorio.findByGeneroAndNombreContainingIgnoreCase(Genero.MASCULINO, "Jo")).thenReturn(mockSorted);
        List<Empleado> empList = eServ.listSorted(Genero.MASCULINO, "Jo");
        assertEquals(1, empList.size());
        assertEquals("Jose", empList.get(0).getNombre());
        assertEquals(1l, empList.get(0).getId());
        verify(repositorio, times(1)).findByGeneroAndNombreContainingIgnoreCase(Genero.MASCULINO, "Jo");
    }

    @Test
    public void getByIdTest() {
        when(repositorio.findById(1L)).thenReturn(Optional.of(mockList.get(0)));
        Empleado empleado = eServ.getById(1L);
        assertEquals("Jose", empleado.getNombre());
        assertEquals("Jose@gmail.com", empleado.getCorreo());
    }

    @Test
    public void agregarTestFine() {
        when(repositorio.save(empleadoSinId)).thenReturn(empleadoConId);
        Empleado insersion = eServ.agregar(empleadoSinId);
        assertEquals(4l, insersion.getId());
        assertEquals("Sandra", insersion.getNombre());
        verify(repositorio, times(1)).save(empleadoSinId);
    }

    @Test
    public void agregarTestFail() {
        Empleado empError = new Empleado(null, "Pedro", "pedrog@gmail.com", 320d, Estado.ACTIVO, Genero.MASCULINO);
        when(repositorio.save(empError)).thenThrow(new ConstraintViolationException("Error sueldo", null));
        assertThrows(RuntimeException.class, () -> {
            eServ.agregar(empError);
        });
        verify(repositorio, times(1)).save(empError);
    }

    @Test
    public void modificarTestFine() {
        when(repositorio.save(empleadoConId)).thenReturn(empleadoModificado);
        Empleado insersion = eServ.modificar(empleadoConId);
        assertEquals(4l, insersion.getId());
        assertEquals("Sandra", insersion.getNombre());
        assertEquals("sandra2@mail.com", insersion.getCorreo());
        verify(repositorio, times(1)).save(empleadoConId);
    }

    @Test
    public void borrarPorIdTestFine() {
        when(repositorio.findById(1l)).thenReturn(Optional.of(mockList.get(0)));
        eServ.borrarPorId(1l);
        verify(repositorio, times(1)).findById(1l);
        verify(repositorio, times(1)).deleteById(1l);
    }

    @Test
    public void borrarPorIdTestFail() {
        when(repositorio.findById(5l)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
            eServ.borrarPorId(5l);
        });
        verify(repositorio, times(1)).findById(5l);
        verify(repositorio, times(0)).deleteById(5l);
    }
}
