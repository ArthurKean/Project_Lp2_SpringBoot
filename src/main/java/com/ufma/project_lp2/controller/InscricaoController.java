package com.ufma.project_lp2.controller;

import com.ufma.project_lp2.service.InscricaoService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Inscricao inscrever(@RequestParam String tituloOportunidade, @RequestParam String emailDiscente, @RequestParam String motivacao) {
        Oportunidade op = oportunidadeService.buscarPorTitulo(tituloOportunidade);
        Usuario discente = usuarioService.buscarPorEmail(emailDiscente);
        if (op != null && discente instanceof Discente) {
            return inscricaoService.inscrever(op, (Discente) discente, motivacao);
        }
        return null;
    }

    @PutMapping("/{id}/cancelar")
    public Inscricao cancelarInscricao(@PathVariable Long id) {
        Inscricao inscricao = inscricaoService.buscarPorId(id);
        return inscricaoService.cancelarInscricao(inscricao);
    }

    @PutMapping("/{id}/aprovar")
    public Inscricao aprovarInscricao(@PathVariable Long id) {
        Inscricao inscricao = inscricaoService.buscarPorId(id);
        return inscricaoService.aprovarInscricao(inscricao);
    }

    @PutMapping("/{id}/rejeitar")
    public Inscricao rejeitarInscricao(@PathVariable Long id) {
        Inscricao inscricao = inscricaoService.buscarPorId(id);
        return inscricaoService.rejeitarInscricao(inscricao);
    }

    @GetMapping("/oportunidade/{tituloOportunidade}")
    public List<Inscricao> listarInscritos(@PathVariable String tituloOportunidade) {
        Oportunidade op = oportunidadeService.buscarPorTitulo(tituloOportunidade);
        return inscricaoService.listarInscritos(op);
    }

    @PutMapping("/{idAtual}/substituir/{idNova}")
    public String substituirParticipante(@PathVariable Long idAtual, @PathVariable Long idNova, @RequestParam String justificativa) {
        Inscricao atual = inscricaoService.buscarPorId(idAtual);
        Inscricao nova = inscricaoService.buscarPorId(idNova);
        return usuarioService.substituirParticipante(atual, nova, justificativa);
    }
}
