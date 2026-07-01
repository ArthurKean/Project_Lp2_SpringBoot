package com.ufma.project_lp2.service;


import com.ufma.project_lp2.model.Certificado;
import com.ufma.project_lp2.model.Discente;
import com.ufma.project_lp2.model.Inscricao;
import com.ufma.project_lp2.model.Oportunidade;
import com.ufma.project_lp2.model.enums.StatusInscricao;
import org.springframework.stereotype.Service;


import org.springframework.beans.factory.annotation.Autowired;
import com.ufma.project_lp2.repository.CertificadoRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CertificadoService {

    @Autowired
    private CertificadoRepository repository;

    public Certificado guardarRegistroDeCertificadoOficial(Certificado certificado) {
        if (certificado != null) {
            Certificado salvo = repository.save(certificado);
            System.out.println("Certificado de '" + certificado.getDiscente().getNome() + "' arquivado com sucesso no cartório institucional.");
            return salvo;
        }
        return null;
    }

    public List<Certificado> listarMeusCertificados(Discente alunoLogado) {
        System.out.println("MEUS CERTIFICADOS (ALUNO: " + alunoLogado.getNome() + "):");
        List<Certificado> meusCertificados = repository.findByDiscenteId(alunoLogado.getId());
        for (Certificado c : meusCertificados) {
            System.out.println("- Documento: " + c.getOportunidade().getTitulo() + " | Validação: " + c.getUuidHash());
        }
        return meusCertificados;
    }

    public List<Certificado> solicitarGeracaoDeLote(Oportunidade oportunidadeConcluida, int cargaHoraria, String caminhoBase) {
        System.out.println("\nGERANDO CERTIFICADOS: Evento '" + oportunidadeConcluida.getTitulo() + "'...");
        List<Certificado> lote = new ArrayList<>();

        for (Inscricao inscricao : oportunidadeConcluida.getInscricoes()) {
            if (inscricao.getStatus() == StatusInscricao.APROVADO) {
                String pathDoc = caminhoBase + "/cert_" + inscricao.getDiscente().getMatricula() + ".pdf";

                Certificado cert = new Certificado(inscricao.getDiscente(), oportunidadeConcluida, cargaHoraria, pathDoc);

                repository.save(cert);
                lote.add(cert);
                
                System.out.println("- Certificado emitido para: " + inscricao.getDiscente().getNome() + " | Hash: " + cert.getUuidHash());
            }
        }
        System.out.println("Total de certificados emitidas: " + lote.size());
        return lote;
    }

    public boolean consultarAutenticidadeNaUFMA(String codigoDeHash) {
        System.out.println("\nSISTEMA DE VALIDAÇÃO UFMA - Consultando: " + codigoDeHash);
        return repository.findByUuidHash(codigoDeHash)
                .map(c -> {
                    System.out.println("Certificado VÁLIDADDO > Pertence ao aluno: " + c.getDiscente().getNome());
                    c.gerarQRCode();
                    repository.save(c);
                    return true;
                })
                .orElseGet(() -> {
                    System.out.println("Certificado FALSO ou não encontrado no sistema.");
                    return false;
                });
    }
}
