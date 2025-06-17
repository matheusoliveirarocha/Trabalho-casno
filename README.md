# Cassino Online Microservice

## Descrição

Microserviço para cassino online desenvolvido com Spring Boot, implementando três jogos clássicos de cassino: **Blackjack**, **Roleta** e **Slot Machine**. O projeto segue as melhores práticas de desenvolvimento, incluindo arquitetura MVC, validações, documentação automática com Swagger e testes unitários com cobertura superior a 90%.

## Tecnologias Utilizadas

- **Java 17** - Linguagem de programação
- **Spring Boot 3.2.0** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Boot Validation** - Validação de dados
- **H2 Database** - Banco de dados em memória (desenvolvimento)
- **MySQL/PostgreSQL** - Banco de dados para produção
- **Swagger/OpenAPI** - Documentação da API
- **JUnit 5** - Testes unitários
- **Mockito** - Mocking para testes
- **JaCoCo** - Cobertura de testes
- **Maven** - Gerenciamento de dependências

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/cassino/online/
│   │   ├── CassinoOnlineApplication.java
│   │   ├── config/
│   │   │   └── SwaggerConfig.java
│   │   ├── controller/
│   │   │   ├── PartidaController.java
│   │   │   ├── BlackjackController.java
│   │   │   ├── RoletaController.java
│   │   │   └── SlotMachineController.java
│   │   ├── dto/
│   │   │   ├── IniciarPartidaDTO.java
│   │   │   ├── JogadaBlackjackDTO.java
│   │   │   ├── JogadaRoletaDTO.java
│   │   │   └── ResultadoPartidaDTO.java
│   │   ├── enums/
│   │   │   ├── TipoJogo.java
│   │   │   └── StatusPartida.java
│   │   ├── model/
│   │   │   └── Partida.java
│   │   ├── repository/
│   │   │   └── PartidaRepository.java
│   │   └── service/
│   │       ├── PartidaService.java
│   │       ├── BlackjackService.java
│   │       ├── RoletaService.java
│   │       └── SlotMachineService.java
│   └── resources/
│       ├── application.properties
│       ├── application-prod.properties
│       ├── schema.sql
│       └── data.sql
└── test/
    └── java/com/cassino/online/service/
        ├── PartidaServiceTest.java
        ├── RoletaServiceTest.java
        └── SlotMachineServiceTest.java
```

## Funcionalidades Implementadas

### 1. Gerenciamento de Partidas
- **POST /api/partidas** - Iniciar nova partida
- **GET /api/partidas** - Listar todas as partidas
- **GET /api/partidas/{id}** - Buscar partida por ID
- **GET /api/partidas/jogador/{nomeJogador}** - Buscar partidas por jogador
- **GET /api/partidas/tipo/{tipoJogo}** - Buscar partidas por tipo de jogo
- **GET /api/partidas/em-andamento** - Buscar partidas em andamento
- **DELETE /api/partidas/{id}** - Cancelar partida
- **GET /api/partidas/historico/{nomeJogador}** - Histórico do jogador
- **GET /api/partidas/periodo** - Buscar partidas por período
- **GET /api/partidas/estatisticas/{nomeJogador}** - Estatísticas do jogador

### 2. Jogo Blackjack
- **POST /api/jogos/blackjack/iniciar/{partidaId}** - Iniciar partida de Blackjack
- **POST /api/jogos/blackjack/jogar/{partidaId}** - Fazer jogada (HIT, STAND, DOUBLE)

### 3. Jogo Roleta
- **POST /api/jogos/roleta/jogar/{partidaId}** - Jogar na roleta (número, cor, par/ímpar)

### 4. Jogo Slot Machine
- **POST /api/jogos/slot-machine/jogar/{partidaId}** - Jogar na slot machine

## Regras dos Jogos

### Blackjack
- Objetivo: Chegar o mais próximo possível de 21 sem estourar
- Ações disponíveis: HIT (pedir carta), STAND (parar), DOUBLE (dobrar aposta)
- Dealer joga automaticamente até 17
- Pagamento: 2:1 (4:1 se dobrar)

### Roleta
- Números de 0 a 36
- Tipos de aposta: número específico (35:1), cor (1:1), par/ímpar (1:1)
- Números vermelhos: 1,3,5,7,9,12,14,16,18,19,21,23,25,27,30,32,34,36
- Zero é verde

### Slot Machine
- 3 rolos com símbolos: 🍒🍋🍊🍇⭐💎🔔7️⃣
- Combinações vencedoras:
  - Três iguais: multiplicador completo
  - Dois iguais: multiplicador reduzido
  - Qualquer 7️⃣: multiplicador 2x

## Como Executar

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6 ou superior

### Executar em Desenvolvimento
```bash
# Clonar o projeto
cd cassino-online-microservice

