package com.ufma.project_lp2.model;


import com.ufma.project_lp2.model.enums.StatusInscricao;

public class Inscricao {

    private Oportunidade oportunidade;
    private Discente discente;
    private StatusInscricao status;
    private String motivacao;

    public Inscricao(Oportunidade oportunidade, Discente discente, String motivacao) {
        this.oportunidade = oportunidade;
        this.discente = discente;
        this.motivacao = motivacao;
        this.status = StatusInscricao.PENDENTE;
    }

    public void aprovar(String data) {
        this.status = StatusInscricao.APROVADO;
        System.out.println("Inscrição aprovada em: " + data);
    }

    public void rejeitar() {
        this.status = StatusInscricao.REJEITADO;
        System.out.println("Inscrição rejeitada.");
    }

    public Oportunidade getOportunidade() {
        return oportunidade;
    }

    public Discente getDiscente() {
        return discente;
    }

    public StatusInscricao getStatus() {
        return status;
    }

    public String getMotivacao() {
        return motivacao;
    }

    public void setMotivacao(String motivacao) {
        this.motivacao = motivacao;
    }

    public void setStatus(StatusInscricao status) {
        this.status = status;
    }

    public void cancelar() {
        this.status = StatusInscricao.CANCELADO;
        System.out.println("Inscrição cancelada.");
    }

    public void setDiscente(Discente discente) {
        this.discente = discente;
    }
}
