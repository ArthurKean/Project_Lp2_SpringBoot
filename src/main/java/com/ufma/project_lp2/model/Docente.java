package com.ufma.project_lp2.model;


import com.ufma.project_lp2.model.enums.Modalidade;
import com.ufma.project_lp2.model.enums.Papel;
import com.ufma.project_lp2.model.enums.TipoOportunidade;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
public class Docente extends Usuario {

    private String siape;
    private String departamento;

    public Docente(){
    }

    public Docente(String nome, String email,
            String senha, Papel papel,
            String siape, String departamento) {
        super(nome, email, senha, papel);
        this.siape = siape;
        this.departamento = departamento;
    }

    public Oportunidade criarOportunidade(String titulo, String descricao,
                                          TipoOportunidade tipo, Modalidade modalidade,
                                          int carga_horaria, int vagas,
                                          LocalDate inicio, LocalDate fim,
                                          Docente responsavel) {
        return new Oportunidade(titulo, descricao, tipo, modalidade,
                carga_horaria, vagas, inicio, fim, this, responsavel);
    }

    public void registrarPlanoAtividades(Oportunidade oportunidade, String data) {
        System.out.println("Plano de atividades registradaa para a oportunidade em " + data);
    }

    public String getSiape() {
        return siape;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setSiape(String siape) {
        this.siape = siape;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    @Override
    public String toString() {
        return super.toString() +
                "siape='" + siape + '\'' +
                ", departamento='" + departamento + '\'' +
                '}';
    }

    @Override
    public void exibirPerfil() {
        System.out.println("Perfil de Docente: " + getNome() + " | SIAPE: " + siape + " | Departamento: " + departamento);
    }
}
