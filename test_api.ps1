$ErrorActionPreference = "Stop"

# Helper para fazer requisicoes HTTP e validar o status code esperado
function Invoke-Test {
    param(
        [string]$Method,
        [string]$Uri,
        [string]$Body,
        [int]$ExpectedStatusCode = 200
    )
    
    $headers = @{ "Content-Type" = "application/json" }
    
    try {
        if ($Body) {
            $resp = Invoke-WebRequest -Method $Method -Uri $Uri -Body $Body -Headers $headers -UseBasicParsing
        } else {
            $resp = Invoke-WebRequest -Method $Method -Uri $Uri -Headers $headers -UseBasicParsing
        }
        
        $statusCode = $resp.StatusCode
        $content = $resp.Content
        
        if ($statusCode -eq $ExpectedStatusCode) {
            return [PSCustomObject]@{ Success = $true; Content = $content; StatusCode = $statusCode }
        } else {
            return [PSCustomObject]@{ Success = $false; Error = "Esperava status $ExpectedStatusCode, obteve $statusCode. Conteudo: $content"; StatusCode = $statusCode }
        }
    } catch {
        if ($_.Exception.Response) {
            $statusCode = [int]$_.Exception.Response.StatusCode
            $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
            $content = $reader.ReadToEnd()
            $reader.Close()
            
            if ($statusCode -eq $ExpectedStatusCode) {
                return [PSCustomObject]@{ Success = $true; Content = $content; StatusCode = $statusCode }
            } else {
                return [PSCustomObject]@{ Success = $false; Error = "Esperava status $ExpectedStatusCode, obteve $statusCode. Resposta: $content"; StatusCode = $statusCode }
            }
        } else {
            return [PSCustomObject]@{ Success = $false; Error = $_.Exception.Message; StatusCode = 0 }
        }
    }
}

# Helper para converter json de string para objeto PowerShell
function Convert-FromJsonHelper {
    param($JsonString)
    return $JsonString | ConvertFrom-Json
}

Write-Host "===================================================" -ForegroundColor Cyan
Write-Host "   BATERIA ULTRA COMPLETA DE TESTES DE INTEGRACAO   " -ForegroundColor Cyan
Write-Host "       (TODOS OS MODULOS E CASOS DE ERRO)           " -ForegroundColor Cyan
Write-Host "===================================================" -ForegroundColor Cyan
Write-Host ""

$totalPassos = 45
$passosOk = 0

function Executar-Passo {
    param(
        [string]$Nome,
        [ScriptBlock]$ScriptBlock
    )
    
    $global:passosOk++
    Write-Host "[$global:passosOk/$totalPassos] $Nome... " -NoNewline
    
    $res = &$ScriptBlock
    
    if ($res.Success) {
        Write-Host "OK" -ForegroundColor Green
        return $res.Content
    } else {
        Write-Host "FALHA" -ForegroundColor Red
        Write-Host "  Erro detalhado: $($res.Error)" -ForegroundColor Red
        throw "Erro na execucao do passo '$Nome'. O teste foi interrompido."
    }
}

