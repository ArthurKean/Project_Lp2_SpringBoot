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

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @PostMapping
    public void cadastrarCurso(@RequestBody Curso curso) {
        cursoService.cadastrarCurso(curso);
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
    public void removerCurso(@PathVariable int codigo) {
        cursoService.removerCurso(codigo);
    }

    @PutMapping("/{codigo}/ppc")
    public Curso atualizarPPC(@PathVariable int codigo, @RequestParam int novasCargaHoraria, @RequestParam String novaVersao) {
        cursoService.atualizarPPC(codigo, novasCargaHoraria, novaVersao);
        return cursoService.buscarPorCodigo(codigo);
    }

    @Autowired
    private UsuarioService usuarioService;

    @PutMapping("/{codigo}/matricular")
    public void matricularDiscente(@PathVariable int codigo, @RequestParam String emailDiscente) {
        Usuario u = usuarioService.buscarPorEmail(emailDiscente);
        if (u instanceof Discente) {
            cursoService.matricularDiscente(codigo, (Discente) u);
        }
    }

    @GetMapping("/{codigo}/alunos/status")
    public List<Discente> listarAlunosPorStatus(@PathVariable int codigo, @RequestParam StatusMatricula status) {
        return cursoService.listarAlunosPorStatus(codigo, status);
    }

    @GetMapping("/{codigo}/alunos/total")
    public int totalDeAlunos(@PathVariable int codigo) {
        return cursoService.totalDeAlunos(codigo);
    }
}