# Compilar
mvn clean compile

# Executar testes
mvn test

# Executar aplicação
mvn spring-boot:run
```

### Acessar a Aplicação
- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:cassino_online`
  - Username: `sa`
  - Password: (vazio)

## Configuração para Produção

### Variáveis de Ambiente
```bash
export DATABASE_URL=jdbc:mysql://localhost:3306/cassino_online
export DATABASE_USERNAME=cassino_user
export DATABASE_PASSWORD=cassino_pass
export ALLOWED_ORIGINS=https://meusite.com
export LIMITE_MAXIMO_APOSTA=10000.00
export LIMITE_MINIMO_APOSTA=1.00
```

### Executar em Produção
```bash
mvn clean package -Pprod
java -jar target/cassino-online-microservice-1.0.0.jar --spring.profiles.active=prod
```

## Testes

O projeto inclui testes unitários abrangentes com cobertura superior a 90%:

```bash
# Executar testes
mvn test

# Gerar relatório de cobertura
mvn jacoco:report

# Verificar cobertura mínima
mvn jacoco:check
```

## Exemplos de Uso da API

### Iniciar uma Partida de Blackjack
```bash
curl -X POST http://localhost:8080/api/partidas \
  -H "Content-Type: application/json" \
  -d '{
    "nomeJogador": "João Silva",
    "tipoJogo": "BLACKJACK",
    "valorAposta": 50.00
  }'
```

### Jogar Blackjack
```bash
curl -X POST http://localhost:8080/api/jogos/blackjack/iniciar/1

curl -X POST http://localhost:8080/api/jogos/blackjack/jogar/1 \
  -H "Content-Type: application/json" \
  -d '{"acao": "HIT"}'
```

### Jogar Roleta
```bash
curl -X POST http://localhost:8080/api/jogos/roleta/jogar/1 \
  -H "Content-Type: application/json" \
  -d '{
    "numeroApostado": 7,
    "tipoAposta": "NUMERO"
  }'
```

### Jogar Slot Machine
```bash
curl -X POST http://localhost:8080/api/jogos/slot-machine/jogar/1
```

## Arquitetura e Padrões

### Padrão MVC
- **Model**: Entidades JPA (`Partida`)
- **View**: Respostas JSON via DTOs
- **Controller**: Controladores REST

### Injeção de Dependências
- Uso de `@Autowired` e construtores
- Separação clara de responsabilidades

### Validações
- Validações de entrada com Bean Validation
- Tratamento de erros personalizado

### Persistência
- Spring Data JPA com repositórios customizados
- Queries nativas e JPQL
- Suporte a múltiplos bancos de dados

## Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -am 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

## Licença

Este projeto está licenciado sob a MIT License - veja o arquivo LICENSE para detalhes.

## Equipe de Desenvolvimento

- Desenvolvido seguindo as especificações do documento "Desenvolvimento de um Microserviço com Spring Boot"
- Implementação completa de POO, testes unitários e documentação
- Cobertura de testes superior a 90% conforme requisitos



## Executar com Docker e Docker Compose

### Pré-requisitos
- Docker Desktop (ou Docker Engine e Docker Compose)

### Configuração
Crie um ficheiro `.env` na raiz do projeto com as seguintes variáveis de ambiente para o banco de dados MySQL:

```
MYSQL_ROOT_PASSWORD=sua_senha_root
MYSQL_USER=cassino_user
MYSQL_PASSWORD=cassino_pass
```

### Construir e Executar
```bash
# Navegue até a raiz do projeto
cd cassino-online-microservice

# Construa a imagem Docker da aplicação (isso também irá empacotar o projeto Maven)
mvn clean package docker:build

# Suba os serviços com Docker Compose
docker-compose up --build
```

### Acessar a Aplicação (Docker)
- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html

### Parar os Serviços
```bash
docker-compose down
```

