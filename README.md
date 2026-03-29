<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_AI-1.1-6DB33F?style=for-the-badge&logo=spring&logoColor=white" />
  <img src="https://img.shields.io/badge/Gemini_2.5_Flash-4285F4?style=for-the-badge&logo=googlegemini&logoColor=white" />
  <img src="https://img.shields.io/badge/PostgreSQL-16-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" />
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" />
  <img src="https://img.shields.io/badge/Status-Hospedado-brightgreen?style=for-the-badge" />
</p>

<h1 align="center">📝 EduGen — Back-end</h1>

<p align="center">
  <strong>Crie atividades escolares personalizadas com Inteligência Artificial.</strong><br/>
  API REST que combina Spring Boot, Spring AI e Google Gemini para gerar, versionar e distribuir listas de exercícios em PDF para professores e estudantes.
</p>

<p align="center">
  <a href="https://edugen-app.vercel.app">🌐 Aplicação em Produção</a> ·
  <a href="#-endpoints-principais">📡 Endpoints</a> ·
  <a href="#-stack-tecnológica">🛠 Stack</a>
</p>

---

## 🌐 Hospedagem

O EduGen está **hospedado e disponível em produção**:

| Camada | Tecnologia | URL |
|--------|-----------|-----|
| **Front-end** | React/Vite / Vercel | [`edugen-app.vercel.app`](https://edugen-app.vercel.app) |
| **Back-end (API)** | Spring Boot / VPS + Docker | [`edugen.duckdns.org`](https://edugen.duckdns.org) |
| **Banco de dados** | PostgreSQL 16 | Container Docker na mesma VPS |
| **Reverse Proxy / HTTPS** | Caddy 2 | Certificado TLS automático via Let's Encrypt |

---

## 📋 Visão Geral

O **EduGen** permite que docentes criem atividades alinhadas ao currículo, personalizando **tema, série, dificuldade e tipo de questão**. A IA gera o conteúdo, o sistema renderiza PDFs diferenciados (aluno vs. professor) e armazena tudo de forma versionada.

### Fluxo Funcional

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│   Usuário    │────▶│  API (JWT)   │────▶│ Google Gemini│────▶│  PDF Engine  │
│ autenticado  │     │  REST        │     │  2.5 Flash   │     │  Thymeleaf + │
│              │◀────│              │◀────│  (Spring AI) │◀────│  OpenHTML    │
└──────────────┘     └──────────────┘     └──────────────┘     └──────────────┘
                            │                                         │
                            ▼                                         ▼
                     ┌──────────────┐                          ┌──────────────┐
                     │ PostgreSQL   │                          │  Storage     │
                     │ + Flyway     │                          │  Local (PDF) │
                     └──────────────┘                          └──────────────┘
```

1. Usuário autenticado cria uma `Worksheet` via `POST /api/worksheets`.
2. Um `WorksheetVersion` é gerado (`POST /api/worksheets/{id}/versions`) e dispara o `AiWorksheetService`.
3. O JSON retornado pelo Gemini é validado, mapeado e persistido; então o `PdfService` renderiza duas variantes (aluno/professor) e salva no storage.
4. O cliente pode baixar o PDF ou consultar o JSON da versão gerada.

---

## 🚀 Principais Recursos

| Recurso | Descrição |
|---------|-----------|
| 🤖 **Geração com IA** | Prompt engineering ajustado para JSON consistente com número e tipo de questões definidos pelo professor |
| 📚 **Versionamento** | Múltiplas versões por atividade com estados `DRAFT`, `GENERATED` e `FAILED` |
| 📄 **PDFs diferenciados** | Templates Thymeleaf + OpenHTMLtoPDF geram versões distintas para alunos (sem gabarito) e professores (com gabarito e explicações) |
| 💾 **Armazenamento** | PDFs persistidos em disco com chave gerenciada pelo `StorageService` |
| 🔐 **Segurança** | OAuth2/JWT com chaves RSA (RS256). Todos os endpoints exigem token válido |
| 🗄️ **Migrações** | PostgreSQL com Flyway — schema versionado e reproduzível |
| 📧 **E-mail** | Integração com Spring Mail para notificações |
| 🐳 **Docker-ready** | Multi-stage build otimizado + Docker Compose com Caddy, App e PostgreSQL |

---

## 🛠 Stack Tecnológica

| Categoria | Tecnologias |
|-----------|-------------|
| **Linguagem** | Java 17 |
| **Framework** | Spring Boot 3.5, Spring Data JPA, Spring Security, Spring AI 1.1 |
| **IA** | Google Gemini 2.5 Flash (via Spring AI) |
| **Banco de dados** | PostgreSQL 16 + Flyway |
| **Geração de PDF** | Thymeleaf + OpenHTMLtoPDF |
| **Autenticação** | OAuth2 Resource Server + JWT (RS256) |
| **Infra / Deploy** | Docker, Docker Compose, Caddy 2 (reverse proxy + TLS) |
| **Build** | Maven Wrapper |

---

## 📁 Estrutura do Projeto

```
.
├── Caddyfile                         # Configuração do reverse proxy (HTTPS automático)
├── Dockerfile                        # Multi-stage build (JDK → JRE)
├── docker-compose.yml                # Caddy + App + PostgreSQL
├── pdfs/                             # PDFs gerados (students/teachers)
├── src/
│   ├── main/java/dev/lucas/edugen
│   │   ├── config/                   # Configurações (Security, CORS, JWT)
│   │   ├── controller/               # REST Controllers (Auth, User, Worksheet)
│   │   ├── domain/                   # Entidades JPA (Worksheet, Version, Question...)
│   │   ├── dtos/                     # Data Transfer Objects
│   │   ├── mapper/                   # Mapeamento entidade ↔ DTO
│   │   ├── repository/               # Spring Data Repositories
│   │   ├── service/
│   │   │   ├── auth/                 # Autenticação e tokens
│   │   │   ├── pdf/                  # Renderização de PDFs
│   │   │   ├── storage/              # Persistência de arquivos
│   │   │   └── worksheet/            # Regras de negócio + integração AI
│   │   └── eduGenException/          # Exceções customizadas
│   └── main/resources/
│       ├── templates/                # Thymeleaf (worksheet_student/teacher)
│       ├── db/migration/             # Scripts Flyway
│       ├── application.yaml          # Configuração principal
│       └── application-prod.yaml     # Configuração de produção
└── pom.xml
```

---

## 📡 Endpoints Principais

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/api/worksheets` | Cria nova atividade (assunto, série, nº de questões) |
| `POST` | `/api/worksheets/{id}/versions` | Gera versão com IA (Gemini) |
| `GET` | `/api/worksheets` | Lista atividades do usuário (paginação + filtros) |
| `GET` | `/api/worksheets/versions/{versionId}/spec` | Retorna o JSON da versão gerada |
| `GET` | `/api/worksheets/versions/{versionId}/download?audience=STUDENTS\|TEACHERS` | Download do PDF |
| `DELETE` | `/api/worksheets/{id}` | Remove atividade |

> Todos os endpoints exigem JWT válido com claim `userId`.

---

## ⚙️ Variáveis de Ambiente

| Variável | Descrição |
|----------|-----------|
| `POSTGRES_DB` | Nome do banco de dados |
| `POSTGRES_USER` | Usuário do PostgreSQL |
| `POSTGRES_PASSWORD` | Senha do PostgreSQL |
| `GEMINI_API_KEY` | Chave da API Google Gemini |
| `CORS_ALLOWED_ORIGIN` | Origem permitida pelo CORS (ex: `https://edugen-app.vercel.app`) |
| `SPRING_PROFILES_ACTIVE` | Perfil ativo (`prod` em produção) |

---

## 🐳 Deploy com Docker

```bash
# Clone o repositório
git clone <url>
cd EduGen

# Configure as variáveis de ambiente
cp .env.example .env
# Edite o .env com suas credenciais

# Suba tudo (Caddy + App + PostgreSQL)
docker compose up -d
```

O Caddy configura automaticamente o certificado HTTPS via Let's Encrypt.

---

## 💻 Desenvolvimento Local

```bash
# 1. Suba apenas o banco
docker compose up -d postgres

# 2. Execute a aplicação
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

---

## 🧪 Testes

```bash
./mvnw test
```

---

## ⚠️ Aviso

> Este repositório é público **exclusivamente para fins de portfólio**. O código-fonte está disponível para visualização e referência, mas **não é permitido**:
>
> - Clonar, copiar ou redistribuir este projeto (total ou parcialmente).
> - Utilizar o código como base para outros projetos.
> - Realizar forks com intenção de uso, modificação ou distribuição.
>
> Caso tenha interesse em discutir o projeto ou colaborar, entre em contato diretamente.

---

## 📜 Licença

**All Rights Reserved** — Todos os direitos reservados. Este projeto não possui licença open-source. A disponibilização pública do código não concede qualquer permissão de uso, cópia, modificação ou distribuição.

---

<p align="center">
  Desenvolvido por <strong>Lucas Emanuel</strong>
</p>

