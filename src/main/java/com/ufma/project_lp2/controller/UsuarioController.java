package com.ufma.project_lp2.controller;

import com.ufma.project_lp2.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ufma.project_lp2.model.Usuario;
import com.ufma.project_lp2.model.Discente;
import com.ufma.project_lp2.model.Docente;
import com.ufma.project_lp2.model.DiscenteDiretor;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/discente")
    public Usuario registrarDiscente(@RequestBody Discente discente) {
        return usuarioService.registrarUsuario(discente);
    }

    @PostMapping("/docente")
    public Usuario registrarDocente(@RequestBody Docente docente) {
        return usuarioService.registrarUsuario(docente);
    }

    @PostMapping("/diretor")
    public Usuario registrarDiretor(@RequestBody DiscenteDiretor diretor) {
        return usuarioService.registrarUsuario(diretor);
    }

    @GetMapping
    public List<Usuario> listarTodos() {
        return usuarioService.listarUsuarios();
    }

    @GetMapping("/discentes")
    public List<Discente> listarDiscentes() {
        return usuarioService.listarDiscentes();
    }

    @GetMapping("/{email}")
    public Usuario buscarPorEmail(@PathVariable String email) {
        return usuarioService.buscarPorEmail(email);
    }

    @PostMapping("/login")
    public Usuario login(@RequestParam String email, @RequestParam String senha) {
        return usuarioService.realizarLogin(email, senha);
    }

    @PutMapping("/{email}/senha")
    public Usuario mudarSenha(@PathVariable String email, @RequestParam String novaSenha) {
        return usuarioService.mudarSenha(email, novaSenha);
    }

    @DeleteMapping("/{email}")
    public Usuario desativarUsuario(@PathVariable String email) {
        return usuarioService.desativarUsuario(email);
    }
}
