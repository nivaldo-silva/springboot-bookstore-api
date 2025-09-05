# API Livraria

## Visão Geral

API RESTful para sistema de gerenciamento de livraria online desenvolvida com Spring Boot. A aplicação implementa princípios de Arquitetura Limpa e padrões de design SOLID para garantir manutenibilidade, escalabilidade e testabilidade.

## Funcionalidades

- **Gerenciamento de Livros**: Operações CRUD completas com controle de estoque, filtragem por gênero e autor, paginação e ordenação
- **Gerenciamento de Autores**: Cadastro e manutenção de autores com validação de dados
- **Gerenciamento de Clientes**: Cadastro de clientes com senhas criptografadas (BCrypt) e gestão de perfil
- **Sistema de Pedidos**: Criação de pedidos com múltiplos itens, validação automática de estoque e cálculo de totais
- **Segurança**: Implementação Spring Security com criptografia de senhas
- **Documentação da API**: Documentação automatizada com Swagger/OpenAPI

## Stack Tecnológica

- **Java 21** - Linguagem de programação
- **Spring Boot 3.5.5** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Framework de segurança
- **MySQL** - Banco de dados principal
- **Redis** - Camada de cache
- **Maven** - Gerenciamento de build e dependências
- **Lombok** - Geração de código
- **SpringDoc OpenAPI** - Documentação da API


## Arquitetura

```
src/main/java/io/github/nivaldosilva/bookstore/
├── controllers/      # Endpoints REST
├── services/         # Camada de lógica de negócio
├── repositories/     # Camada de acesso a dados
├── entities/         # Entidades do domínio
├── dtos/            # Objetos de transferência de dados
├── mappers/         # Mapeadores Entity-DTO
├── config/          # Configurações do Spring
└── exceptions/      # Tratamento global de exceções
```

## Início Rápido

### Pré-requisitos

- JDK 21+
- Maven 3.6+
- MySQL 8.0+

### Instalação

1. **Clone o repositório**
   ```bash
   git clone https://github.com/nivaldo-silva/bookstore-backend-api.git
   cd bookstore
   ```

2. **Configuração do Banco de Dados**
   - Crie um banco de dados MySQL
   - Atualize `src/main/resources/application.yml` com suas credenciais do banco

3. **Execute a aplicação**
   ```bash
   mvn spring-boot:run
   ```

4. **Acesse a Documentação da API**
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - Especificação OpenAPI: http://localhost:8080/v3/api-docs

### Testes

```bash
# Executar testes unitários
mvn test

## Endpoints da API

### Livros
- `GET /api/books` - Listar livros com paginação e filtros
- `GET /api/books/{id}` - Buscar livro por ID
- `POST /api/books` - Criar novo livro
- `PUT /api/books/{id}` - Atualizar livro
- `DELETE /api/books/{id}` - Excluir livro

### Autores
- `GET /api/authors` - Listar autores
- `GET /api/authors/{id}` - Buscar autor por ID
- `POST /api/authors` - Criar autor
- `PUT /api/authors/{id}` - Atualizar autor
- `DELETE /api/authors/{id}` - Excluir autor

### Clientes
- `GET /api/customers` - Listar clientes
- `GET /api/customers/{id}` - Buscar cliente por ID
- `POST /api/customers` - Registrar cliente
- `PUT /api/customers/{id}` - Atualizar cliente

### Pedidos
- `GET /api/orders` - Listar pedidos
- `GET /api/orders/{id}` - Buscar pedido por ID
- `POST /api/orders` - Criar pedido
- `PUT /api/orders/{id}` - Atualizar status do pedido


## Contribuindo

1. Faça um fork do repositório
2. Crie uma branch para sua funcionalidade
3. Faça suas alterações
4. Adicione testes para novas funcionalidades
5. Certifique-se de que todos os testes passem
6. Envie um pull request