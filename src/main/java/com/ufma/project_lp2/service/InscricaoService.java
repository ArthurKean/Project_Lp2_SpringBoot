package com.ufma.project_lp2.service;


import com.ufma.project_lp2.model.Discente;
import com.ufma.project_lp2.model.Inscricao;
import com.ufma.project_lp2.model.Oportunidade;
import com.ufma.project_lp2.model.enums.StatusInscricao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import com.ufma.project_lp2.repository.InscricaoRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class InscricaoService {

    @Autowired
    private InscricaoRepository repository;

    public Inscricao inscrever(Oportunidade oportunidade, Discente discente, String motivacao) {
        Inscricao novaInscricao = new Inscricao(oportunidade, discente, motivacao);
        repository.save(novaInscricao);

        oportunidade.getInscricoes().add(novaInscricao);
        
        System.out.println("Inscrição de '" + discente.getNome() + "' realizada com sucesso! Motivacao: " + motivacao);
        return novaInscricao;
    }

    public Inscricao cancelarInscricao(Inscricao inscricao) {
        if (inscricao != null) {
            inscricao.cancelar();
            Inscricao salvo = repository.save(inscricao);
            System.out.println("A inscrição do aluno '" + inscricao.getDiscente().getNome() + "' foi cancelada.");
            return salvo;
        } else {
            System.out.println("Inscrição não encontrada para cancelamento.");
            return null;
        }
    }

    public Inscricao aprovarInscricao(Inscricao inscricao) {
        if (inscricao != null) {
            String dataAprovacao = LocalDate.now().toString();
            inscricao.aprovar(dataAprovacao);
            Inscricao salvo = repository.save(inscricao);
            System.out.println("O coordenadorAPROVOU a inscrição de '" + inscricao.getDiscente().getNome() + "'.");
            return salvo;
        }
        return null;
    }

    public Inscricao rejeitarInscricao(Inscricao inscricao) {
        if (inscricao != null) {
            inscricao.rejeitar();
            Inscricao salvo = repository.save(inscricao);
            System.out.println("A inscrição de '" + inscricao.getDiscente().getNome() + "' foi REJEITADA.");
            return salvo;
        }
        return null;
    }

    public Inscricao substituirParticipante(Inscricao inscricao, Discente novoDiscente) {
        if (inscricao != null && novoDiscente != null) {
            System.out.println("Substituindo o participante '" + inscricao.getDiscente().getNome() + "' por '" + novoDiscente.getNome() + "'.");
            inscricao.setDiscente(novoDiscente);
            inscricao.setStatus(StatusInscricao.PENDENTE);
            return repository.save(inscricao);
        }
        return null;
    }

    public Inscricao buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Inscricao> listarInscritos(Oportunidade oportunidade) {
        System.out.println("LISTA DE INSCRITOS NA OPORTUNIDADE: " + oportunidade.getTitulo());
        List<Inscricao> filtradas = new ArrayList<>();
        
        for (Inscricao inscricao : repository.findAll()) {
            if (inscricao.getOportunidade() != null && inscricao.getOportunidade().getId() != null && inscricao.getOportunidade().getId().equals(oportunidade.getId())) {
                filtradas.add(inscricao);
                System.out.println("- Aluno: " + inscricao.getDiscente().getNome() + " | Status: " + inscricao.getStatus() + " | Motivação: " + inscricao.getMotivacao());
            }
        }
        return filtradas;
    }
}
