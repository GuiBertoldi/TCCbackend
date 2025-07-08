
# **README - Backend do Sistema de Gerenciamento de Prontuários Psicológicos e Agendamento de Consultas

## 1. **Introdução**

### 1.1. **Contexto**
Este repositório contém o **backend** do **Sistema de Gerenciamento de Prontuários Psicológicos e Agendamento de Consultas**, desenvolvido em **Spring Boot** com **Java**. O sistema lida com a lógica de negócios, processamento de dados e comunicação com o banco de dados, permitindo o gerenciamento de prontuários, agendamento de consultas e controle de horários de psicólogos.

### 1.2. **Justificativa**
O objetivo é fornecer uma plataforma para psicólogos, garantindo o gerenciamento eficiente de prontuários e consultas, e permitindo a integração de funcionalidades administrativas como o controle de horários. Além disso, o backend foi projetado para ser escalável e seguro.

### 1.3. **Objetivo**
Automatizar e digitalizar os processos administrativos de psicólogos, como:
- Criação e atualização de prontuários de pacientes.
- Agendamento e visualização de sessões de psicologia.
- Gerenciamento de horários dos psicólogos.

---

## 2. **Tecnologias Utilizadas**

- **Spring Boot**: Framework Java para construção da API.
- **Spring Data JPA**: Para interação com o banco de dados (PostgreSQL).
- **JUnit**: Framework de testes unitários para garantir a qualidade do código.
- **Docker**: Para containerização do backend.
- **AWS EC2**: Para deploy do backend na infraestrutura em nuvem.
- **Datadog**: Para monitoramento de performance e logs.

---

## 3. **Montagem do Ambiente Local**

### 3.1. **Pré-requisitos**

Antes de rodar o backend localmente, você precisa ter as seguintes ferramentas instaladas:

- **Java 17** (OpenJDK)
- **Maven** ou **Gradle** (para gerenciar dependências e construir o projeto)
- **PostgreSQL**

### 3.2. **Clonando o Repositório**

Clone o repositório para o seu ambiente local:

```bash
git clone https://github.com/GuiBertoldi/TCCbackend.git
cd TCCbackend
```

### 3.3. **Instalação das Dependências**

Se você estiver utilizando o Maven:

```bash
mvn clean install
```

### 3.4. **Configuração do Banco de Dados**


Configure as credenciais no arquivo `application.properties` ou `application.yml`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/yourdbname
spring.datasource.username=postgres
spring.datasource.password=password
```

### 3.5. **Rodando o Backend Localmente**

Para rodar o backend, use o comando:

```bash
mvn spring-boot:run
```

O backend será iniciado no endereço `http://localhost:8080`.

---

## 4. **CI/CD com GitHub Actions**

O repositório utiliza **GitHub Actions** para automação de **build**, **testes**, **deploy** e **monitoramento** do backend.

### 4.1. **Fluxo de Trabalho do GitHub Actions**

1. **Commit e Push**:
    - Quando um desenvolvedor faz um commit e push para o repositório, o GitHub Actions inicia o fluxo de integração contínua (CI).

2. **Build e Testes**:
    - O **GitHub Actions** realiza a **compilação** do código e executa os **testes unitários** (usando **JUnit**).
    - Se todos os testes passarem, o código é aprovado para o próximo passo.

3. **Containerização com Docker**:
    - O código é **containerizado** utilizando o Docker.
    - O **Dockerfile** presente no repositório configura a construção da imagem Docker, permitindo rodar o backend dentro de um container.

4. **Deploy na AWS EC2**:
    - Após a containerização, o código é **implantado** em uma **instância EC2** na AWS.
    - Isso garante que o backend esteja disponível em um ambiente de produção escalável e robusto.

5. **Monitoramento com Datadog**:
    - O **Datadog** é configurado para monitorar a aplicação, coletando métricas sobre o desempenho e disponibilizando visualizações para acompanhar o uso da aplicação.


## 5. **Considerações de Segurança**

### 5.1. **Armazenamento Seguro de Dados**
- **Criptografia de Senhas**: As senhas de usuários e outras informações sensíveis devem ser criptografadas usando algoritmos seguros.
- **HTTPS**: Toda comunicação entre o frontend e o backend deve ser feita sobre **HTTPS** para garantir a segurança dos dados transmitidos.
- **LGPD**: Por ora, o sistema ainda não está de acordo com a LGPD nem com as regras específicas de sistemas de saúde, que são regras rígidas e especificas para esse determinado nicho. É necessário um tratamento de dados mais rigoroso, pois lida com dados sensíveis e confidenciais de paciente/psicólogo. Será o principal ponto a ser focado no fururo, para que se torne um produto viável. No momento o sistema é um MVP, que não trata dados reais, por conta da necessidade da aplicação das regras da LGPD.
  

---

## 6. **Considerações de Deploy**

### 6.1. **AWS EC2**
O backend é **deployado** em uma instância **AWS EC2**. Após o deploy, a instância é configurada para rodar o **Docker** e garantir que a aplicação esteja acessível ao público.

### 6.2. **Datadog**
O **Datadog** é configurado para monitorar a aplicação, coletando métricas sobre o desempenho e disponibilizando visualizações para acompanhar o uso da aplicação.

---

## 7. **Referências**

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Docker Documentation](https://docs.docker.com/)
- [AWS EC2 Documentation](https://aws.amazon.com/ec2/)
- [Datadog Documentation](https://www.datadoghq.com/)
- [JUnit Documentation](https://junit.org/)
