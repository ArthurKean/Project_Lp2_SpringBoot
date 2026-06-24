package com.ufma.project_lp2.model;


import com.ufma.project_lp2.model.enums.Cargos;
import com.ufma.project_lp2.model.enums.StatusGrupo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grupo {

    private String nome;
    private String tipo;
    private String email;
    private String descricao;
    private StatusGrupo status;
    private Docente responsavel;
    private LocalDate dataCriacao;
    
    private Map<Usuario, Cargos> membros;
    private List<String> historicoUsuarios;

    public Grupo(String nome, String tipo,
                 String email, String descricao,
                 Docente responsavel) {
        this.nome = nome;
        this.tipo = tipo;
        this.email = email;
        this.descricao = descricao;
        this.status = StatusGrupo.ATIVO;
        this.responsavel = responsavel;
        this.membros = new HashMap<>();
        this.historicoUsuarios = new ArrayList<>();
        this.dataCriacao = LocalDate.now();
    }

    public void adicionarMembro(Usuario membro) {
        if (membro != null && !this.membros.containsKey(membro)) {
            this.membros.put(membro, Cargos.MEMBRO); // Cargo padrão ao entrar
            this.historicoUsuarios.add("Membro " + membro.getNome() + " ingressou em " + LocalDate.now());
            System.out.println("Membro " + membro.getNome() + " foi adicionado com sucesso ao grupo " + nome + " como MEMBRO.");
        } else {
            System.out.println("Aviso: Esse aluno não pode ser adicionado ou já faz parte do grupo.");
        }
    }

    public void removerMembro(Usuario membro) {
        if (membros.containsKey(membro)) {
            membros.remove(membro);
            this.historicoUsuarios.add("Membro " + membro.getNome() + " saiu em " + LocalDate.now());
            System.out.println("Membro " + membro.getNome() + " removido do grupo " + nome);
        } else {
            System.out.println("Erro: Membro não encontrado no grupo.");
        }
    }
    
    public void atribuirCargo(Usuario membro, Cargos novoCargo) {
        if (membros.containsKey(membro)) {
            membros.put(membro, novoCargo);
            System.out.println("O cargo de " + membro.getNome() + " foi alterado para " + novoCargo);
        } else {
            System.out.println("Erro: O usuário não faz parte deste grupo.");
        }
    }

    public void listarMembros() {
        System.out.println("\nLista de Membros do grupo " + nome + " (" + membros.size() + "):");
        for (Map.Entry<Usuario, Cargos> entry : membros.entrySet()) {
            Usuario m = entry.getKey();
            Cargos c = entry.getValue();
            System.out.println("- " + m.getNome() + " | Cargo: " + c + " | Email: " + m.getEmail());
        }
    }

    public void exibirHistoricoUsuarios() {
        System.out.println("\nHistórico de Usuários do grupo " + nome + ":");
        if (historicoUsuarios.isEmpty()) {
            System.out.println("Nenhum histórico registrado.");
        } else {
            for (String registro : historicoUsuarios) {
                System.out.println("- " + registro);
            }
        }
    }

    public List<Usuario> getUsuariosRegistrados() {
        return new ArrayList<>(membros.keySet());
    }

    public void alterarStatus(StatusGrupo novoStatus) {
        this.status = novoStatus;
        System.out.println("O status do grupo '" + nome + "' foi alterado para: " + novoStatus);
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public StatusGrupo getStatus() { return status; }
    public void setStatus(StatusGrupo status) { this.status = status; }

    public Docente getResponsavel() { return responsavel; }
    public void setResponsavel(Docente responsavel) { this.responsavel = responsavel; }

    public Map<Usuario, Cargos> getMembros() { return membros; }
    public LocalDate getDataCriacao() { return dataCriacao; }

    @Override
    public String toString() {
        return "Grupo{" +
                "nome='" + nome + '\'' +
                ", tipo='" + tipo + '\'' +
                ", status=" + status +
                ", responsavel=" + (responsavel != null ? responsavel.getNome() : "null") +
                ", membros_qtd=" + membros.size() +
                ", dataCriacao=" + dataCriacao +
                '}';
    }
}