try {
    # ================= MODULO 1: USUARIOS =================
    
    $resDocente = Executar-Passo "Registrar Docente" {
        Invoke-Test -Method Post -Uri "http://localhost:8080/api/usuarios/docente" -Body '{"nome":"Carlos Silva","email":"carlos@ufma.br","senha":"123","papel":"DOCENTE","siape":"99112"}'
    }

    $resAna = Executar-Passo "Registrar Discente Ana" {
        Invoke-Test -Method Post -Uri "http://localhost:8080/api/usuarios/discente" -Body '{"nome":"Ana","email":"ana@ufma.br","senha":"123","papel":"DISCENTE","matricula":"20221","semestreAtual":4}'
    }

    $resJoao = Executar-Passo "Registrar Discente Joao" {
        Invoke-Test -Method Post -Uri "http://localhost:8080/api/usuarios/discente" -Body '{"nome":"Joao","email":"joao@ufma.br","senha":"123","papel":"DISCENTE","matricula":"20231","semestreAtual":2}'
    }

    $resDiretor = Executar-Passo "Registrar Discente Diretor" {
        Invoke-Test -Method Post -Uri "http://localhost:8080/api/usuarios/diretor" -Body '{"nome":"Diretor Grupo","email":"diretor@ufma.br","senha":"123","papel":"DISCENTE_DIRETOR","matricula":"20211","semestreAtual":8}'
    }

    Executar-Passo "Listar Todos os Usuarios" {
        Invoke-Test -Method Get -Uri "http://localhost:8080/api/usuarios"
    }

    Executar-Passo "Listar Apenas Discentes" {
        Invoke-Test -Method Get -Uri "http://localhost:8080/api/usuarios/discentes"
    }

    Executar-Passo "Buscar Usuario por Email (Ana)" {
        Invoke-Test -Method Get -Uri "http://localhost:8080/api/usuarios/ana@ufma.br"
    }

    Executar-Passo "Realizar Login com Sucesso" {
        Invoke-Test -Method Post -Uri "http://localhost:8080/api/usuarios/login?email=ana@ufma.br&senha=123"
    }

    # ================= MODULO 2: CURSOS =================

    Executar-Passo "Registrar Novo Curso" {
        Invoke-Test -Method Post -Uri "http://localhost:8080/api/cursos" -Body '{"codigo":1001,"nome":"Ciencia da Computacao","cargaHoraria":3200,"versaoPpc":"PPC-2022"}'
    }

    Executar-Passo "Listar Todos os Cursos" {
        Invoke-Test -Method Get -Uri "http://localhost:8080/api/cursos"
    }

    Executar-Passo "Matricular Ana no Curso 1001" {
        Invoke-Test -Method Put -Uri "http://localhost:8080/api/cursos/1001/matricular?emailDiscente=ana@ufma.br"
    }

    Executar-Passo "Matricular Joao no Curso 1001" {
        Invoke-Test -Method Put -Uri "http://localhost:8080/api/cursos/1001/matricular?emailDiscente=joao@ufma.br"
    }

    Executar-Passo "Verificar Total de Alunos no Curso 1001" {
        Invoke-Test -Method Get -Uri "http://localhost:8080/api/cursos/1001/alunos/total"
    }

    Executar-Passo "Listar Alunos Ativos no Curso 1001" {
        Invoke-Test -Method Get -Uri "http://localhost:8080/api/cursos/1001/alunos/status?status=ATIVO"
    }

    Executar-Passo "Atualizar PPC do Curso 1001" {
        Invoke-Test -Method Put -Uri "http://localhost:8080/api/cursos/1001/ppc?novasCargaHoraria=3400&novaVersao=PPC-2024"
    }

    Executar-Passo "Listar Historico do PPC do Curso 1001" {
        Invoke-Test -Method Get -Uri "http://localhost:8080/api/cursos/1001/historico-ppc"
    }

    # ================= MODULO 3: GRUPOS =================

    Executar-Passo "Solicitar Criacao de Grupo (Diretor Valido)" {
        Invoke-Test -Method Post -Uri "http://localhost:8080/api/grupos/solicitacoes?emailDiretor=diretor@ufma.br" -Body '{"nome":"Grupo de Banco de Dados","tipo":"Pesquisa","email":"bd.pesquisa@ufma.br","descricao":"Grupo de BD"}'
    }

    Executar-Passo "Registrar Novo Grupo de IA" {
        Invoke-Test -Method Post -Uri "http://localhost:8080/api/grupos" -Body '{"nome":"Grupo de Pesquisa em IA","tipo":"Pesquisa","email":"ia.pesquisa@ufma.br","descricao":"IA aplicada","responsavel":{"email":"carlos@ufma.br"}}'
    }

    Executar-Passo "Listar Todos os Grupos" {
        Invoke-Test -Method Get -Uri "http://localhost:8080/api/grupos"
    }

    Executar-Passo "Aprovar Grupo de IA" {
        Invoke-Test -Method Put -Uri "http://localhost:8080/api/grupos/Grupo de Pesquisa em IA/aprovar"
    }

    Executar-Passo "Listar Apenas Grupos Ativos" {
        Invoke-Test -Method Get -Uri "http://localhost:8080/api/grupos/ativos"
    }

    Executar-Passo "Buscar Grupo de IA por Nome" {
        Invoke-Test -Method Get -Uri "http://localhost:8080/api/grupos/Grupo de Pesquisa em IA"
    }

    Executar-Passo "Adicionar Ana como Membro do Grupo de IA" {
        Invoke-Test -Method Post -Uri "http://localhost:8080/api/grupos/Grupo de Pesquisa em IA/membros/ana@ufma.br"
    }

    Executar-Passo "Adicionar Joao como Membro do Grupo de IA" {
        Invoke-Test -Method Post -Uri "http://localhost:8080/api/grupos/Grupo de Pesquisa em IA/membros/joao@ufma.br"
    }

    Executar-Passo "Listar Membros do Grupo de IA" {
        Invoke-Test -Method Get -Uri "http://localhost:8080/api/grupos/Grupo de Pesquisa em IA/membros"
    }

    Executar-Passo "Atribuir Cargo de DIRETOR a Ana no Grupo" {
        Invoke-Test -Method Put -Uri "http://localhost:8080/api/grupos/Grupo de Pesquisa em IA/cargos?emailDiscente=ana@ufma.br&cargo=DIRETOR&emailDocente=carlos@ufma.br"
    }

    Executar-Passo "Remover Joao do Grupo de IA" {
        Invoke-Test -Method Delete -Uri "http://localhost:8080/api/grupos/Grupo de Pesquisa em IA/membros/joao@ufma.br"
    }

    Executar-Passo "Ver Historico de Membros do Grupo de IA" {
        Invoke-Test -Method Get -Uri "http://localhost:8080/api/grupos/Grupo de Pesquisa em IA/historico"
    }

    # ================= MODULO 4: OPORTUNIDADES =================

    Executar-Passo "Registrar Oportunidade PIBIC (tipo PROJETO)" {
        Invoke-Test -Method Post -Uri "http://localhost:8080/api/oportunidades?emailAutor=carlos@ufma.br" -Body '{"titulo":"Bolsa de IC em IA","descricao":"Deep Learning","tipo":"PROJETO","modalidade":"PRESENCIAL","carga_horaria":20,"vagas":1,"inicio":"2026-08-01","fim":"2026-12-01"}'
    }

    Executar-Passo "Listar Todas as Oportunidades" {
        Invoke-Test -Method Get -Uri "http://localhost:8080/api/oportunidades"
    }

    Executar-Passo "Divulgar Oportunidade (Carlos Docente -> AGUARDANDO_APROVACAO)" {
        Invoke-Test -Method Put -Uri "http://localhost:8080/api/oportunidades/1/divulgar?emailRequisitante=carlos@ufma.br"
    }

    Executar-Passo "Aprovar Oportunidade (Carlos Docente -> PUBLICADA)" {
        Invoke-Test -Method Put -Uri "http://localhost:8080/api/oportunidades/1/aprovar?emailAvaliador=carlos@ufma.br"
    }

    Executar-Passo "Listar Oportunidades Abertas" {
        Invoke-Test -Method Get -Uri "http://localhost:8080/api/oportunidades/abertas"
    }

    # ================= MODULO 5: INSCRICOES =================

    Executar-Passo "Inscrever Ana na Oportunidade" {
        Invoke-Test -Method Post -Uri "http://localhost:8080/api/inscricoes?tituloOportunidade=Bolsa de IC em IA&emailDiscente=ana@ufma.br&motivacao=Gosto de IA"
    }

    Executar-Passo "Listar Inscritos na Oportunidade" {
        Invoke-Test -Method Get -Uri "http://localhost:8080/api/inscricoes/oportunidade/Bolsa de IC em IA"
    }

    Executar-Passo "Aprovar Inscricao de Ana" {
        Invoke-Test -Method Put -Uri "http://localhost:8080/api/inscricoes/1/aprovar"
    }

    Executar-Passo "Encerrar Oportunidade" {
        Invoke-Test -Method Put -Uri "http://localhost:8080/api/oportunidades/1/fechar"
    }

    # ================= MODULO 6: CERTIFICADOS =================

    $loteRes = Executar-Passo "Gerar Certificados em Lote" {
        Invoke-Test -Method Post -Uri "http://localhost:8080/api/certificados/lote?tituloOportunidade=Bolsa de IC em IA&cargaHoraria=20&caminhoBase=/docs/certificados"
    }

    $resMeusCerts = Executar-Passo "Listar Certificados da Ana" {
        Invoke-Test -Method Get -Uri "http://localhost:8080/api/certificados/meus/ana@ufma.br"
    }

    Executar-Passo "Validar Hash do Certificado Oficial" {
        $certList = Convert-FromJsonHelper $resMeusCerts
        $hash = $certList[0].uuidHash
        Invoke-Test -Method Get -Uri "http://localhost:8080/api/certificados/validar/$hash"
    }

    # ================= MODULO 7: APROVEITAMENTOS =================

    Executar-Passo "Registrar Solicitacao de Aproveitamento (Ana)" {
        Invoke-Test -Method Post -Uri "http://localhost:8080/api/aproveitamentos?emailDiscente=ana@ufma.br" -Body '{"descricao":"Hackathon","instituicao":"UFMA","horas":40}'
    }

    Executar-Passo "Listar Aproveitamentos Pendentes" {
        Invoke-Test -Method Get -Uri "http://localhost:8080/api/aproveitamentos/pendentes"
    }

    Executar-Passo "Verificar Prazo do Aproveitamento" {
        Invoke-Test -Method Get -Uri "http://localhost:8080/api/aproveitamentos/1/prazo"
    }

    Executar-Passo "Rejeitar Aproveitamento para Ajustes" {
        Invoke-Test -Method Put -Uri "http://localhost:8080/api/aproveitamentos/1/rejeitar?emailAvaliador=carlos@ufma.br&motivo=Sem assinatura"
    }

    Executar-Passo "Reenviar Aproveitamento Corrigido" {
        Invoke-Test -Method Put -Uri "http://localhost:8080/api/aproveitamentos/1/reenviar?novoCertificadoPath=/docs/comprovante.pdf"
    }

    Executar-Passo "Aprovar Aproveitamento" {
        Invoke-Test -Method Put -Uri "http://localhost:8080/api/aproveitamentos/1/aprovar?emailAvaliador=carlos@ufma.br"
    }

    Executar-Passo "Calcular Total de Horas Aprovadas (Ana)" {
        Invoke-Test -Method Get -Uri "http://localhost:8080/api/aproveitamentos/horas/ana@ufma.br"
    }

    # ================= MODULO 8: CONTROLE DE ERROS HUMANIZADOS (404/400) =================

    Executar-Passo "Testar Erro: Buscar Grupo Inexistente (404)" {
        Invoke-Test -Method Get -Uri "http://localhost:8080/api/grupos/Grupo Fantasma" -ExpectedStatusCode 404
    }

    Executar-Passo "Testar Erro: Matricular Email Inexistente (404)" {
        Invoke-Test -Method Put -Uri "http://localhost:8080/api/cursos/1001/matricular?emailDiscente=inexistente@ufma.br" -ExpectedStatusCode 404
    }

    # ================= MODULO 9: GERENCIAMENTO DE USUARIOS EXTRA =================

    Executar-Passo "Mudar Senha de Usuario" {
        Invoke-Test -Method Put -Uri "http://localhost:8080/api/usuarios/ana@ufma.br/senha?novaSenha=novasenha123"
    }

    Executar-Passo "Remover/Desativar Usuario Joao (Soft Delete)" {
        Invoke-Test -Method Delete -Uri "http://localhost:8080/api/usuarios/joao@ufma.br"
    }

    Write-Host ""
    Write-Host "===================================================" -ForegroundColor Green
    Write-Host "  TODOS OS TESTES PASSARAM COM SUCESSO ABSOLUTO!   " -ForegroundColor Green
    Write-Host "===================================================" -ForegroundColor Green
    Write-Host "Cenarios validados: CRUDs, Enums, Fluxos de Aprovacao e Tratamento de Erros." -ForegroundColor Yellow

} catch {
    Write-Host ""
    Write-Host "===================================================" -ForegroundColor Red
    Write-Host "                 ERRO NO TESTE                     " -ForegroundColor Red
    Write-Host "===================================================" -ForegroundColor Red
    Write-Host "Falha no passo atual: $_" -ForegroundColor Red
    Write-Host "Instrucoes:" -ForegroundColor Yellow
    Write-Host "1. Verifique se o servidor Spring Boot esta rodando localmente (porta 8080)." -ForegroundColor Yellow
    Write-Host "2. Certifique-se de REINICIAR a aplicacao antes de rodar o script." -ForegroundColor Yellow
}
