package com.ufma.project_lp2.controller;


import com.ufma.project_lp2.service.CertificadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ufma.project_lp2.model.Certificado;
import com.ufma.project_lp2.model.Discente;
import com.ufma.project_lp2.model.Usuario;
import com.ufma.project_lp2.model.Oportunidade;
import com.ufma.project_lp2.service.UsuarioService;
import com.ufma.project_lp2.service.OportunidadeService;

@RestController
@RequestMapping("/api/certificados")
public class CertificadoController {

    @Autowired
    private CertificadoService certificadoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private OportunidadeService oportunidadeService;

    @PostMapping
    public Certificado guardarCertificado(@RequestBody Certificado certificado) {
        return certificadoService.guardarRegistroDeCertificadoOficial(certificado);
    }

    @GetMapping("/meus/{emailDiscente}")
    public List<Certificado> listarMeusCertificados(@PathVariable String emailDiscente) {
        Usuario discente = usuarioService.buscarPorEmail(emailDiscente);
        if (discente instanceof Discente) {
            return certificadoService.listarMeusCertificados((Discente) discente);
        }
        return null;
    }

    @PostMapping("/lote")
    public List<Certificado> gerarLote(
            @RequestParam String tituloOportunidade,
            @RequestParam int cargaHoraria,
            @RequestParam String caminhoBase) {
        
        Oportunidade oportunidade = oportunidadeService.buscarPorTitulo(tituloOportunidade);
        if (oportunidade != null) {
            return certificadoService.solicitarGeracaoDeLote(oportunidade, cargaHoraria, caminhoBase);
        }
        return null;
    }

    @GetMapping("/validar/{hash}")
    public boolean consultarAutenticidade(@PathVariable String hash) {
        return certificadoService.consultarAutenticidadeNaUFMA(hash);
    }
}
