package com.ufma.project_lp2.model;


import com.ufma.project_lp2.model.enums.StatusInscricao;
import jakarta.persistence.*;

@Entity
public class Inscricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "oportunidade_id")
    private Oportunidade oportunidade;

    @ManyToOne
    @JoinColumn(name = "discente_id")
    private Discente discente;

    @Enumerated(EnumType.STRING)
    private StatusInscricao status;

    private String motivacao;

    public Inscricao(){
    }

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

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
