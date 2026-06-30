package com.ufma.project_lp2.service;


import com.ufma.project_lp2.model.Discente;
import com.ufma.project_lp2.model.Inscricao;
import com.ufma.project_lp2.model.Usuario;
import com.ufma.project_lp2.model.enums.StatusInscricao;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import com.ufma.project_lp2.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    public void registrarUsuario(Usuario usuario) {
        if (usuario != null) {
            repository.save(usuario);
            System.out.println("Usuario '" + usuario.getNome() + "' cadastrado no sistema com sucesso!");
        } else {
            System.out.println("Tentativa de cadastrar um usuário que não pode!");
        }
    }

    public List<Usuario> listarUsuarios() {
        System.out.println("LISTA DE USUARIOS REGISTRADOS:");
        List<Usuario> todos = repository.findAll();
        for (Usuario u : todos) {
            System.out.println("Nome: " + u.getNome() + " | Email: " + u.getEmail() + " | Papel: " + u.getPapel());
        }
        return todos;
    }

    public Usuario buscarPorEmail(String email) {
        for (Usuario u : repository.findAll()) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }

    public Usuario realizarLogin(String email, String senha) {
        System.out.println("Consultando o banco de dados" + email + "'...");
        Usuario u = buscarPorEmail(email);
        
        if (u != null && u.getSenha().equals(senha)) {
            if (u.isAtivo()) {
                System.out.println("Login realizado com sucesso! Bem-vindo(a), " + u.getNome());
                return u;
            } else {
                System.out.println("Perfil de usuário desativado/suspenso.");
                return null;
            }
        }
        System.out.println("Credenciais invalidas.");
        return null;
    }

    public void mudarSenha(String email, String novaSenha) {
        Usuario u = buscarPorEmail(email);
        if (u != null) {
            u.mudarSenha(novaSenha);
            repository.save(u);
            System.out.println("Senha do usuário '" + email + "' foi alterada com sucesso!");
        } else {
            System.out.println("Usuário não encontrado para alterar senha!");
        }
    }

    public void desativarUsuario(String emailDoUsuario) {
        Usuario u = buscarPorEmail(emailDoUsuario);
        if (u != null) {
            u.setAtivo(false);
            repository.save(u);
            System.out.println("O perfil da conta '" + emailDoUsuario + "' foi desativado/suspenso no sistema!");
        } else {
            System.out.println("Usuário não encontrado para desativação!");
        }
    }

    public void substituirParticipante(Inscricao atual, Inscricao nova, String justificativa) {
        if (atual == null || nova == null) {
            System.out.println("Erro: inscrições inválidas.");
            return;
        }
        if (!atual.getOportunidade().equals(nova.getOportunidade())) {
            System.out.println("Erro: inscrições de oportunidades diferentes.");
            return;
        }
        if (atual.getStatus() != StatusInscricao.APROVADO) {
            System.out.println("Erro: participante atual não está aprovado.");
            return;
        }

        atual.setStatus(StatusInscricao.CANCELADO);
        nova.setStatus(StatusInscricao.APROVADO);

        System.out.println("Substituição realizada com sucesso!");
        System.out.println("Removido: " + atual.getDiscente().getNome());
        System.out.println("Adicionado: " + nova.getDiscente().getNome());
        System.out.println("Justificativa: " + justificativa);
    }

    public List<Discente> listarDiscentes() {
        System.out.println("LISTA APENAS DE DISCENTES:");
        List<Discente> discentes = new ArrayList<>();
        for (Usuario u : repository.findAll()) {
            if (u instanceof Discente) {
                Discente d = (Discente) u;
                discentes.add(d);
                System.out.println("Nome: " + d.getNome() + " | Matrícula: " + d.getMatricula() + " | Email: " + d.getEmail());
            }
        }
        return discentes;
    }
}
