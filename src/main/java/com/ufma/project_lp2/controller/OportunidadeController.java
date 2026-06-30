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

    @PostMapping
    public void registrarOportunidade(@RequestBody Oportunidade oportunidade) {
        oportunidadeService.registrarOportunidade(oportunidade);
    }

    @GetMapping
    public List<Oportunidade> listarOportunidades() {
        return oportunidadeService.listarOportunidades();
    }

    @GetMapping("/abertas")
    public List<Oportunidade> listarOportunidadesAbertas() {
        return oportunidadeService.listarOportunidadesAbertas();
    }
}
