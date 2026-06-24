package com.ufma.project_lp2.model;


import com.ufma.project_lp2.model.enums.StatusAproveitamento;
import jakarta.persistence.*;

import javax.annotation.processing.Generated;
import java.time.LocalDate;

@Entity
public class Aproveitamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "discente_id")
    private Discente discente;

    private String descricao;
    private String instituicao;
    private int horas;

    @Enumerated(EnumType.STRING)
    private StatusAproveitamento status;

    private String certificadoPath;

    @ManyToOne
    @JoinColumn(name = "avaliador_id")
    private Usuario avaliador;

    private String motivoRejeicao;
    private LocalDate dataSolicitacao;

    public Aproveitamento () {
    }

    public Aproveitamento(Discente discente, String descricao,
                          String instituicao, int horas) {
        this.discente = discente;
        this.descricao = descricao;
        this.instituicao = instituicao;
        this.horas = horas;
        this.status = StatusAproveitamento.PENDENTE; 
        this.dataSolicitacao = LocalDate.now();
    }

    public boolean uploadCertificado(String file) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        this.certificadoPath = file;
        return true;
    }

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }

    public Discente getDiscente() {
        return discente;
    }
    public void setDiscente(Discente discente) {
        this.discente = discente;
    }

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getInstituicao() {
        return instituicao;
    }
    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

    public int getHoras() {
        return horas;
    }
    public void setHoras(int horas) {
        this.horas = horas;
    }

    public StatusAproveitamento getStatus() {
        return status;
    }
    public void setStatus(StatusAproveitamento status) {
        this.status = status;
    }

    public String getCertificado_path() {
        return certificadoPath;
    }
    public void setCertificado_path(String certificado_path) {
        this.certificadoPath = certificado_path;
    }

    public Usuario getAvaliador() {
        return avaliador;
    }
    public void setAvaliador(Usuario avaliador) {
        this.avaliador = avaliador;
    }

    public String getMotivo_rejeicao() {
        return motivoRejeicao;
    }
    public void setMotivo_rejeicao(String motivo_rejeicao) {
        this.motivoRejeicao = motivo_rejeicao;
    }

    public LocalDate getDataSolicitacao() {
        return dataSolicitacao;
    }
    public void setDataSolicitacao(LocalDate dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }
}
