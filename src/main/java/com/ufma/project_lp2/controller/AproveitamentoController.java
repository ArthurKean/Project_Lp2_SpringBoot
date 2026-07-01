package com.ufma.project_lp2.controller;

import com.ufma.project_lp2.service.AproveitamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ufma.project_lp2.model.Aproveitamento;
import com.ufma.project_lp2.model.Discente;
import com.ufma.project_lp2.model.Usuario;
import com.ufma.project_lp2.service.UsuarioService;

@RestController
@RequestMapping("/api/aproveitamentos")
public class AproveitamentoController {

    @Autowired
    private AproveitamentoService aproveitamentoService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<?> registrarAproveitamento(@RequestBody Aproveitamento solicitacao,
                                                     @RequestParam String emailDiscente) {
        Usuario discente = usuarioService.buscarPorEmail(emailDiscente);
        if (discente == null) {
            return ResponseEntity.status(404).body("Esse email não tá cadastrado no sistema.");
        }
        if (!(discente instanceof Discente)) {
            return ResponseEntity.badRequest().body(discente.getNome() + " não é um discente.");
        }
        solicitacao.setDiscente((Discente) discente);
        return ResponseEntity.ok(aproveitamentoService.registrarAproveitamento(solicitacao));
    }

    @GetMapping("/pendentes")
    public List<Aproveitamento> listarPendentes() {
        return aproveitamentoService.listarPendentes();
    }

    @PutMapping("/{id}/aprovar")
    public ResponseEntity<String> aprovarSolicitacao(@PathVariable Long id, @RequestParam String emailAvaliador) {
        Aproveitamento ap = aproveitamentoService.buscarPorId(id);
        if (ap == null) {
            return ResponseEntity.status(404).body("Aproveitamento " + id + " não encontrado.");
        }
        Usuario avaliador = usuarioService.buscarPorEmail(emailAvaliador);
        if (avaliador == null) {
            return ResponseEntity.status(404).body("Avaliador não encontrado.");
        }
        boolean ok = aproveitamentoService.aprovarSolicitacao(ap, avaliador);
        return ok ? ResponseEntity.ok("Aproveitamento aprovado!") :
                    ResponseEntity.badRequest().body("Não foi possível aprovar. Verifique o status da solicitação.");
    }

    @PutMapping("/{id}/rejeitar")
    public ResponseEntity<String> rejeitarSolicitacao(@PathVariable Long id,
                                                      @RequestParam String emailAvaliador,
                                                      @RequestParam String motivo) {
        Aproveitamento ap = aproveitamentoService.buscarPorId(id);
        if (ap == null) {
            return ResponseEntity.status(404).body("Aproveitamento " + id + " não encontrado.");
        }
        Usuario avaliador = usuarioService.buscarPorEmail(emailAvaliador);
        if (avaliador == null) {
            return ResponseEntity.status(404).body("Avaliador não encontrado.");
        }
        boolean ok = aproveitamentoService.rejeitarSolicitacao(ap, avaliador, motivo);
        return ok ? ResponseEntity.ok("Aproveitamento rejeitado.") :
                    ResponseEntity.badRequest().body("Não foi possível rejeitar. Verifique o status da solicitação.");
    }

    @PutMapping("/{id}/reenviar")
    public ResponseEntity<String> reenviarSolicitacao(@PathVariable Long id,
                                                      @RequestParam String novoCertificadoPath) {
        Aproveitamento ap = aproveitamentoService.buscarPorId(id);
        if (ap == null) {
            return ResponseEntity.status(404).body("Aproveitamento " + id + " não encontrado.");
        }
        boolean ok = aproveitamentoService.reenviarSolicitacao(ap, novoCertificadoPath);
        return ok ? ResponseEntity.ok("Solicitação reenviada!") :
                    ResponseEntity.badRequest().body("Só dá pra reenviar solicitações que foram rejeitadas.");
    }

    @GetMapping("/horas/{emailDiscente}")
    public ResponseEntity<?> calcularHorasAprovadas(@PathVariable String emailDiscente) {
        Usuario discente = usuarioService.buscarPorEmail(emailDiscente);
        if (discente == null) {
            return ResponseEntity.status(404).body("Discente não encontrado.");
        }
        if (!(discente instanceof Discente)) {
            return ResponseEntity.badRequest().body(discente.getNome() + " não é um discente.");
        }
        return ResponseEntity.ok(aproveitamentoService.calcularHorasAprovadas((Discente) discente));
    }

    @GetMapping("/{id}/prazo")
    public ResponseEntity<String> verificarPrazo(@PathVariable Long id) {
        Aproveitamento ap = aproveitamentoService.buscarPorId(id);
        if (ap == null) {
            return ResponseEntity.status(404).body("Aproveitamento " + id + " não encontrado.");
        }
        return ResponseEntity.ok(aproveitamentoService.verificarPrazo(ap));
    }
}
