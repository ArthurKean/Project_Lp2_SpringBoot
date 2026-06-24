package com.ufma.project_lp2.model;



import com.ufma.project_lp2.model.enums.Modalidade;
import com.ufma.project_lp2.model.enums.StatusInscricao;
import com.ufma.project_lp2.model.enums.StatusOportunidade;
import com.ufma.project_lp2.model.enums.TipoOportunidade;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Oportunidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descricao;

    @Enumerated(EnumType.STRING)
    private TipoOportunidade tipo;

    @Enumerated(EnumType.STRING)
    private Modalidade modalidade;

    private int carga_horaria;
    private int vagas;

    @Enumerated(EnumType.STRING)
    private StatusOportunidade status;

    private LocalDate inicio;
    private LocalDate fim;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Usuario autor;

    @ManyToOne
    @JoinColumn(name = "responsavel_id")
    private Docente responsavel;

    private String planoAtividades;

    @OneToMany(mappedBy = "oportunidade")
    private List<Inscricao> inscricoes;

    public Oportunidade(){
    }

    public Oportunidade(String titulo, String descricao,
                        TipoOportunidade tipo, Modalidade modalidade,
                        int carga_horaria, int vagas,
                        LocalDate inicio, LocalDate fim,
                        Usuario autor, Docente responsavel) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipo = tipo;
        this.modalidade = modalidade;
        this.carga_horaria = carga_horaria;
        this.vagas = vagas;
        this.inicio = inicio;
        this.fim = fim;
        this.autor = autor;
        this.responsavel = responsavel;
        this.status = StatusOportunidade.RASCUNHO;
        this.inscricoes = new ArrayList<>();
    }


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoOportunidade getTipo() {
        return tipo;
    }
    public Modalidade getModalidade() {
        return modalidade;
    }

    public int getCarga_horaria() {
        return carga_horaria;
    }

    public int getVagas() {
        return vagas;
    }
    public void setVagas(int vagas) {
        this.vagas = vagas;
    }

    public StatusOportunidade getStatus() {
        return status;
    }
    public void setStatus(StatusOportunidade status) {
        this.status = status;
    }

    public LocalDate getInicio() {
        return inicio;
    }
    public LocalDate getFim() {
        return fim;
    }

    public Usuario getAutor() {
        return autor;
    }

    public Docente getResponsavel() {
        return responsavel;
    }
    public void setResponsavel(Docente responsavel) {
        this.responsavel = responsavel;
    }

    public String getPlanoAtividades() {
        return planoAtividades;
    }
    public void setPlanoAtividades(String planoAtividades) {
        this.planoAtividades = planoAtividades;
    }

    public List<Inscricao> getInscricoes() {
        return inscricoes;
    }

    public void publicar() {
        if (status != StatusOportunidade.RASCUNHO && status != StatusOportunidade.AGUARDANDO_APROVACAO){
            System.out.println("ERRO! Só pode publicar se for Rascunho ou Aguardando Aprovaçao");
            return;
        }
        this.status = StatusOportunidade.PUBLICADA;
    }

    public void fecharInscricoes(){
        if (status != StatusOportunidade.ABERTA){
            System.out.println("EERO! Só dá pra fechar a inscrição se o status for aberta");
            return;
        }
        this.status = StatusOportunidade.EM_EXECUCAO;
    }

    public void encerrar(){
        if (status == StatusOportunidade.ENCERRADA){
            System.out.println("Essa oportunidade já acabou antes.");
            return;
        }
        this.status = StatusOportunidade.ENCERRADA;
    }

    public boolean temVagasDisponiveis(){
        long aprovadas = 0;
        for (Inscricao i : inscricoes){
            if (i.getStatus() == StatusInscricao.APROVADO){
                aprovadas++;
            }
        }
        return aprovadas < vagas;
    }

    @Override
    public String toString() {
        return "Oportunidade{" +
                "titulo='" + titulo + '\'' +
                ", tipo=" + tipo +
                ", modalidade=" + modalidade +
                ", carga_horaria=" + carga_horaria +
                ", vagas=" + vagas +
                ", status=" + status +
                ", inicio=" + inicio +
                ", fim=" + fim +
                '}';
    }
}
