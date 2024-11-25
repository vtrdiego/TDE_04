package com.Clinica.paciente_consulta_api.controller;

import com.Clinica.paciente_consulta_api.model.Consulta;
import com.Clinica.paciente_consulta_api.repository.ConsultaRepository;
import com.Clinica.paciente_consulta_api.repository.PacienteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;

    public ConsultaController(ConsultaRepository consultaRepository, PacienteRepository pacienteRepository) {
        this.consultaRepository = consultaRepository;
        this.pacienteRepository = pacienteRepository;
    }

    // Listar todas as consultas
    @GetMapping(produces = {"application/json", "application/xml"})
    public List<Consulta> listarConsultas() {
        return consultaRepository.findAll();
    }

    // Obter consulta por ID
    @GetMapping(value = "/{id}", produces = {"application/json", "application/xml"})
    public ResponseEntity<Consulta> obterConsulta(@PathVariable Long id) {
        Optional<Consulta> consulta = consultaRepository.findById(id);
        return consulta.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Criar uma nova consulta
    @PostMapping(consumes = {"application/json", "application/xml"}, produces = {"application/json", "application/xml"})
    public Consulta criarConsulta(@RequestBody Consulta consulta) {
        return consultaRepository.save(consulta);
    }

    // Atualizar consulta por ID
    @PutMapping(value = "/{id}", consumes = {"application/json", "application/xml"}, produces = {"application/json", "application/xml"})
    public ResponseEntity<Consulta> atualizarConsulta(@PathVariable Long id, @RequestBody Consulta consultaAtualizada) {
        Optional<Consulta> consultaOptional = consultaRepository.findById(id);
        if (consultaOptional.isPresent()) {
            Consulta consulta = consultaOptional.get();
            consulta.setData(consultaAtualizada.getData());
            consulta.setDescricao(consultaAtualizada.getDescricao());
            // Persistir a atualização no banco de dados
            Consulta consultaSalva = consultaRepository.save(consulta);
            return ResponseEntity.ok(consultaSalva);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Excluir consulta por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarConsulta(@PathVariable Long id) {
        Optional<Consulta> consulta = consultaRepository.findById(id);
        if (consulta.isPresent()) {
            consultaRepository.deleteById(id);
            return ResponseEntity.ok("Consulta excluída com sucesso.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
