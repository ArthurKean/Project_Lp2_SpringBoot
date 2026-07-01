package com.ufma.project_lp2.controller;

import com.ufma.project_lp2.model.Oportunidade;
import com.ufma.project_lp2.service.OportunidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/oportunidades")
public class OportunidadeController {

    @Autowired
    private OportunidadeService oportunidadeService;

    @Autowired
    private com.ufma.project_lp2.service.UsuarioService usuarioService;

    @PostMapping
    public Oportunidade registrarOportunidade(@RequestBody Oportunidade oportunidade, @RequestParam String emailAutor) {
        com.ufma.project_lp2.model.Usuario autor = usuarioService.buscarPorEmail(emailAutor);
        oportunidade.setAutor(autor);
        return oportunidadeService.registrarOportunidade(oportunidade);
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
    public Oportunidade fecharOportunidade(@PathVariable Long id) {
        for (Oportunidade op : oportunidadeService.listarOportunidades()) {
            if (op.getId() != null && op.getId().equals(id)) {
                oportunidadeService.encerrarOportunidade(op);
                return op;
            }
        }
        return null;
    }

    @PutMapping("/{id}/divulgar")
    public Oportunidade divulgarOportunidade(@PathVariable Long id, @RequestParam String emailRequisitante) {
        com.ufma.project_lp2.model.Usuario req = usuarioService.buscarPorEmail(emailRequisitante);
        for (Oportunidade op : oportunidadeService.listarOportunidades()) {
            if (op.getId() != null && op.getId().equals(id)) {
                if (req != null) {
                    oportunidadeService.divulgarOportunidade(op, req);
                }
                return op;
            }
        }
        return null;
    }

    @PutMapping("/{id}/aprovar")
    public Oportunidade aprovarOportunidade(@PathVariable Long id, @RequestParam String emailAvaliador) {
        com.ufma.project_lp2.model.Usuario avaliador = usuarioService.buscarPorEmail(emailAvaliador);
        for (Oportunidade op : oportunidadeService.listarOportunidades()) {
            if (op.getId() != null && op.getId().equals(id)) {
                if (avaliador != null) {
                    oportunidadeService.aprovarOportunidade(op, avaliador);
                }
                return op;
            }
        }
        return null;
    }
}
