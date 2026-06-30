package com.ufma.project_lp2.controller;


import com.ufma.project_lp2.service.GrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ufma.project_lp2.model.Grupo;
import com.ufma.project_lp2.model.Usuario;
import com.ufma.project_lp2.model.Docente;
import com.ufma.project_lp2.model.DiscenteDiretor;
import com.ufma.project_lp2.model.enums.Cargos;
import com.ufma.project_lp2.service.UsuarioService;

@RestController
@RequestMapping("/api/grupos")
public class GrupoController {

    @Autowired
    private GrupoService grupoService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public Grupo registrarGrupo(@RequestBody Grupo grupo) {
        return grupoService.registrarGrupo(grupo);
    }

    @GetMapping
    public List<Grupo> listarTodos() {
        return grupoService.listarTodosOsGrupos();
    }

    @GetMapping("/ativos")
    public List<Grupo> listarAtivos() {
        return grupoService.listarGruposAtivos();
    }

    @GetMapping("/{nome}")
    public Grupo buscarPorNome(@PathVariable String nome) {
        return grupoService.buscarGrupoPorNome(nome);
    }
    
    @PutMapping("/{nome}/aprovar")
    public Grupo aprovarGrupo(@PathVariable String nome) {
        Grupo grupo = grupoService.buscarGrupoPorNome(nome);
        return grupoService.aprovarGrupo(grupo);
    }

    @GetMapping("/{nome}/membros")
    public List<Usuario> listarMembros(@PathVariable String nome) {
        return grupoService.listarUsuariosdeUmGrupo(nome);
    }

    @PostMapping("/{nome}/membros/{email}")
    public Grupo adicionarMembro(@PathVariable String nome, @PathVariable String email) {
        Grupo grupo = grupoService.buscarGrupoPorNome(nome);
        Usuario membro = usuarioService.buscarPorEmail(email);
        return grupoService.adicionarMembro(grupo, membro);
    }

    @PutMapping("/{nome}/cargos")
    public Grupo atribuirCargo(
            @PathVariable String nome,
            @RequestParam String emailDiscente,
            @RequestParam Cargos cargo,
            @RequestParam String emailDocente) {
        
        Grupo grupo = grupoService.buscarGrupoPorNome(nome);
        Usuario discente = usuarioService.buscarPorEmail(emailDiscente);
        Usuario docenteObj = usuarioService.buscarPorEmail(emailDocente);
        
        if (docenteObj instanceof Docente) {
            return grupoService.atribuirCargo(grupo, discente, cargo, (Docente) docenteObj);
        }
        return null;
    }

    @PostMapping("/solicitacoes")
    public String solicitarCriacao(
            @RequestParam String emailDiretor,
            @RequestBody Grupo novoGrupo) {
        
        Usuario diretorObj = usuarioService.buscarPorEmail(emailDiretor);
        if (diretorObj instanceof DiscenteDiretor) {
            return grupoService.solicitarCriacaoDeNovoGrupo((DiscenteDiretor) diretorObj, novoGrupo);
        }
        return "Erro: Usuário não é um Discente Diretor válido.";
    }
}
