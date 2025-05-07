package com.tarea10.dgmt10e03.domain;

import com.tarea10.dgmt10e03.modelos.Estado;
import com.tarea10.dgmt10e03.modelos.Genero;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
public class Empleado {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3, message = "El nombre debe tener al menos 3 caracteres.")
    @NotEmpty(message = "El nombre no puede estar vacío.")
    @NotNull(message = "El nombre es obligatorio.")
    private String nombre;

    @Email(message = "El correo debe tener un formato válido.")
    @NotEmpty(message = "El correo no puede estar vacío.")
    @NotNull(message = "El correo es obligatorio.")
    private String correo;

    @Min(value = 18000, message = "El salario no puede ser menor a 18,000.")
    @NotNull(message = "El salario es obligatorio.")
    private Double salario;

    @NotNull(message = "El estado es obligatorio.")
    private Estado estado;

    private Genero genero;
}
