package com.ufma.project_lp2.controller;

import com.ufma.project_lp2.model.Oportunidade;
import com.ufma.project_lp2.model.Usuario;
import com.ufma.project_lp2.service.OportunidadeService;
import com.ufma.project_lp2.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/oportunidades")
public class OportunidadeController {

    @Autowired
    private OportunidadeService oportunidadeService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<?> registrarOportunidade(@RequestBody Oportunidade oportunidade, @RequestParam String emailAutor) {
        Usuario autor = usuarioService.buscarPorEmail(emailAutor);
        if (autor == null) {
            return ResponseEntity.status(404).body("Autor não encontrado com esse email.");
        }
        oportunidade.setAutor(autor);
        return ResponseEntity.ok(oportunidadeService.registrarOportunidade(oportunidade));
    }

    @GetMapping
    public List<Oportunidade> listarOportunidades() {
        return oportunidadeService.listarOportunidades();
    }

    @GetMapping("/abertas")
    public List<Oportunidade> listarOportunidadesAbertas() {
        return oportunidadeService.listarOportunidadesAbertas();
    }

    @PutMapping("/{id}/fechar")
    public ResponseEntity<?> fecharOportunidade(@PathVariable Long id) {
        for (Oportunidade op : oportunidadeService.listarOportunidades()) {
            if (op.getId() != null && op.getId().equals(id)) {
                oportunidadeService.encerrarOportunidade(op);
                return ResponseEntity.ok(op);
            }
        }
        return ResponseEntity.status(404).body("Oportunidade " + id + " não existe.");
    }

    @PutMapping("/{id}/divulgar")
    public ResponseEntity<?> divulgarOportunidade(@PathVariable Long id, @RequestParam String emailRequisitante) {
        Usuario req = usuarioService.buscarPorEmail(emailRequisitante);
        if (req == null) {
            return ResponseEntity.status(404).body("Esse email não tá cadastrado no sistema.");
        }
        for (Oportunidade op : oportunidadeService.listarOportunidades()) {
            if (op.getId() != null && op.getId().equals(id)) {
                oportunidadeService.divulgarOportunidade(op, req);
                return ResponseEntity.ok(op);
            }
        }
        return ResponseEntity.status(404).body("Oportunidade " + id + " não existe.");
    }

    @PutMapping("/{id}/aprovar")
    public ResponseEntity<?> aprovarOportunidade(@PathVariable Long id, @RequestParam String emailAvaliador) {
        Usuario avaliador = usuarioService.buscarPorEmail(emailAvaliador);
        if (avaliador == null) {
            return ResponseEntity.status(404).body("Avaliador não encontrado.");
        }
        for (Oportunidade op : oportunidadeService.listarOportunidades()) {
            if (op.getId() != null && op.getId().equals(id)) {
                oportunidadeService.aprovarOportunidade(op, avaliador);
                return ResponseEntity.ok(op);
            }
        }
        return ResponseEntity.status(404).body("Oportunidade " + id + " não existe.");
    }
}
