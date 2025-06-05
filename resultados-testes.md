# Relatório de Testes - Sistema de Votação

## Informações do Ambiente
- **Data dos Testes**: 03/06/2025
- **Ambiente**: Local (Docker)
- **URL Base**: http://localhost:8080
- **Versão da Aplicação**: 1.0.0

## Configuração do Ambiente
O ambiente foi configurado usando Docker Compose com os seguintes serviços:
- PostgreSQL (porta 5433)
- Aplicação Spring Boot (porta 8080)

## Testes Realizados

### 1. Cadastro de Pauta
**Endpoint**: POST /api/pautas

**Teste 1: Cadastro com sucesso**
```http
POST http://localhost:8080/api/pautas
Content-Type: application/json

{
    "titulo": "Pauta de Teste",
    "descricao": "Descrição da pauta de teste"
}
```
**Resultado**: ✅ Sucesso
- Status: 201 Created
- ID da pauta retornado corretamente

### 2. Abertura de Sessão
**Endpoint**: POST /api/sessoes

**Teste 1: Abertura de sessão com sucesso**
```http
POST http://localhost:8080/api/sessoes
Content-Type: application/json

{
    "pautaId": 1,
    "duracaoMinutos": 5
}
```
**Resultado**: ✅ Sucesso
- Status: 201 Created
- Sessão aberta corretamente

### 3. Registro de Voto
**Endpoint**: POST /api/votos

**Teste 1: Voto válido**
```http
POST http://localhost:8080/api/votos
Content-Type: application/json

{
    "sessaoId": 1,
    "cpf": "12345678900",
    "voto": "SIM"
}
```
**Resultado**: ✅ Sucesso
- Status: 201 Created
- Voto registrado corretamente

**Teste 2: Voto duplicado (mesmo CPF)**
```http
POST http://localhost:8080/api/votos
Content-Type: application/json

{
    "sessaoId": 1,
    "cpf": "12345678900",
    "voto": "SIM"
}
```
**Resultado**: ❌ Erro esperado
- Status: 400 Bad Request
- Mensagem: "Associado já votou nesta pauta"

**Teste 3: Voto em sessão encerrada**
```http
POST http://localhost:8080/api/votos
Content-Type: application/json

{
    "sessaoId": 1,
    "cpf": "98765432100",
    "voto": "NAO"
}
```
**Resultado**: ❌ Erro esperado
- Status: 400 Bad Request
- Mensagem: "Sessão de votação já encerrada"

### 4. Consulta Resultado
**Endpoint**: GET /api/sessoes/{sessaoId}/resultado

**Teste 1: Consulta resultado após encerramento**
```http
GET http://localhost:8080/api/sessoes/1/resultado
```
**Resultado**: ✅ Sucesso
- Status: 200 OK
- Resultado retornado corretamente com contagem de votos

## Resumo dos Resultados

| Funcionalidade | Total de Testes | Sucesso | Falha |
|----------------|----------------|---------|-------|
| Cadastro de Pauta | 1 | 1 | 0 |
| Abertura de Sessão | 1 | 1 | 0 |
| Registro de Voto | 3 | 1 | 2* |
| Consulta Resultado | 1 | 1 | 0 |

\* As falhas nos testes de voto são comportamentos esperados (validações de regras de negócio)

## Conclusão
O sistema está funcionando conforme esperado, com todas as validações de regras de negócio implementadas corretamente:
1. Não permite votos duplicados
2. Não permite votos após o encerramento da sessão
3. Registra corretamente os votos válidos
4. Contabiliza e apresenta os resultados corretamente

