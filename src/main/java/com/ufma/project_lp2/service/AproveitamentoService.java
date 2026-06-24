package com.ufma.project_lp2.service;


import com.ufma.project_lp2.model.Aproveitamento;
import com.ufma.project_lp2.model.Discente;
import com.ufma.project_lp2.model.Usuario;
import com.ufma.project_lp2.model.enums.StatusAproveitamento;
import org.jvnet.hk2.annotations.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class AproveitamentoService {

    private List<Aproveitamento> bancoDeSolicitacoes;

    public AproveitamentoService() {
        this.bancoDeSolicitacoes = new ArrayList<>();
    }

    public void registrarAproveitamento(Aproveitamento solicitacao) {
        if (solicitacao != null) {
            bancoDeSolicitacoes.add(solicitacao);
            System.out.println("A solicitação de aproveitamento de '" + solicitacao.getDiscente().getNome() + "' foi recebida e está PENDENTE.");
        } else {
            System.out.println("Erro: Requerimento inválido.");
        }
    }

    public List<Aproveitamento> listarPendentes() {
        System.out.println("PAINEL DE APROVEITAMENTOS (PENDENTES):");
        List<Aproveitamento> pendentes = new ArrayList<>();
        
        for (Aproveitamento ap : bancoDeSolicitacoes) {
            if (ap.getStatus() == StatusAproveitamento.PENDENTE) {
                pendentes.add(ap);
                System.out.println("- Aluno: " + ap.getDiscente().getNome() + " | Horas: " + ap.getHoras() + "h | Data: " + ap.getDataSolicitacao());
            }
        }
        return pendentes;
    }

    public boolean aprovarSolicitacao(Aproveitamento aproveitamento, Usuario avaliador) {
        if (aproveitamento == null || aproveitamento.getStatus() != StatusAproveitamento.PENDENTE) {
            System.out.println("Erro: Aproveitamento não encontrado ou não está PENDENTE.");
            return false;
        }

        aproveitamento.setStatus(StatusAproveitamento.APROVADO);
        aproveitamento.setAvaliador(avaliador);
        System.out.println("Aproveitamento de " + aproveitamento.getHoras() + "h aprovado para '" + aproveitamento.getDiscente().getNome() + "' pelo avaliador " + avaliador.getNome());
        return true;
    }

    public int calcularHorasAprovadas(Discente discente) {
        int total = 0;
        for (Aproveitamento ap : bancoDeSolicitacoes) {
            if (ap.getDiscente().equals(discente) && ap.getStatus() == StatusAproveitamento.APROVADO) {
                total += ap.getHoras();
            }
        }
        System.out.println("Total de horas extracurriculares validadas para " + discente.getNome() + ": " + total + " horas.");
        return total;
    }

    public boolean rejeitarSolicitacao(Aproveitamento aproveitamento, Usuario avaliador, String motivo) {
        if (aproveitamento == null || aproveitamento.getStatus() != StatusAproveitamento.PENDENTE) {
            System.out.println("Erro: Aproveitamento não encontrado ou não está PENDENTE.");
            return false;
        }
        if (motivo == null || motivo.trim().isEmpty()) {
            System.out.println("Erro: O motivo da rejeição é obrigatório.");
            return false;
        }

        aproveitamento.setStatus(StatusAproveitamento.INDEFERIDO);
        aproveitamento.setAvaliador(avaliador);
        aproveitamento.setMotivo_rejeicao(motivo);
        System.out.println("Aproveitamento do aluno '" + aproveitamento.getDiscente().getNome() + "' foi REJEITADO. Motivo: " + motivo);
        return true;
    }

    public boolean reenviarSolicitacao(Aproveitamento aproveitamento, String novoCertificadoPath) {
        if (aproveitamento != null && aproveitamento.getStatus() == StatusAproveitamento.INDEFERIDO) {
            aproveitamento.setCertificado_path(novoCertificadoPath);
            aproveitamento.setStatus(StatusAproveitamento.PENDENTE);
            aproveitamento.setMotivo_rejeicao(null);
            aproveitamento.setDataSolicitacao(LocalDate.now());
            System.out.println("A solicitação do aluno '" + aproveitamento.getDiscente().getNome() + "' foi atualizada e reenviada para análise.");
            return true;
        }
        System.out.println("Erro: Apenas solicitações REJEITADAS podem ser reenviadas.");
        return false;
    }

    public void verificarPrazo(Aproveitamento aproveitamento) {
        if (aproveitamento != null && aproveitamento.getStatus() == StatusAproveitamento.PENDENTE) {
            long diasPendentes = ChronoUnit.DAYS.between(aproveitamento.getDataSolicitacao(), LocalDate.now());
            if (diasPendentes > 15) {
                System.out.println("ALERTA: O requerimento de '" + aproveitamento.getDiscente().getNome() + "' está aguardando avaliação há " + diasPendentes + " dias! Passou do prazo de 15 dias.");
            } else {
                System.out.println("O requerimento está dentro do prazo normal de avaliação (" + diasPendentes + " dias corridos).");
            }
        }
    }
}
