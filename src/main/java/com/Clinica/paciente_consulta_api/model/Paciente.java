package com.Clinica.paciente_consulta_api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pacientes")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto incremento
    private Long id;

    @Column(nullable = false, length = 100) // Define regras para a coluna
    private String nome;

    @Column(nullable = false, unique = true, length = 20) // CPF único
    private String cpf;
    
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Consulta> consultas = new ArrayList<>();

    // Método para adicionar consulta ao paciente
    public void addConsulta(Consulta consulta) {
        consulta.setPaciente(this); // Define a referência do paciente na consulta
        this.consultas.add(consulta); // Adiciona a consulta à lista de consultas
    }

    public void removeConsulta(Consulta consulta) {
        this.consultas.remove(consulta);
        consulta.setPaciente(null); // Remove a referência do paciente na consulta
    }

    // Métodos getters e setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }
}