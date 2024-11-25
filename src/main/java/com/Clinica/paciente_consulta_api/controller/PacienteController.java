package com.Clinica.paciente_consulta_api.controller;

import com.Clinica.paciente_consulta_api.model.Paciente;
import com.Clinica.paciente_consulta_api.model.Consulta;
import com.Clinica.paciente_consulta_api.repository.PacienteRepository;
import com.Clinica.paciente_consulta_api.repository.ConsultaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteRepository pacienteRepository;
    private final ConsultaRepository consultaRepository;

    public PacienteController(PacienteRepository pacienteRepository, ConsultaRepository consultaRepository) {
        this.pacienteRepository = pacienteRepository;
        this.consultaRepository = consultaRepository;
    }

    // Listar todos os pacientes
    @GetMapping(produces = {"application/json", "application/xml"})
    public List<Paciente> listarPacientes() {
        return pacienteRepository.findAll();
    }

    // Obter paciente por ID
    @GetMapping(value = "/{id}", produces = {"application/json", "application/xml"})
    public ResponseEntity<Paciente> obterPaciente(@PathVariable Long id) {
        Optional<Paciente> paciente = pacienteRepository.findById(id);
        return paciente.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Criar um novo paciente
    @PostMapping(consumes = {"application/json", "application/xml"}, produces = {"application/json", "application/xml"})
    public Paciente criarPaciente(@RequestBody Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    // Criar uma consulta para um paciente específico
    @PostMapping(value = "/{pacienteId}/consultas", consumes = {"application/json", "application/xml"}, produces = {"application/json", "application/xml"})
    public ResponseEntity<Consulta> criarConsultaParaPaciente(@PathVariable Long pacienteId, @RequestBody Consulta consulta) {
        Optional<Paciente> paciente = pacienteRepository.findById(pacienteId);
        if (paciente.isPresent()) {
            consulta.setPaciente(paciente.get());
            Consulta consultaSalva = consultaRepository.save(consulta);
            paciente.get().getConsultas().add(consultaSalva);  // Adiciona consulta à lista do paciente
            return ResponseEntity.ok(consultaSalva);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Listar todas as consultas de um paciente específico
    @GetMapping(value = "/{pacienteId}/consultas", produces = {"application/json", "application/xml"})
    public ResponseEntity<List<Consulta>> listarConsultasPorPaciente(@PathVariable Long pacienteId) {
        Optional<Paciente> paciente = pacienteRepository.findById(pacienteId);
        if (paciente.isPresent()) {
            return ResponseEntity.ok(paciente.get().getConsultas());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Atualizar uma consulta de um paciente específico
    @PutMapping(value = "/{pacienteId}/consultas/{consultaId}", consumes = {"application/json", "application/xml"}, produces = {"application/json", "application/xml"})
    public ResponseEntity<String> atualizarConsulta(@PathVariable Long pacienteId, @PathVariable Long consultaId, @RequestBody Consulta consultaAtualizada) {
        Optional<Paciente> paciente = pacienteRepository.findById(pacienteId);
        if (paciente.isPresent()) {
            List<Consulta> consultas = paciente.get().getConsultas();
            for (Consulta consulta : consultas) {
                if (consulta.getId().equals(consultaId)) {
                    consulta.setData(consultaAtualizada.getData());
                    consulta.setDescricao(consultaAtualizada.getDescricao());

                    // Salvar a consulta atualizada no banco de dados
                    consultaRepository.save(consulta);

                    return ResponseEntity.ok("Atualização de consulta feita com sucesso!");
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Consulta não encontrada.");  // Consulta não encontrada
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente não encontrado.");  // Paciente não encontrado
        }
    }

    // Excluir uma consulta de um paciente específico
    @DeleteMapping("/{pacienteId}/consultas/{consultaId}")
    public ResponseEntity<String> deletarConsulta(@PathVariable Long pacienteId, @PathVariable Long consultaId) {
        Optional<Paciente> paciente = pacienteRepository.findById(pacienteId);
        if (paciente.isPresent()) {
            List<Consulta> consultas = paciente.get().getConsultas();
            boolean removed = consultas.removeIf(consulta -> consulta.getId().equals(consultaId));
            if (removed) {
                return ResponseEntity.ok("Consulta excluída com sucesso.");
            } else {
                return ResponseEntity.notFound().build();  // Consulta não encontrada
            }
        } else {
            return ResponseEntity.notFound().build();  // Paciente não encontrado
        }
    }

    // Excluir paciente (e suas consultas associadas)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarPaciente(@PathVariable Long id) {
        Optional<Paciente> paciente = pacienteRepository.findById(id);
        if (paciente.isPresent()) {
            pacienteRepository.deleteById(id);  // Exclui o paciente e as consultas associadas
            return ResponseEntity.ok("Paciente excluído com sucesso.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

