package com.tarea10.dgmt10e03.controladores;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tarea10.dgmt10e03.Repositorios.EmpleadoRepository;
import com.tarea10.dgmt10e03.domain.Empleado;
import com.tarea10.dgmt10e03.modelos.Estado;
import com.tarea10.dgmt10e03.modelos.Genero;
import com.tarea10.dgmt10e03.servicios.eService;

@SpringBootTest
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
public class ControladorTest {

    @InjectMocks
    private Controlador controlador;

    @MockitoBean
    private eService eServ;

    @MockitoBean
    private EmpleadoRepository repositorio;

    @Autowired
    MockMvc mockMvc;

    ArrayList<Empleado> mockList;
    ArrayList<Empleado> mockSorted;
    Empleado empleadoSinId, empleadoConId, empleadoModificado;

    @BeforeAll
    public void init() {
        mockList = new ArrayList<>();
        mockList.add(new Empleado(1l, "Jose", "Jose@gmail.com", 30000d, Estado.ACTIVO, Genero.MASCULINO));
        mockList.add(new Empleado(2l, "Pedro", "pedrog@gmail.com", 32000d, Estado.ACTIVO, Genero.MASCULINO));
        mockList.add(new Empleado(3l, "Maria", "mcarolina@gmail.com", 35000d, Estado.ACTIVO, Genero.FEMENINO));
        empleadoSinId = new Empleado(null, "Sandra", "Sandra@mail.com", 35000d, Estado.ACTIVO, Genero.FEMENINO);
        empleadoConId = new Empleado(4l, "Sandra", "sandra@mail.com", 35000d, Estado.ACTIVO, Genero.FEMENINO);
        empleadoModificado = new Empleado(4l, "Sandra", "sandra2@mail.com", 35000d, Estado.ACTIVO, Genero.FEMENINO);
        mockSorted = new ArrayList<>();
        mockSorted.add(mockList.get(0));
    }

    @Test
    public void getIndexTest() throws Exception {
        when(eServ.listAll()).thenReturn(mockList);
        mockMvc.perform(get("/empleados")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nombre", is("Jose")))
                .andExpect(jsonPath("$[0].correo", is("Jose@gmail.com")))
                .andExpect(jsonPath("$[0].salario", is(30000d)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].nombre", is("Pedro")))
                .andExpect(jsonPath("$[1].correo", is("pedrog@gmail.com")))
                .andExpect(jsonPath("$[1].salario", is(32000d)))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].nombre", is("Maria")))
                .andExpect(jsonPath("$[2].correo", is("mcarolina@gmail.com")))
                .andExpect(jsonPath("$[2].salario", is(35000d)));
        verify(eServ, times(1)).listAll();
    }

    @Test
    public void getInfoTest() throws Exception {
        when(eServ.getById(1)).thenReturn(mockList.get(0));
        mockMvc.perform(get("/empleados/info/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Jose")))
                .andExpect(jsonPath("$.correo", is("Jose@gmail.com")))
                .andExpect(jsonPath("$.salario", is(30000d)));
        verify(eServ, times(1)).getById(1);
    }

    @Test
    public void postAddTestFine() throws Exception {
        when(eServ.agregar(empleadoSinId)).thenReturn(empleadoConId);
        mockMvc.perform(post("/empleados/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(empleadoSinId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.nombre", is("Sandra")))
                .andExpect(jsonPath("$.salario", is(35000d)));
        verify(eServ, times(1)).agregar(empleadoSinId);
    }

    @Test
    public void postAddTestFail() throws Exception {
        when(eServ.agregar(empleadoSinId)).thenThrow(new RuntimeException("No se pudo agregar el empleado."));
        mockMvc.perform(post("/empleados/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(empleadoSinId)))
                .andExpect(status().isBadRequest());
        verify(eServ, times(1)).agregar(empleadoSinId);
    }

    @Test
    public void postEditTestFine() throws Exception {
        when(eServ.getById(empleadoModificado.getId())).thenReturn(empleadoConId);
        when(eServ.modificar(empleadoModificado)).thenReturn(empleadoModificado);
        mockMvc.perform(put("/empleados/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(empleadoModificado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.nombre", is("Sandra")))
                .andExpect(jsonPath("$.correo", is("sandra2@mail.com")))
                .andExpect(jsonPath("$.salario", is(35000d)));
        verify(eServ, times(1)).getById(empleadoModificado.getId());
        verify(eServ, times(1)).modificar(empleadoModificado);
    }

    @Test
    public void postEditTestFail() throws Exception {
        Empleado empleadoModificado2 = new Empleado(4l, "Sandra", "sandra2@mail.com", 3500d, Estado.ACTIVO,
                Genero.FEMENINO);
        when(eServ.getById(empleadoModificado2.getId())).thenReturn(empleadoConId);
        when(eServ.modificar(empleadoModificado2)).thenThrow(new RuntimeException("No se pudo modificar el empleado."));
        mockMvc.perform(put("/empleados/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(empleadoModificado2)))
                .andExpect(status().isBadRequest());
        verify(eServ, times(1)).getById(empleadoModificado2.getId());
        verify(eServ, times(1)).modificar(empleadoModificado2);
    }

    @Test
    public void postEditTestFail2() throws Exception {
        Empleado empleadoModificado2 = new Empleado(4l, "Sandra", "sandra2@mail.com", 3500d, Estado.ACTIVO,
                Genero.FEMENINO);
        when(eServ.getById(empleadoModificado2.getId()))
                .thenThrow(new RuntimeException("No se encontraron empleados con la ID indicada."));
        when(eServ.modificar(empleadoModificado2)).thenThrow(new RuntimeException("No se pudo modificar el empleado."));
        mockMvc.perform(put("/empleados/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(empleadoModificado2)))
                .andExpect(status().isBadRequest());
        verify(eServ, times(1)).getById(empleadoModificado2.getId());
        verify(eServ, times(0)).modificar(empleadoModificado2);
    }

    @Test
    public void getDeleteTestFine() throws Exception {
        // when(repositorio.findById(1l)).thenReturn(Optional.of(mockList.get(0)));
        mockMvc.perform(delete("/empleados/delete/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(eServ, times(1)).borrarPorId(1);
    }

    @Test
    public void getDeleteTestFail() throws Exception {
        // when(repositorio.findById(1l)).thenReturn(Optional.empty());
        when(eServ.borrarPorId(7)).thenThrow(new RuntimeException("No existe el empleado."));
        mockMvc.perform(delete("/empleados/delete/7")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(eServ, times(1)).borrarPorId(7);
    }
}
