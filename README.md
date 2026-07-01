# Sistema de Gestao de Extensao Universitaria (LP2)

Este projeto foi desenvolvido como trabalho pratico para a disciplina de Linguagem de Programacao II (LP2). Trata-se de uma API REST desenvolvida em Spring Boot para gerenciar e registrar atividades de extensao academica na universidade, contemplando o fluxo de cursos, grupos, oportunidades (como bolsas), inscricoes, emissao de certificados oficiais e aproveitamento de horas.

---

## 👥 Membros do Grupo
* Chrysthyan Goncalves
* Wesley Paiva
* Kaua Almeida
* Arthur Kean

---

## 🛠️ Pre-requisitos Tecnicos
Para compilar e rodar a aplicacao na sua maquina, voce vai precisar de:
* **Java SDK 21** (o projeto foi desenvolvido e testado utilizando a JDK 21).
* Uma IDE de sua preferencia com suporte a projetos Maven (sugerimos o IntelliJ IDEA ou Visual Studio Code com o Extension Pack para Java).
* Banco de Dados: O projeto usa o banco em memoria **H2**, logo nao e necessario configurar nenhum servidor de banco de dados local.

---

## 🚀 Como Iniciar a Aplicacao

### Opcao 1: Pela sua IDE (Recomendado para Avaliacao)
1. Importe a pasta do projeto como um projeto Maven na sua IDE.
2. Aguarde o download das dependencias listadas no arquivo `pom.xml`.
3. Localize a classe principal em `src/main/java/com/ufma/project_lp2/ProjectLp2Application.java`.
4. Clique com o botao direito e selecione a opcao **Run** para iniciar o servidor.
5. O servidor iniciara na porta padrao: `http://localhost:8080`.

### Opcao 2: Pelo Terminal
Se voce tiver o Maven configurado no seu PATH, basta rodar na pasta raiz:
```bash
mvn spring-boot:run
```

---
