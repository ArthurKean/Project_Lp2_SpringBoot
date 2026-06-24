package com.ufma.project_lp2.service;


import com.ufma.project_lp2.model.Discente;
import com.ufma.project_lp2.model.Inscricao;
import com.ufma.project_lp2.model.Oportunidade;
import com.ufma.project_lp2.model.enums.StatusInscricao;
import org.jvnet.hk2.annotations.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InscricaoService {

    private List<Inscricao> bancoDeInscricoes;

    public InscricaoService() {
        this.bancoDeInscricoes = new ArrayList<>();
    }

    public Inscricao inscrever(Oportunidade oportunidade, Discente discente, String motivacao) {
        Inscricao novaInscricao = new Inscricao(oportunidade, discente, motivacao);
        bancoDeInscricoes.add(novaInscricao);

        oportunidade.getInscricoes().add(novaInscricao);
        
        System.out.println("Inscrição de '" + discente.getNome() + "' realizada com sucesso! Motivacao: " + motivacao);
        return novaInscricao;
    }

    public void cancelarInscricao(Inscricao inscricao) {
        if (inscricao != null) {
            inscricao.cancelar();
            System.out.println("A inscrição do aluno '" + inscricao.getDiscente().getNome() + "' foi cancelada.");
        } else {
            System.out.println("Inscrição não encontrada para cancelamento.");
        }
    }

    public void aprovarInscricao(Inscricao inscricao) {
        if (inscricao != null) {
            String dataAprovacao = LocalDate.now().toString();
            inscricao.aprovar(dataAprovacao);
            System.out.println("O coordenadorAPROVOU a inscrição de '" + inscricao.getDiscente().getNome() + "'.");
        }
    }

    public void rejeitarInscricao(Inscricao inscricao) {
        if (inscricao != null) {
            inscricao.rejeitar();
            System.out.println("A inscrição de '" + inscricao.getDiscente().getNome() + "' foi REJEITADA.");
        }
    }

    public void substituirParticipante(Inscricao inscricao, Discente novoDiscente) {
        if (inscricao != null && novoDiscente != null) {
            System.out.println("Substituindo o participante '" + inscricao.getDiscente().getNome() + "' por '" + novoDiscente.getNome() + "'.");
            inscricao.setDiscente(novoDiscente);
            inscricao.setStatus(StatusInscricao.PENDENTE);
        }
    }

    public List<Inscricao> listarInscritos(Oportunidade oportunidade) {
        System.out.println("LISTA DE INSCRITOS NA OPORTUNIDADE: " + oportunidade.getTitulo());
        List<Inscricao> filtradas = new ArrayList<>();
        
        for (Inscricao inscricao : bancoDeInscricoes) {
            if (inscricao.getOportunidade().equals(oportunidade)) {
                filtradas.add(inscricao);
                System.out.println("- Aluno: " + inscricao.getDiscente().getNome() + " | Status: " + inscricao.getStatus() + " | Motivação: " + inscricao.getMotivacao());
            }
        }
        return filtradas;
    }
}
