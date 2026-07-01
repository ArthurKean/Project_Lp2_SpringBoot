$ErrorActionPreference = "Stop"

Write-Host "===================================================" -ForegroundColor Cyan
Write-Host "  🚀 BATERIA COMPLETA DE TESTES (ALL ENDPOINTS) 🚀 " -ForegroundColor Cyan
Write-Host "===================================================" -ForegroundColor Cyan
Write-Host ""

try {
    # ------------------ FASE 1: BASE ------------------
    Write-Host "[1/20] Criando Curso..." -NoNewline
    $curso = Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/cursos" -ContentType "application/json" -Body '{"codigo":123,"nome":"Engenharia","cargaHoraria":3200,"versaoPpc":"2023.1"}'
    Write-Host " OK ✅" -ForegroundColor Green

    Write-Host "[2/20] Criando Usuários (Docente, Discente Ana, Discente Joao, Diretor)..." -NoNewline
    Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/usuarios/docente" -ContentType "application/json" -Body '{"nome":"Carlos Silva","email":"carlos@ufma.br","senha":"123","papel":"DOCENTE","siape":"99"}' | Out-Null
    Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/usuarios/discente" -ContentType "application/json" -Body '{"nome":"Ana","email":"ana@ufma.br","senha":"123","papel":"DISCENTE","matricula":"2022","semestreAtual":4}' | Out-Null
    Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/usuarios/discente" -ContentType "application/json" -Body '{"nome":"Joao","email":"joao@ufma.br","senha":"123","papel":"DISCENTE","matricula":"2023","semestreAtual":2}' | Out-Null
    Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/usuarios/diretor" -ContentType "application/json" -Body '{"nome":"Diretor","email":"diretor@ufma.br","senha":"123","papel":"DISCENTE_DIRETOR","matricula":"11","semestreAtual":8}' | Out-Null
    Write-Host " OK ✅" -ForegroundColor Green

    Write-Host "[3/20] Matriculando Alunos..." -NoNewline
    Invoke-RestMethod -Method Put -Uri "http://localhost:8080/api/cursos/123/matricular?emailDiscente=ana@ufma.br" | Out-Null
    Invoke-RestMethod -Method Put -Uri "http://localhost:8080/api/cursos/123/matricular?emailDiscente=joao@ufma.br" | Out-Null
    Write-Host " OK ✅" -ForegroundColor Green

    Write-Host "[4/20] Atualizando PPC do Curso..." -NoNewline
    Invoke-RestMethod -Method Put -Uri "http://localhost:8080/api/cursos/123/ppc?novasCargaHoraria=3400&novaVersao=2024.1" | Out-Null
    Write-Host " OK ✅" -ForegroundColor Green

    Write-Host "[5/20] Lendo Histórico do PPC..." -NoNewline
    Invoke-RestMethod -Method Get -Uri "http://localhost:8080/api/cursos/123/historico-ppc" | Out-Null
    Write-Host " OK ✅" -ForegroundColor Green

    # ------------------ FASE 2: GRUPOS ------------------
    Write-Host "[6/20] Criando e Aprovando Grupo LAPPIS..." -NoNewline
    Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/grupos" -ContentType "application/json" -Body '{"nome":"LAPPIS","tipo":"PESQUISA","responsavel":{"id":1}}' | Out-Null
    Invoke-RestMethod -Method Put -Uri "http://localhost:8080/api/grupos/LAPPIS/aprovar" | Out-Null
    Write-Host " OK ✅" -ForegroundColor Green

    Write-Host "[7/20] Adicionando Ana e Joao no Grupo..." -NoNewline
    Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/grupos/LAPPIS/membros/ana@ufma.br" | Out-Null
    Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/grupos/LAPPIS/membros/joao@ufma.br" | Out-Null
    Write-Host " OK ✅" -ForegroundColor Green

    Write-Host "[8/20] Promovendo Ana a VICE..." -NoNewline
    Invoke-RestMethod -Method Put -Uri "http://localhost:8080/api/grupos/LAPPIS/cargos?emailDiscente=ana@ufma.br&cargo=VICE&emailDocente=carlos@ufma.br" | Out-Null
    Write-Host " OK ✅" -ForegroundColor Green

    Write-Host "[9/20] Removendo Joao do Grupo e Lendo Histórico..." -NoNewline
    Invoke-RestMethod -Method Delete -Uri "http://localhost:8080/api/grupos/LAPPIS/membros/joao@ufma.br" | Out-Null
    Invoke-RestMethod -Method Get -Uri "http://localhost:8080/api/grupos/LAPPIS/historico" | Out-Null
    Write-Host " OK ✅" -ForegroundColor Green

    # ------------------ FASE 3: OPORTUNIDADES E INSCRICOES ------------------
    Write-Host "[10/20] Criando PIBIC..." -NoNewline
    Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/oportunidades?emailAutor=carlos@ufma.br" -ContentType "application/json" -Body '{"titulo":"PIBIC","tipo":"PROJETO","modalidade":"PRESENCIAL","vagas":2}' | Out-Null
    Write-Host " OK ✅" -ForegroundColor Green

    Write-Host "[11/20] Divulgando e Aprovando PIBIC..." -NoNewline
    Invoke-RestMethod -Method Put -Uri "http://localhost:8080/api/oportunidades/1/divulgar?emailRequisitante=carlos@ufma.br" | Out-Null
    Invoke-RestMethod -Method Put -Uri "http://localhost:8080/api/oportunidades/1/aprovar?emailAvaliador=carlos@ufma.br" | Out-Null
    Write-Host " OK ✅" -ForegroundColor Green

    Write-Host "[12/20] Ana e Joao se Inscrevendo..." -NoNewline
    Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/inscricoes?tituloOportunidade=PIBIC&emailDiscente=ana@ufma.br&motivacao=Teste1" | Out-Null
    Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/inscricoes?tituloOportunidade=PIBIC&emailDiscente=joao@ufma.br&motivacao=Teste2" | Out-Null
    Write-Host " OK ✅" -ForegroundColor Green

    Write-Host "[13/20] Aprovando Ana, Cancelando Joao..." -NoNewline
    Invoke-RestMethod -Method Put -Uri "http://localhost:8080/api/inscricoes/1/aprovar" | Out-Null
    Invoke-RestMethod -Method Put -Uri "http://localhost:8080/api/inscricoes/2/cancelar" | Out-Null
    Write-Host " OK ✅" -ForegroundColor Green

    Write-Host "[14/20] Encerrando Oportunidade..." -NoNewline
    Invoke-RestMethod -Method Put -Uri "http://localhost:8080/api/oportunidades/1/fechar" | Out-Null
    Write-Host " OK ✅" -ForegroundColor Green

    # ------------------ FASE 4: BUROCRACIA (CERTIFICADO E APROVEITAMENTO) ------------------
    Write-Host "[15/20] Emitindo Certificado para Ana..." -NoNewline
    $certificado = Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/certificados?emailDiscente=ana@ufma.br&tituloOportunidade=PIBIC" -ContentType "application/json" -Body '{"horas": 120, "certificadoPath": "C:/pdf"}'
    Write-Host " OK ✅" -ForegroundColor Green
    
    Write-Host "[16/20] Validando Hash do Certificado..." -NoNewline
    $hash = $certificado.uuidHash
    Invoke-RestMethod -Method Get -Uri "http://localhost:8080/api/certificados/validar/$hash" | Out-Null
    Write-Host " OK ✅" -ForegroundColor Green

    Write-Host "[17/20] Ana Solicitando Aproveitamento de Horas..." -NoNewline
    Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/aproveitamentos?emailDiscente=ana@ufma.br" -ContentType "application/json" -Body '{"descricao": "PIBIC", "instituicao": "UFMA", "horas": 120}' | Out-Null
    Write-Host " OK ✅" -ForegroundColor Green

    Write-Host "[18/20] Professor Aprovando Aproveitamento..." -NoNewline
    Invoke-RestMethod -Method Put -Uri "http://localhost:8080/api/aproveitamentos/1/aprovar?emailAvaliador=carlos@ufma.br" | Out-Null
    Write-Host " OK ✅" -ForegroundColor Green

    # ------------------ FASE 5: USUARIOS EXTRA ------------------
    Write-Host "[19/20] Alterando Senha da Ana..." -NoNewline
    Invoke-RestMethod -Method Put -Uri "http://localhost:8080/api/usuarios/ana@ufma.br/senha?novaSenha=senha_nova_secreta" | Out-Null
    Write-Host " OK ✅" -ForegroundColor Green

    Write-Host "[20/20] Desativando a conta do Joao (Soft Delete)..." -NoNewline
    Invoke-RestMethod -Method Delete -Uri "http://localhost:8080/api/usuarios/joao@ufma.br" | Out-Null
    Write-Host " OK ✅" -ForegroundColor Green


    Write-Host ""
    Write-Host "===================================================" -ForegroundColor Green
    Write-Host " 🎉 TESTE MÁXIMO FINALIZADO COM SUCESSO ABSOLUTO! 🎉 " -ForegroundColor Green
    Write-Host "===================================================" -ForegroundColor Green
    Write-Host "DICA: Olhe o Console da sua IDE agora. O relatório de ponta a ponta impresso lá está lindo!" -ForegroundColor Yellow

} catch {
    Write-Host " FALHA ❌" -ForegroundColor Red
    Write-Host "Erro Encontrado no Passo atual:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    Write-Host "Dica: Veja se a aplicação Spring Boot está rodando e se você limpou o banco de dados antes de rodar o teste." -ForegroundColor Yellow
}
