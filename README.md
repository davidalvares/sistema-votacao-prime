# Sistema de Votação para Cooperativa

Este é um sistema de votação desenvolvido para cooperativas, permitindo a criação de pautas e gerenciamento de sessões de votação.

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.2.3
- MongoDB
- Kafka
- Docker
- Maven
- Swagger/OpenAPI 3.0
- k6 (Testes de Performance)
- Postman (Testes de API)

## Funcionalidades

- Criação de pautas para votação
- Abertura de sessões de votação com tempo configurável
- Registro de votos (Sim/Não) por associados
- Contabilização dos resultados da votação
- Documentação interativa da API com Swagger
- Testes de performance automatizados
- Coleções Postman para testes manuais e automatizados

## Pré-requisitos

- Docker
- Docker Compose
- Git
- Postman (opcional, para testes manuais)

## Como Executar

### Usando Docker Compose (Recomendado)

1. Clone o repositório:
```bash
git clone [URL_DO_REPOSITORIO]
cd [NOME_DO_DIRETORIO]
```

2. Execute o projeto usando Docker Compose:
```bash
   docker-compose up --build
```

O sistema estará disponível em:
- API: `http://localhost:8080`
- Documentação Swagger: `http://localhost:8080/swagger-ui.html`
- Grafana Dashboard: `http://localhost:3000`

3. Execute o projeto com Maven:
```bash
  mvn spring-boot:run
```

## Testes de API com Postman

A aplicação inclui coleções Postman completas para testes manuais e automatizados.

### Estrutura das Coleções

1. **Testes de Performance**
   - Smoke Test (200ms)
   - Load Test (500ms)
   - Stress Test (limites)

### Como Usar as Coleções

1. Importe os arquivos no Postman:
   - `postman/Votacao_Cooperativa.postman_collection.json`
   - `postman/Votacao_Cooperativa.postman_environment.json`

2. Configure o ambiente:
   - Selecione "Votação Cooperativa - Local"
   - As variáveis principais já estão configuradas
   - Ajuste `baseUrl` se necessário

3. Execute os testes:
   - Individualmente: Clique em "Send" em cada request
   - Em lote: Use o "Collection Runner"
   - Automatizado: Use o "Newman" via CLI

### Variáveis de Ambiente

- `baseUrl`: URL base da API (default: http://localhost:8080)
- `language`: Linguagem para votação (default: Java)
- Outras variáveis para testes específicos

### Testes Automatizados

Cada endpoint inclui testes automatizados para:
- Status code correto
- Formato da resposta
- Dados esperados
- Tempo de resposta
- Casos de erro

## Testes de Performance

O projeto inclui testes de performance automatizados usando k6, com visualização de métricas através do Grafana.

### Cenários de Teste (Total: ~3 minutos)

1. **Smoke Test** (30s):
   - 1 usuário constante
   - Validação básica do sistema

2. **Teste de Carga** (1.5min):
   - Até 50 usuários simultâneos
   - Simula uso normal da aplicação

3. **Teste de Stress** (1.5min):
   - Até 100 usuários simultâneos
   - Avalia limites do sistema

### Executando os Testes

```bash
# Inicia todos os serviços
docker-compose up -d

# Executa os testes de performance
docker-compose run k6 run /scripts/performance-test.js
```

## Documentação da API

A documentação completa da API está disponível através do Swagger UI. Após iniciar a aplicação, acesse:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- OpenAPI YAML: `http://localhost:8080/v3/api-docs.yaml`

## Configuração do Ambiente de Desenvolvimento

### Configuração do IDE

1. Importe o projeto como um projeto Maven
2. Certifique-se de usar Java 17
3. Execute o `mvn clean install` para baixar todas as dependências

Autor: David Alvares Engenheiro de Software.
