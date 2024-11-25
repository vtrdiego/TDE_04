package com.Clinica.paciente_consulta_api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "consultas")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto incremento
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false, length = 255)
    private String descricao;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Exclui REMOVE do cascade
    @JoinColumn(name = "paciente_id", nullable = false, foreignKey = @ForeignKey(name = "fk_consultas_paciente"))
    @JsonBackReference
    private Paciente paciente;

    // Método setter para paciente sem chamada adicional de referência
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    // Métodos getters e setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getData() {
        return data;
    }

    public String getDescricao() {
        return descricao;
    }

    public Paciente getPaciente() {
        return paciente;
    }
}