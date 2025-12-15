# Desafio Técnico – Gestão de Eventos

Aplicação **full-stack** desenvolvida em **24 horas** para gerenciamento de eventos, utilizando **Java + Spring Boot (backend)**, **Angular (frontend)**, **PostgreSQL**, **Docker** e boas práticas de arquitetura, versionamento e conteinerização.

> ✅ **Basta rodar `docker-compose up --build` e a aplicação estará pronta!**

---

## 📋 Funcionalidades

- ✅ Cadastrar, listar, editar e excluir eventos  
- 🗓️ Validação de data/hora futura  
- 🔍 Paginação na listagem (frontend e backend)  
- 🗑️ *Soft delete*: eventos não são removidos fisicamente  
- 🌐 Internacionalização preparada (pt)  
- 📦 Entrega via Docker (sem necessidade de instalar Java, Node ou banco localmente)

---

## 🛠️ Tecnologias Utilizadas

| Camada       | Tecnologia |
|-------------|------------|
| **Backend** | Java 17, Spring Boot, Spring Data JPA, Spring Validation, Springdoc OpenAPI, Flyway |
| **Frontend** | Angular 17+, Reactive Forms, Angular Material, ngx-pagination |
| **Banco** | PostgreSQL 15 |
| **Infra** | Docker, Docker Compose, Nginx |
| **Testes** | JUnit 5, Mockito, Testcontainers (H2 para testes de integração) |

---

## 🚀 Como Executar

### Pré-requisitos
- Docker  
- Docker Compose  

### Passos

```bash
git clone https://github.com/thiagosbene/desafio_evento.git
cd desafio_evento
docker-compose up --build
```

### Acesso

- **Frontend:** http://localhost  
- **API Docs (Swagger):** http://localhost:8080/swagger-ui.html  

✨ Toda a aplicação é autocontida: o backend inicia o banco, aplica migrations (Flyway) e expõe a API. O frontend é servido via Nginx.

---

## 📂 Estrutura do Projeto

```
desafio_evento/
├── backend/                 # Spring Boot (Java 17)
│   ├── src/main/java
│   │   ├── controller/      # REST endpoints
│   │   ├── service/         # Lógica de negócio
│   │   ├── repository/      # Spring Data JPA
│   │   ├── dto/             # DTOs de entrada/saída
│   │   └── entity/          # Entidades JPA com soft delete
│   └── Dockerfile           # Multi-stage build
├── frontend/                # Angular 16+
│   ├── src/app
│   │   ├── core/            # Serviços globais
│   │   ├── shared/          # Componentes compartilhados
│   │   └── events/          # Feature module (CRUD de eventos)
│   └── Dockerfile           # Build Angular + Nginx
├── docker-compose.yml       # Orquestração
├── .env                     # Variáveis de ambiente
└── README.md
```

---

## 🧪 Testes

- 3+ testes de unidade (Service/Repository com Mockito)  
- 1 teste de integração com `@SpringBootTest` e H2 em memória  
- Executados automaticamente no build Docker (Maven)

---

## 📡 Endpoints da API (`/api/events`)

| Método | Endpoint | Descrição |
|------|---------|-----------|
| GET | `/api/events` | Lista eventos (paginação `?page=0&size=10`) |
| GET | `/api/events/{id}` | Detalhes de um evento |
| POST | `/api/events` | Cria novo evento |
| PUT | `/api/events/{id}` | Atualiza evento |
| DELETE | `/api/events/{id}` | Soft delete |

✔️ Validações com Bean Validation  
✔️ Tratamento global de erros com `@ControllerAdvice`  

---

## 📄 Documentação da API

A API está documentada com **OpenAPI 3.0** via `springdoc-openapi`.

👉 http://localhost/api/swagger-ui.html

---

## 🌐 Frontend – Rotas

- `/events` – Lista paginada  
- `/events/new` – Criação  
- `/events/:id/edit` – Edição  
- `/events/:id` – Detalhes  

✔️ Formulários reativos  
✔️ Feedback visual com Angular Material  

---

## 🐳 Dockerização

- Backend com **multi-stage build**
- Frontend Angular servido via **Nginx**
- PostgreSQL com **volume persistente**
- Rede interna do Docker Compose
- Variáveis de ambiente via `.env`

---

## 📌 Boas Práticas Aplicadas

- Commits atômicos com Conventional Commits  
- Separação clara de camadas  
- DTOs para entrada/saída  
- Soft delete em vez de remoção física  

---

## 📬 Desenvolvido por

**Thiago Benevides**  
GitHub: https://github.com/thiagosbene  

Projeto criado como parte do **Desafio Técnico (24h)** – Dezembro/2025
