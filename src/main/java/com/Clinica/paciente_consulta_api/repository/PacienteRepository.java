package com.Clinica.paciente_consulta_api.repository;

import com.Clinica.paciente_consulta_api.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    // Métodos adicionais podem ser adicionados aqui, se necessário
}