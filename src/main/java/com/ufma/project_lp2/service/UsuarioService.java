package com.ufma.project_lp2.service;


import com.ufma.project_lp2.model.Discente;
import com.ufma.project_lp2.model.Inscricao;
import com.ufma.project_lp2.model.Usuario;
import com.ufma.project_lp2.model.enums.StatusInscricao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import com.ufma.project_lp2.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    public Usuario registrarUsuario(Usuario usuario) {
        if (usuario != null) {
            Usuario salvo = repository.save(usuario);
            System.out.println("Usuario '" + usuario.getNome() + "' cadastrado no sistema com sucesso!");
            return salvo;
        } else {
            System.out.println("Tentativa de cadastrar um usuário que não pode!");
            return null;
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
        return repository.findByEmailIgnoreCase(email).orElse(null);
    }

    public Usuario buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
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

    public Usuario mudarSenha(String email, String novaSenha) {
        Usuario u = buscarPorEmail(email);
        if (u != null) {
            u.mudarSenha(novaSenha);
            Usuario salvo = repository.save(u);
            System.out.println("Senha do usuário '" + email + "' foi alterada com sucesso!");
            return salvo;
        } else {
            System.out.println("Usuário não encontrado para alterar senha!");
            return null;
        }
    }

    public Usuario desativarUsuario(String emailDoUsuario) {
        Usuario u = buscarPorEmail(emailDoUsuario);
        if (u != null) {
            u.setAtivo(false);
            Usuario salvo = repository.save(u);
            System.out.println("O perfil da conta '" + emailDoUsuario + "' foi desativado/suspenso no sistema!");
            return salvo;
        } else {
            System.out.println("Usuário não encontrado para desativação!");
            return null;
        }
    }

    @Transactional
    public String substituirParticipante(Inscricao atual, Inscricao nova, String justificativa) {
        if (atual == null || nova == null) {
            System.out.println("Erro: inscrições inválidas.");
            return "Erro: inscrições inválidas.";
        }
        if (!atual.getOportunidade().equals(nova.getOportunidade())) {
            System.out.println("Erro: inscrições de oportunidades diferentes.");
            return "Erro: inscrições de oportunidades diferentes.";
        }
        if (atual.getStatus() != StatusInscricao.APROVADO) {
            System.out.println("Erro: participante atual não está aprovado.");
            return "Erro: participante atual não está aprovado.";
        }

        atual.setStatus(StatusInscricao.CANCELADO);
        nova.setStatus(StatusInscricao.APROVADO);

        System.out.println("Substituição realizada com sucesso!");
        System.out.println("Removido: " + atual.getDiscente().getNome());
        System.out.println("Adicionado: " + nova.getDiscente().getNome());
        System.out.println("Justificativa: " + justificativa);
        return "Substituição realizada com sucesso!";
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
