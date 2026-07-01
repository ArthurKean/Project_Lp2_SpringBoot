package com.ufma.project_lp2.controller;

import com.ufma.project_lp2.model.Curso;
import com.ufma.project_lp2.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ufma.project_lp2.model.Discente;
import com.ufma.project_lp2.model.Usuario;
import com.ufma.project_lp2.model.enums.StatusMatricula;
import com.ufma.project_lp2.service.UsuarioService;

import java.util.List;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @PostMapping
    public ResponseEntity<String> cadastrarCurso(@RequestBody Curso curso) {
        if (cursoService.buscarPorCodigo(curso.getCodigo()) != null) {
            return ResponseEntity.badRequest().body("Esse código de curso já tá cadastrado.");
        }
        cursoService.cadastrarCurso(curso);
        return ResponseEntity.ok("Curso cadastrado!!!!");
    }

    @GetMapping
    public List<Curso> listarTodos() {
        return cursoService.listarTodos();
    }

    @GetMapping("/{codigo}")
    public Curso buscarPorCodigo(@PathVariable int codigo) {
        return cursoService.buscarPorCodigo(codigo);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<String> removerCurso(@PathVariable int codigo) {
        if (cursoService.buscarPorCodigo(codigo) == null) {
            return ResponseEntity.status(404).body("Curso não encontrado.");
        }
        cursoService.removerCurso(codigo);
        return ResponseEntity.ok("Curso removido.");
    }

    @PutMapping("/{codigo}/ppc")
    public Curso atualizarPPC(@PathVariable int codigo, @RequestParam int novasCargaHoraria, @RequestParam String novaVersao) {
        cursoService.atualizarPPC(codigo, novasCargaHoraria, novaVersao);
        return cursoService.buscarPorCodigo(codigo);
    }

    @Autowired
    private UsuarioService usuarioService;

    @PutMapping("/{codigo}/matricular")
    public ResponseEntity<String> matricularDiscente(@PathVariable int codigo, @RequestParam String emailDiscente) {
        Usuario u = usuarioService.buscarPorEmail(emailDiscente);

        if (u == null) {
            return ResponseEntity.status(404).body("Esse email não tá cadastrado no sistema.");
        }
        if (!(u instanceof Discente)) {
            return ResponseEntity.badRequest().body(u.getNome() + " não é um discente.");
        }
        if (cursoService.buscarPorCodigo(codigo) == null) {
            return ResponseEntity.status(404).body("Curso " + codigo + " não encontrado.");
        }

        cursoService.matricularDiscente(codigo, (Discente) u);
        return ResponseEntity.ok(u.getNome() + " matriculado(a) no curso!");
    }

    @GetMapping("/{codigo}/alunos/status")
    public List<Discente> listarAlunosPorStatus(@PathVariable int codigo, @RequestParam StatusMatricula status) {
        return cursoService.listarAlunosPorStatus(codigo, status);
    }

    @GetMapping("/{codigo}/alunos/total")
    public int totalDeAlunos(@PathVariable int codigo) {
        return cursoService.totalDeAlunos(codigo);
    }

    @GetMapping("/{codigo}/historico-ppc")
    public List<String> listarHistoricoPpc(@PathVariable int codigo) {
        Curso curso = cursoService.buscarPorCodigo(codigo);
        if (curso != null) {
            return curso.getHistoricoPpc();
        }
        return null;
    }
}
