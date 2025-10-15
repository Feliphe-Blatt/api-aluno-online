@echo off
echo ========================================
echo   Configuracao do Banco de Dados MySQL
echo   API Aluno Online
echo ========================================
echo.

echo Passo 1: Compilando a aplicacao...
call mvnw clean package -DskipTests
if errorlevel 1 (
    echo ERRO: Falha na compilacao
    pause
    exit /b 1
)
echo Compilacao concluida com sucesso!
echo.

echo Passo 2: Iniciando aplicacao para criar tabelas...
echo (A aplicacao vai iniciar e criar o banco automaticamente)
echo (Aguarde 30 segundos e depois pressione Ctrl+C para parar)
echo.
timeout /t 5
start /b mvnw spring-boot:run
timeout /t 30
taskkill /F /FI "WINDOWTITLE eq mvnw spring-boot:run*" 2>nul
echo.

echo Passo 3: Populando banco de dados...
echo Digite a senha do MySQL root quando solicitado (padrao: root)
mysql -u root -p aluno_online < docs\populate_database.sql
if errorlevel 1 (
    echo AVISO: Erro ao popular banco. Verifique se o MySQL esta rodando.
    echo Tente executar manualmente: mysql -u root -p aluno_online ^< docs\populate_database.sql
    pause
    exit /b 1
)
echo.

echo ========================================
echo   Configuracao concluida!
echo ========================================
echo.
echo O banco de dados foi criado e populado com dados de teste.
echo.
echo Para iniciar a aplicacao, execute:
echo   mvnw spring-boot:run
echo.
echo Login de teste:
echo   Email: lucas.ferreira@aluno.com
echo   Senha: senha123
echo   Tipo: ALUNO
echo.
pause

