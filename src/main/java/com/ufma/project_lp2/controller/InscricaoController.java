package com.ufma.project_lp2.controller;

import com.ufma.project_lp2.service.InscricaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ufma.project_lp2.model.Inscricao;
import com.ufma.project_lp2.model.Oportunidade;
import com.ufma.project_lp2.model.Discente;
import com.ufma.project_lp2.model.Usuario;
import com.ufma.project_lp2.service.OportunidadeService;
import com.ufma.project_lp2.service.UsuarioService;

@RestController
@RequestMapping("/api/inscricoes")
public class InscricaoController {

    @Autowired
    private InscricaoService inscricaoService;

    @Autowired
    private OportunidadeService oportunidadeService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<?> inscrever(@RequestParam String tituloOportunidade,
                                       @RequestParam String emailDiscente,
                                       @RequestParam String motivacao) {
        Oportunidade op = oportunidadeService.buscarPorTitulo(tituloOportunidade);
        if (op == null) {
            return ResponseEntity.status(404).body("Oportunidade '" + tituloOportunidade + "' não encontrada.");
        }
        Usuario discente = usuarioService.buscarPorEmail(emailDiscente);
        if (discente == null) {
            return ResponseEntity.status(404).body("Esse email não tá cadastrado no sistema.");
        }
        if (!(discente instanceof Discente)) {
            return ResponseEntity.badRequest().body(discente.getNome() + " não é um discente.");
        }
        return ResponseEntity.ok(inscricaoService.inscrever(op, (Discente) discente, motivacao));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarInscricao(@PathVariable Long id) {
        Inscricao inscricao = inscricaoService.buscarPorId(id);
        if (inscricao == null) {
            return ResponseEntity.status(404).body("Inscrição " + id + " não encontrada.");
        }
        return ResponseEntity.ok(inscricaoService.cancelarInscricao(inscricao));
    }

    @PutMapping("/{id}/aprovar")
    public ResponseEntity<?> aprovarInscricao(@PathVariable Long id) {
        Inscricao inscricao = inscricaoService.buscarPorId(id);
        if (inscricao == null) {
            return ResponseEntity.status(404).body("Inscrição " + id + " não encontrada.");
        }
        return ResponseEntity.ok(inscricaoService.aprovarInscricao(inscricao));
    }

    @PutMapping("/{id}/rejeitar")
    public ResponseEntity<?> rejeitarInscricao(@PathVariable Long id) {
        Inscricao inscricao = inscricaoService.buscarPorId(id);
        if (inscricao == null) {
            return ResponseEntity.status(404).body("Inscrição " + id + " não encontrada.");
        }
        return ResponseEntity.ok(inscricaoService.rejeitarInscricao(inscricao));
    }

    @GetMapping("/oportunidade/{tituloOportunidade}")
    public ResponseEntity<?> listarInscritos(@PathVariable String tituloOportunidade) {
        Oportunidade op = oportunidadeService.buscarPorTitulo(tituloOportunidade);
        if (op == null) {
            return ResponseEntity.status(404).body("Oportunidade '" + tituloOportunidade + "' não encontrada.");
        }
        return ResponseEntity.ok(inscricaoService.listarInscritos(op));
    }

    @PutMapping("/{idAtual}/substituir/{idNova}")
    public ResponseEntity<String> substituirParticipante(@PathVariable Long idAtual,
                                                         @PathVariable Long idNova,
                                                         @RequestParam String justificativa) {
        Inscricao atual = inscricaoService.buscarPorId(idAtual);
        Inscricao nova = inscricaoService.buscarPorId(idNova);
        if (atual == null || nova == null) {
            return ResponseEntity.status(404).body("Uma das inscrições não foi encontrada.");
        }
        return ResponseEntity.ok(usuarioService.substituirParticipante(atual, nova, justificativa));
    }
}
