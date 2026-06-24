package com.ufma.project_lp2.service;


import com.ufma.project_lp2.model.Certificado;
import com.ufma.project_lp2.model.Discente;
import com.ufma.project_lp2.model.Inscricao;
import com.ufma.project_lp2.model.Oportunidade;
import com.ufma.project_lp2.model.enums.StatusInscricao;
import org.jvnet.hk2.annotations.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CertificadoService {

    private List<Certificado> cartorioInstitucional;

    public CertificadoService() {
        this.cartorioInstitucional = new ArrayList<>();
    }

    public void guardarRegistroDeCertificadoOficial(Certificado certificado) {
        if (certificado != null) {
            cartorioInstitucional.add(certificado);
            System.out.println("Certificado de '" + certificado.getDiscente().getNome() + "' arquivado com sucesso no cartório institucional.");
        }
    }

    public List<Certificado> listarMeusCertificados(Discente alunoLogado) {
        System.out.println("MEUS CERTIFICADOS (ALUNO: " + alunoLogado.getNome() + "):");
        List<Certificado> meusCertificados = new ArrayList<>();
        
        for (Certificado c : cartorioInstitucional) {
            if (c.getDiscente() != null && c.getDiscente().equals(alunoLogado)) {
                meusCertificados.add(c);
                System.out.println("- Documento: " + c.getOportunidade().getTitulo() + " | Validação: " + c.getUuidHash());
            }
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

                cartorioInstitucional.add(cert);
                lote.add(cert);
                
                System.out.println("- Certificado emitido para: " + inscricao.getDiscente().getNome() + " | Hash: " + cert.getUuidHash());
            }
        }
        System.out.println("Total de certificados emitidos: " + lote.size());
        return lote;
    }

    public boolean consultarAutenticidadeNaUFMA(String codigoDeHash) {
        System.out.println("\nSISTEMA DE VALIDAÇÃO UFMA - Consultando: " + codigoDeHash);
        for (Certificado c : cartorioInstitucional) {
            if (c.getUuidHash().equals(codigoDeHash)) {
                System.out.println("Certificado VÁLIDO. Pertence ao aluno: " + c.getDiscente().getNome());
                c.gerarQRCode();
                return true;
            }
        }
        System.out.println("Certificado FALSO ou não encontrado no sistema.");
        return false;
    }
}
