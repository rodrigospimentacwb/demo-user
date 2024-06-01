# Projeto Java 21 com Spring Boot 3

## Descrição
Este é um pequeno projeto para demonstrar a autenticação, cadastro e busca de usuários no sistema.

## Tecnologias Utilizadas
- Java 21
- Spring Boot 3
- JUnit 5
- SOLID
- Inversão de Controle (IoC)
- Spring Security

## Banco de Dados
- MySQL (utilizado via Docker Compose)
- Banco H2 para contexto de testes de integração

## Organização do Projeto
Os pacotes foram separados em domínios.

## Arquivos na Raiz do Projeto
- Docker compose com as configurações do MySQL
- Collection do Postman e env

## Configurações Necessárias
Para executar o projeto, é necessário criar algumas variáveis de ambiente:
- `JWT_SECRET`: Chave secreta para JWT (você pode gerar uma [aqui](https://www.devglan.com/online-tools/hmac-sha256-online?ref=blog.tericcabrel.com))
- `DB_USERNAME`: Usuário do banco de dados (pode ser root)
- `DB_PASSWORD`: Senha do banco de dados (pode ser root)

## Endpoints
Abaixo estão alguns exemplos de endpoints:


### Autenticação

#### Signin

```
POST {{base_url}}/auth/signin

{
    "email": "user1@example.com",
    "password": "pass123456"
}
```

- Autenticação: Retorna o Bearer Token

#### Signup

```
POST {{base_url}}/auth/signup

{
    "name": "user test 1",
    "email": "user1@example.com",
    "password": "pass123456"
}
```

- Chama uma classe de Email Service mock que enviaria um email ao novo usuário. Processo assíncrono para não travar a operação transacional no banco.
- Necessita de token de autenticação.

### Gerenciamento de Usuários

#### Listar Usuários

```
GET {{base_url}}/users?page=0&size=10&sortField=name&sortDirection=asc&name=test
```

- Necessita de token de autenticação.
- Pode realizar pesquisa sem informar nome ou parte do nome. Também pode paginar a pesquisa e definir a ordem pelos campos **nome**, **email** ou **id**.

#### Obter por ID

```
GET {{base_url}}/users/5
```

- Necessita de token de autenticação.

### Pontos de Melhoria

- Adicionar um logstash para enviar os logs ao Kibana, poderia ter um local no docker compose.
- Adicionar um lock para o verbo POST no método de signup devido a ele não ser idempotente, evitando duplicações e condição de corrida (vide projeto condição de corrida em meu repositório GIT), apesar de ter sido colocado um unique por email na User.
- Criar apenas testes para os pontos importantes e principais do sistema. Também criar testes de integração validando estes pontos.

### Qual Tecnologia Utilizar para Front Endpoints

Estou considerando o Next.js para o frontend. Realmente me chama atenção pela excelente documentação e atualizações frequentes. A maneira como ele organiza os pacotes e componentes é bastante intuitiva, o que me atrai. Embora eu tenha analisado outras opções como Vue.js e Angular, o Next.js se destaca para mim. Claro, reconheço que pode haver uma curva de aprendizado inicial para quem não está familiarizado com o React, mas estou confiante de que, uma vez superada essa fase, o Next.js oferecerá uma base sólida e escalável para o projeto.