package com.ufma.project_lp2.controller;

import com.ufma.project_lp2.model.enums.StatusAssinatura;
import com.ufma.project_lp2.service.CertificadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
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
    public ResponseEntity<?> guardarCertificado(@RequestBody Certificado certificado,
                                                @RequestParam String emailDiscente,
                                                @RequestParam String tituloOportunidade) {
        Usuario discente = usuarioService.buscarPorEmail(emailDiscente);
        if (discente == null) {
            return ResponseEntity.status(404).body("Discente não encontrado.");
        }
        if (!(discente instanceof Discente)) {
            return ResponseEntity.badRequest().body(discente.getNome() + " não é um discente.");
        }
        Oportunidade op = oportunidadeService.buscarPorTitulo(tituloOportunidade);
        if (op == null) {
            return ResponseEntity.status(404).body("Oportunidade '" + tituloOportunidade + "' não encontrada.");
        }

        certificado.setDiscente((Discente) discente);
        certificado.setOportunidade(op);
        if (certificado.getUuidHash() == null)      certificado.setUuidHash(UUID.randomUUID().toString());
        if (certificado.getDataEmissao() == null)   certificado.setDataEmissao(LocalDate.now());
        if (certificado.getStatusAssinatura() == null) certificado.setStatusAssinatura(StatusAssinatura.PENDENTE);

        return ResponseEntity.ok(certificadoService.guardarRegistroDeCertificadoOficial(certificado));
    }

    @GetMapping("/meus/{emailDiscente}")
    public ResponseEntity<?> listarMeusCertificados(@PathVariable String emailDiscente) {
        Usuario discente = usuarioService.buscarPorEmail(emailDiscente);
        if (discente == null) {
            return ResponseEntity.status(404).body("Esse email não tá cadastrado no sistema.");
        }
        if (!(discente instanceof Discente)) {
            return ResponseEntity.badRequest().body(discente.getNome() + " não é um discente.");
        }
        return ResponseEntity.ok(certificadoService.listarMeusCertificados((Discente) discente));
    }

    @PostMapping("/lote")
    public ResponseEntity<?> gerarLote(@RequestParam String tituloOportunidade,
                                       @RequestParam int cargaHoraria,
                                       @RequestParam String caminhoBase) {
        Oportunidade oportunidade = oportunidadeService.buscarPorTitulo(tituloOportunidade);
        if (oportunidade == null) {
            return ResponseEntity.status(404).body("Oportunidade '" + tituloOportunidade + "' não encontrada.");
        }
        List<Certificado> lote = certificadoService.solicitarGeracaoDeLote(oportunidade, cargaHoraria, caminhoBase);
        if (lote.isEmpty()) {
            return ResponseEntity.ok("Nenhum inscrito aprovado pra gerar certificado.");
        }
        return ResponseEntity.ok(lote);
    }

    @GetMapping("/validar/{hash}")
    public ResponseEntity<String> consultarAutenticidade(@PathVariable String hash) {
        boolean valido = certificadoService.consultarAutenticidadeNaUFMA(hash);
        return valido ? ResponseEntity.ok("Certificado válido!") :
                        ResponseEntity.status(404).body("Certificado não encontrado ou inválido.");
    }
}
