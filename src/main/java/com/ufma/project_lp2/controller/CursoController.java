package com.ufma.project_lp2.controller;

import com.ufma.project_lp2.model.Curso;
import com.ufma.project_lp2.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
