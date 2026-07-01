package com.ufma.project_lp2.controller;

import com.ufma.project_lp2.service.AproveitamentoService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Aproveitamento registrarAproveitamento(@RequestBody Aproveitamento solicitacao, @RequestParam String emailDiscente) {
        Usuario discente = usuarioService.buscarPorEmail(emailDiscente);
        if (discente instanceof Discente) {
            solicitacao.setDiscente((Discente) discente);
        }
        return aproveitamentoService.registrarAproveitamento(solicitacao);
    }

    @GetMapping("/pendentes")
    public List<Aproveitamento> listarPendentes() {
        return aproveitamentoService.listarPendentes();
    }

    @PutMapping("/{id}/aprovar")
    public boolean aprovarSolicitacao(@PathVariable Long id, @RequestParam String emailAvaliador) {
        Aproveitamento ap = aproveitamentoService.buscarPorId(id);
        Usuario avaliador = usuarioService.buscarPorEmail(emailAvaliador);
        return aproveitamentoService.aprovarSolicitacao(ap, avaliador);
    }

    @PutMapping("/{id}/rejeitar")
    public boolean rejeitarSolicitacao(@PathVariable Long id, @RequestParam String emailAvaliador, @RequestParam String motivo) {
        Aproveitamento ap = aproveitamentoService.buscarPorId(id);
        Usuario avaliador = usuarioService.buscarPorEmail(emailAvaliador);
        return aproveitamentoService.rejeitarSolicitacao(ap, avaliador, motivo);
    }

    @PutMapping("/{id}/reenviar")
    public boolean reenviarSolicitacao(@PathVariable Long id, @RequestParam String novoCertificadoPath) {
        Aproveitamento ap = aproveitamentoService.buscarPorId(id);
        return aproveitamentoService.reenviarSolicitacao(ap, novoCertificadoPath);
    }

    @GetMapping("/horas/{emailDiscente}")
    public int calcularHorasAprovadas(@PathVariable String emailDiscente) {
        Usuario discente = usuarioService.buscarPorEmail(emailDiscente);
        if (discente instanceof Discente) {
            return aproveitamentoService.calcularHorasAprovadas((Discente) discente);
        }
        return 0;
    }

    @GetMapping("/{id}/prazo")
    public String verificarPrazo(@PathVariable Long id) {
        Aproveitamento ap = aproveitamentoService.buscarPorId(id);
        return aproveitamentoService.verificarPrazo(ap);
    }
}
