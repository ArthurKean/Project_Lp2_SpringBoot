package com.ufma.project_lp2.model;


import com.ufma.project_lp2.model.enums.Modalidade;
import com.ufma.project_lp2.model.enums.Papel;
import com.ufma.project_lp2.model.enums.TipoOportunidade;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

@Entity
public class DiscenteDiretor extends Discente {

    @ManyToOne
    @JoinColumn(name = "grupo_id")
    private Grupo grupo;

    private String cargo;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public DiscenteDiretor(){
    }

    public DiscenteDiretor(String nome, String email, String senha,
                           Papel papel, String matricula, int semestre_atual,
                           Curso curso, Grupo grupo, String cargo,
                           LocalDate dataInicio, LocalDate dataFim) {
        super(nome, email, senha, papel, matricula, semestre_atual, curso);
        this.grupo = grupo;
        this.cargo = cargo;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public Oportunidade criarOportunidade(String titulo, String descricao,
                                          TipoOportunidade tipo, Modalidade modalidade,
                                          int carga_horaria, int vagas,
                                          LocalDate inicio, LocalDate fim,
                                          Docente responsavel) {
        return new Oportunidade(titulo, descricao, tipo, modalidade, 
                                carga_horaria, vagas, inicio, fim, this, responsavel);
    }

    public Grupo getGrupo() {
        return grupo;
    }
    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public String getCargo() {
        return cargo;
    }
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public LocalDate getData_inicio() {
        return dataInicio;
    }
    public void setData_inicio(LocalDate data_inicio) {
        this.dataInicio = data_inicio;
    }

    public LocalDate getData_fim() {
        return dataFim;
    }
    public void setData_fim(LocalDate data_fim) {
        this.dataFim = data_fim;
    }

    @Override
    public void exibirPerfil() {
        String nomeGrupo = "N/A";
        if (grupo != null) {
            nomeGrupo = grupo.getNome();
        }
        System.out.println("Perfil de Discente Diretor: " + getNome() + " | Matrícula: " + getMatricula() + " | Cargo: " + cargo + " do Grupo " + nomeGrupo);
    }
}
