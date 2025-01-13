### Simple Transit API

Uma API REST para gerenciamento de ocorrências de transito.

### Tecnologias utilizadas

- Spring Security: Para implementar segurança na aplicação, como autenticação e autorização.
- Spring Web: Framework para criação de aplicações web, incluindo recursos como REST APIs.
- Spring Data JPA: Para facilitar a integração e o gerenciamento de dados com o banco de dados, utilizando JPA.
- Spring Validation: Para realizar validações de dados de entrada.
- Flyway: Ferramenta para migração de banco de dados, gerenciando versões do esquema do banco.
- PostgreSQL: Banco de dados relacional que será utilizado na aplicação.
- Spring Boot DevTools: Ferramentas de desenvolvimento para facilitar o processo de depuração e recarga automática da aplicação.
- H2 Database: Banco de dados em memória utilizado para testes ou desenvolvimento local.- Lombok: Biblioteca que reduz o código boilerplate em Java, como getters e setters.
- Spring Boot Starter Test: Ferramentas para testes no Spring Boot, incluindo JUnit e Mockito.
- Spring Security Test: Ferramentas de teste para segurança do Spring, usadas para testar configurações de segurança.
- Java JWT: Biblioteca para manipulação de tokens JWT (JSON Web Tokens).
- Springdoc OpenAPI: Para gerar a documentação da API REST utilizando OpenAPI e Swagger UI.

### Features
#### Auth (Autenticação)
- auth/register (POST): Registro de usuário.
- auth/login (POST): Login de usuário, retornando authToken e refreshToken.
- auth/refresh (POST): Obter um novo par de tokens a partir de um refreshToken.
- auth/ (PATCH): Atualizar informações de usuário (nome e senha).
#### Ocorrências
- ocorrencias/ (POST): Criação de ocorrência.
- ocorrencias/{id} (DELETE): Deletar uma ocorrência por ID.
- ocorrencias/{id} (GET): Obter uma ocorrência por ID.
- ocorrencias/ (GET): Obter todas as ocorrências com paginação.
#### Comentários
- comentarios (POST): Adicionar um comentário a uma ocorrência.

Aqui está a lista das regras e funcionalidades para o sistema:

## Regras e Funcionalidades

1. **Visualização de Ocorrências**:
   - Qualquer pessoa que acessar o sistema poderá visualizar todas as ocorrências inseridas, com todas as suas informações e comentários.

2. **Criação e Exclusão de Ocorrências**:
   - Apenas usuários autenticados (através de login e senha) podem criar novas ocorrências e excluir as ocorrências que eles mesmos inseriram.

3. **Comentários**:
   - Apenas usuários autenticados podem fazer comentários nas ocorrências.

4. **Limitação de Tempo para Ocorrências**:
   - O evento não pode ter ocorrido há mais de 2 dias.

5. **Pesquisa de Ocorrências**:
   - A pesquisa de ocorrências pode ser feita com filtros por:
     - Tipo
     - Data
     - Localização
     - Palavra-chave (busca no resumo ou na descrição)

### Instalação

### 1. Instalar o Docker

Antes de começar, você precisa ter o Docker instalado em sua máquina. Se você ainda não tem o Docker instalado, siga as instruções da documentação oficial:

- [Instalar Docker no Linux](https://docs.docker.com/engine/install/)
- [Instalar Docker no Windows](https://docs.docker.com/docker-for-windows/install/)
- [Instalar Docker no macOS](https://docs.docker.com/docker-for-mac/install/)

### 2. Baixar a Imagem Oficial do PostgreSQL

O Docker Hub contém a imagem oficial do PostgreSQL. Não é necessário baixar manualmente, pois o Docker fará isso automaticamente ao executar o comando `docker run`.

### 3. Executar o Comando Docker

Abra o terminal ou linha de comando e execute o seguinte comando:

```bash
docker run --name postgres -e POSTGRES_PASSWORD=password -p 5432:5432 -d postgres
```

1. Clone o repositório:

   ```bash
   git clone https://github.com/JulianeMaran32/my-projects.git
   ```

2. Navegue até o diretório do projeto:

   ```bash
   cd my-projects/address-api
   ```

3. Compile e execute o projeto:

   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

#### Uso

Acesse a documentação da API em `http://localhost:8081/api/v1/swagger-ui.html` para ver os endpoints disponíveis.
