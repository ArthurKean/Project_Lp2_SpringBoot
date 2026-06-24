package com.ufma.project_lp2.model;


import com.ufma.project_lp2.model.enums.StatusMatricula;
import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Curso {

    private String nome;
    private int codigo;
    private int cargaHoraria;
    private String versaoPpc;
    private List<Discente> discentes;
    private List<String> historicoPpc;

    public Curso(String nome,
                 int codigo,
                 int cargaHoraria,
                 String versaoPpc) {
        this.nome = nome;
        this.codigo = codigo;
        this.cargaHoraria = cargaHoraria;
        this.versaoPpc = versaoPpc;
        this.discentes = new ArrayList<>();
        this.historicoPpc = new ArrayList<>();
    }

    public void atualizarPPC(int horas, String versao) {
        if (horas <= 0) {
            System.out.println("Carga horária inválida");
            return;
        }
        this.historicoPpc.add("Versão: " + this.versaoPpc + " (CH: " + this.cargaHoraria + "h) -> Nova Versão: " + versao + " (CH: " + horas + "h)");
        this.cargaHoraria = horas;
        this.versaoPpc = versao;
        System.out.println("PPC atualizado");
        System.out.println("Nova carga horária : " + this.cargaHoraria + " horas");
        System.out.println("Nova versao do PPC : " + this.versaoPpc);
    }

    public void exibirHistoricoPPC() {
        System.out.println("\nHistórico do PPC do curso " + nome + ":");
        if (historicoPpc.isEmpty()) {
            System.out.println("Nenhum histórico registrado.");
        } else {
            for (String registro : historicoPpc) {
                System.out.println("- " + registro);
            }
        }
    }

    public List<Discente> listarAlunosPorStatus(StatusMatricula status) {
        List<Discente> resultado = new ArrayList<>();

        if (status == null) {
            System.out.println("Status informado eh invalido");
            return resultado;
        }

        for (Discente discente : this.discentes) {
            if (discente.getStatusMatricula() == status) {
                resultado.add(discente);
            }
        }
        return resultado;
    }

    public void adicionarDiscente(Discente discente) {
        if (discente == null) {
            System.out.println("Discente invalido");
            return;
        }
        this.discentes.add(discente);
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCodigo() {
        return codigo;
    }
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }
    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public String getVersaoPpc() {
        return versaoPpc;
    }
    public void setVersaoPpc(String versaoPpc) {
        this.versaoPpc = versaoPpc;
    }

    public List<Discente> getDiscentes() {
        return discentes;
    }
    public void setDiscentes(List<Discente> discentes) {
        this.discentes = discentes;
    }

    @Override
    public String toString() {
        return "Curso{" +
                "nome='" + nome + '\'' +
                ", codigo=" + codigo +
                ", cargaHoraria=" + cargaHoraria +
                ", versaoPpc='" + versaoPpc + '\'' +
                ", totalDiscentes=" + discentes.size() +
                '}';
    }
}
