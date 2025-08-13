
###  Bookstore API

#### Visão Geral

Este projeto é uma API RESTful para uma livraria online, desenvolvida com Spring Boot. A aplicação foi construída seguindo as melhores práticas de arquitetura limpa e princípios SOLID, garantindo um sistema robusto e de fácil manutenção.

#### Objetivo do Projeto

Demonstrar proficiência técnica no ecossistema Java e Spring Boot, com foco em arquitetura REST, qualidade de código, segurança, validação de dados e aplicação de padrões de design consolidados.

#### Arquitetura e Design

#### Padrões Arquiteturais
- **Arquitetura em Camadas (MVC)**: Separação clara entre Controlador, Serviço e Repositório
- **Repository Pattern**: Abstração da camada de acesso a dados
- **Service Layer Pattern**: Centralização da lógica de negócio
- **Data Transfer Objects (DTO)**: Transferência segura de dados entre camadas

#### Princípios Aplicados
- Código limpo e legível
- Separação clara de responsabilidades
- Controle transacional robusto
- Validações abrangentes com Jakarta Validation
- Tratamento global de exceções
- Documentação automática com Swagger (SpringDoc OpenAPI)

#### Funcionalidades Principais

#### Gerenciamento de Livros
- Operações CRUD completas
- Sistema de filtros por gênero e autor
- Controle de estoque integrado
- Paginação e ordenação dinâmica

#### Gerenciamento de Autores
- Cadastro e manutenção com validação rigorosa de dados
- Relacionamento com livros publicados

#### Gerenciamento de Clientes
- Sistema de cadastro com criptografia de senhas (BCrypt)
- Funcionalidades de atualização de perfil
- Consultas otimizadas por identificador

#### Sistema de Pedidos
- Criação de pedidos com múltiplos itens
- Validação automática de disponibilidade em estoque
- Cálculo automático de totais
- Controle transacional para consistência de dados

#### Stack Tecnológica

#### Core
- **Java 21**: Linguagem principal com recursos modernos
- **Spring Boot 3.3.x**: Framework principal para desenvolvimento
- **Spring Data JPA + Hibernate**: Mapeamento objeto-relacional
- **MySQL 8.0**: Sistema de gerenciamento de banco de dados

#### Segurança e Validação
- **Spring Security**: Framework de segurança
- **Bean Validation**: Validação declarativa de dados
- **BCrypt**: Algoritmo de hash para senhas

#### Ferramentas de Desenvolvimento
- **Maven**: Gerenciamento de dependências e build
- **Lombok**: Redução de código boilerplate
- **SLF4J + Logback**: Sistema de logging
- **SpringDoc OpenAPI**: Documentação automática de APIs

#### Estrutura do Projeto

```
src/main/java/
├── controller/       # Endpoints REST e controle de requisições
├── service/          # Implementação das regras de negócio
├── repository/       # Camada de acesso a dados (JPA)
├── dto/              # Objetos de transferência de dados
├── entity/           # Entidades do modelo de domínio
├── config/           # Configurações do Spring Framework
└── exception/        # Tratamento centralizado de exceções
```

#### Benefícios da Arquitetura

#### Manutenibilidade
- Código bem estruturado e de fácil compreensão
- Separação clara de responsabilidades
- Documentação integrada e atualizada automaticamente

#### Escalabilidade
- Arquitetura preparada para crescimento horizontal
- Componentes desacoplados e independentes
- Padrões que facilitam a expansão de funcionalidades

#### Testabilidade
- Camadas isoladas e testáveis independentemente
- Injeção de dependências facilitando mock e stub
- Cobertura de testes simplificada

#### Reutilização
- Componentes modulares e bem definidos
- Interfaces claras entre camadas
- Código reutilizável em diferentes contextos

#### Configuração e Execução

#### Pré-requisitos
- Java Development Kit (JDK) 21 ou superior
- Apache Maven 3.6 ou superior
- MySQL 8.0 ou Docker para containerização
- Docker (opcional, para ambiente containerizado)

#### Instruções de Instalação

1. **Clone do repositório**
   ```bash
   git clone https://github.com/nivaldo-silva/bookstore-microservice.git
   cd bookstore-microservice
   ```

2. **Configuração do banco de dados**
   - Configure as credenciais do MySQL no arquivo `application.yaml`
   - Certifique-se de que o banco de dados esteja em execução

3. **Execução da aplicação**
   ```bash
   mvn spring-boot:run
   ```

4. **Acesso à documentação**
   - Interface Swagger UI: `http://localhost:8080/swagger-ui.html`
   - Especificação OpenAPI: `http://localhost:8080/v3/api-docs`

#### Documentação da API

A documentação completa da API está disponível através do Swagger UI, incluindo:
- Especificação detalhada de todos os endpoints
- Modelos de dados com exemplos
- Códigos de resposta e tratamento de erros
- Interface interativa para testes

#### Contribuição

Este projeto segue padrões de código estabelecidos e práticas de desenvolvimento profissional. Contribuições são bem-vindas mediante pull requests que mantenham a qualidade e consistência do código existente.











