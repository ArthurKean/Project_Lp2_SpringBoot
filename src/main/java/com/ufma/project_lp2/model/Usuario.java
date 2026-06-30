package com.ufma.project_lp2.model;


import com.ufma.project_lp2.model.enums.Papel;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String senha;

    @Enumerated(EnumType.STRING)
    private Papel papel;

    private boolean ativo = true;

    public Usuario(){
    }

    public Usuario(String nome,
                   String email,
                   String senha,
                   Papel papel) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.papel = papel;
        this.ativo = true;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Papel getPapel() {
        return papel;
    }
    public void setPapel(Papel papel) {
        this.papel = papel;
    }

    public boolean isAtivo() {
        return ativo;
    }
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getSenha() {
        return senha;
    }



    public void mudarSenha(String novaSenha) {
        if (novaSenha == null || novaSenha.length() < 8){
            System.out.println("Deu errado: A senha precisa ter pelo menos 8 caracteres!");
            return;
        }
        this.senha = novaSenha;
    }

    public List<Oportunidade> obterOportunidade(List<Oportunidade> todas) {
        List<Oportunidade> resultado = new ArrayList<>();
        for (Oportunidade op: todas){
            if(op.getAutor() != null && op.getAutor().equals(this)){
                resultado.add(op);
            }
        }
        return resultado;
    }

    public abstract void exibirPerfil();

    @Override
    public String toString() {
        return "Usuario{" +
                "nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", papel=" + papel +
                ", ativo=" + ativo +
                '}';
    }
}
