package com.ufma.project_lp2.controller;


import com.ufma.project_lp2.service.GrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> registrarGrupo(@RequestBody Grupo grupo) {
        Grupo salvo = grupoService.registrarGrupo(grupo);
        if (salvo == null) {
            return ResponseEntity.badRequest().body("Não foi possível registrar o grupo.");
        }
        return ResponseEntity.ok(salvo);
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
    public ResponseEntity<?> buscarPorNome(@PathVariable String nome) {
        Grupo grupo = grupoService.buscarGrupoPorNome(nome);
        if (grupo == null) {
            return ResponseEntity.status(404).body("Grupo '" + nome + "' não encontrado.");
        }
        return ResponseEntity.ok(grupo);
    }

    @PutMapping("/{nome}/aprovar")
    public ResponseEntity<?> aprovarGrupo(@PathVariable String nome) {
        Grupo grupo = grupoService.buscarGrupoPorNome(nome);
        if (grupo == null) {
            return ResponseEntity.status(404).body("Grupo '" + nome + "' não existe.");
        }
        return ResponseEntity.ok(grupoService.aprovarGrupo(grupo));
    }

    @GetMapping("/{nome}/membros")
    public ResponseEntity<?> listarMembros(@PathVariable String nome) {
        Grupo grupo = grupoService.buscarGrupoPorNome(nome);
        if (grupo == null) {
            return ResponseEntity.status(404).body("Grupo '" + nome + "' não encontrado.");
        }
        return ResponseEntity.ok(grupoService.listarUsuariosdeUmGrupo(nome));
    }

    @PostMapping("/{nome}/membros/{email}")
    public ResponseEntity<?> adicionarMembro(@PathVariable String nome, @PathVariable String email) {
        Grupo grupo = grupoService.buscarGrupoPorNome(nome);
        if (grupo == null) {
            return ResponseEntity.status(404).body("Grupo '" + nome + "' não encontrado.");
        }
        Usuario membro = usuarioService.buscarPorEmail(email);
        if (membro == null) {
            return ResponseEntity.status(404).body("Nenhum usuário com esse email foi encontrado.");
        }
        return ResponseEntity.ok(grupoService.adicionarMembro(grupo, membro));
    }

    @DeleteMapping("/{nome}/membros/{email}")
    public ResponseEntity<?> removerMembro(@PathVariable String nome, @PathVariable String email) {
        Grupo grupo = grupoService.buscarGrupoPorNome(nome);
        if (grupo == null) {
            return ResponseEntity.status(404).body("Grupo '" + nome + "' não encontrado.");
        }
        Usuario membro = usuarioService.buscarPorEmail(email);
        if (membro == null) {
            return ResponseEntity.status(404).body("Nenhum usuário com esse email foi encontrado.");
        }
        return ResponseEntity.ok(grupoService.removerMembro(grupo, membro));
    }

    @PutMapping("/{nome}/cargos")
    public ResponseEntity<?> atribuirCargo(
            @PathVariable String nome,
            @RequestParam String emailDiscente,
            @RequestParam Cargos cargo,
            @RequestParam String emailDocente) {

        Grupo grupo = grupoService.buscarGrupoPorNome(nome);
        if (grupo == null) {
            return ResponseEntity.status(404).body("Grupo '" + nome + "' não encontrado.");
        }
        Usuario discente = usuarioService.buscarPorEmail(emailDiscente);
        if (discente == null) {
            return ResponseEntity.status(404).body("Discente não encontrado.");
        }
        Usuario docenteObj = usuarioService.buscarPorEmail(emailDocente);
        if (!(docenteObj instanceof Docente)) {
            return ResponseEntity.badRequest().body("O email informado não pertence a um docente.");
        }
        return ResponseEntity.ok(grupoService.atribuirCargo(grupo, discente, cargo, (Docente) docenteObj));
    }

    @PostMapping("/solicitacoes")
    public ResponseEntity<String> solicitarCriacao(
            @RequestParam String emailDiretor,
            @RequestBody Grupo novoGrupo) {

        Usuario diretorObj = usuarioService.buscarPorEmail(emailDiretor);
        if (diretorObj == null) {
            return ResponseEntity.status(404).body("Esse email não tá cadastrado.");
        }
        if (!(diretorObj instanceof DiscenteDiretor)) {
            return ResponseEntity.badRequest().body(diretorObj.getNome() + " não é um Discente Diretor.");
        }
        return ResponseEntity.ok(grupoService.solicitarCriacaoDeNovoGrupo((DiscenteDiretor) diretorObj, novoGrupo));
    }

    @GetMapping("/{nome}/historico")
    public ResponseEntity<?> listarHistorico(@PathVariable String nome) {
        Grupo grupo = grupoService.buscarGrupoPorNome(nome);
        if (grupo == null) {
            return ResponseEntity.status(404).body("Grupo '" + nome + "' não encontrado.");
        }
        return ResponseEntity.ok(grupo.getHistoricoUsuarios());
    }
}
