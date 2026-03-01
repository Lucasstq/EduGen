# EduGen

Gerador de atividades escolares que combina Spring Boot, Spring AI e modelos Google Gemini para criar, versionar e distribuir listas de exercícios em PDF para professores e estudantes.

## Visão geral
- **Propósito:** permitir que docentes criem atividades alinhadas ao currículo, com personalização de tema, série, dificuldade e tipo de questão.
- **Fluxo:** o usuário cadastra a atividade via API segura (JWT), o serviço chama o Gemini para montar o enunciado em JSON, converte para templates Thymeleaf e entrega PDFs distintos para alunos e professores.
- **Automação:** cada versão é rastreada com seed, status e arquivos persistidos em disco local, possibilitando downloads posteriores.

## Principais recursos
1. **Geração com IA**: prompt ajustado (`AiWorksheetService`) garante JSON consistente com número e tipo de questões definidos.
2. **Versionamento**: múltiplas versões por atividade com estados `DRAFT`, `GENERATED` e `FAILED`, inclusive filtros por assunto.
3. **Renderização em PDF**: templates `worksheet_student.html` e `worksheet_teacher.html` (Thymeleaf) combinados ao `openhtmltopdf` para produzir arquivos diferenciados por audiência.
4. **Armazenamento local**: PDFs físicos ficam em `pdfs/` com chave gerenciada por `StorageService`.
5. **Autenticação e segurança**: fluxo OAuth2/JWT com chaves `public.pem` e `private.pem`. Endpoints consultam `userId` no token.
6. **Integração PostgreSQL/Flyway**: persistência relacional e migrações versionáveis (pasta `src/main/resources/db/migration`).

## Stack principal
- Java 17 + Spring Boot 3.5
- Spring Data JPA, Spring Security, OAuth2 Client
- Spring AI + Google Gemini 2.5 Flash
- PostgreSQL 16 (Docker) + Flyway
- Thymeleaf + OpenHTMLtoPDF

## Pré-requisitos
- Java 17 SDK
- Maven Wrapper (`./mvnw`) ou Maven 3.9+
- Docker + Docker Compose (opcional, para subir PostgreSQL)
- Conta Google AI Studio (chave Gemini)

## Variáveis de ambiente
| Variável | Descrição |
| --- | --- |
| `POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD` | Credenciais usadas pelo `docker-compose.yml` e por `spring.datasource` |
| `GEMINI_API_KEY` | Chave do modelo Gemini usada em `spring.ai.google.genai.api-key` |
| `JWT_ACCESS_TOKEN_EXPIRATION`, `JWT_REFRESH_TOKEN_EXPIRATION` *(opcionais)* | Sobrescrevem valores padrão definidos em `application.yaml`, se necessário |

Configure-as em um `.env` compartilhado pelo Docker Compose e/ou exporte no shell antes de iniciar a aplicação.

## Configuração local
1. **Clonar o repositório**
   ```bash
   git clone <url>
   cd EduGen
   ```
2. **Preparar o banco** (opcional, se já houver PostgreSQL configurado)
   ```bash
   docker compose up -d
   ```
3. **Garanta chaves JWT**: os arquivos `src/main/resources/public.pem` e `private.pem` já existem; substitua-os se precisar de pares distintos.
4. **Atualize `application.yaml`** se desejar outro diretório para PDFs (`storage.base-path`) ou hosts de CORS.

## Execução
- **Aplicação Spring Boot**
  ```bash
  ./mvnw spring-boot:run
  ```
- **Build empacotado**
  ```bash
  ./mvnw clean package
  java -jar target/EduGen-0.0.1-SNAPSHOT.jar
  ```
A API inicia, por padrão, em `http://localhost:8080` e se conecta ao banco configurado.

## Estrutura do projeto
```
.
├── docker-compose.yml               # PostgreSQL 16 pronto para uso
├── pdfs/                            # PDFs gerados (students/teachers)
├── src/
│   ├── main/java/dev/lucas/edugen   # Domínio, serviços, controllers
│   │   ├── controller               # REST (Authentication, User, Worksheet)
│   │   ├── service/worksheet        # Regras de negócio e integração AI
│   │   └── service/pdf              # Renderização e armazenamento dos PDFs
│   └── main/resources
│       ├── templates                # Modelos Thymeleaf (aluno/professor)
│       ├── db/migration             # Scripts Flyway
│       └── application.yaml         # Configuração central
└── target/                          # Artefatos Maven
```

## Fluxo funcional
1. Usuário autenticado cria uma `Worksheet` via `POST /api/worksheets`.
2. Um `WorksheetVersion` é gerado (`POST /api/worksheets/{id}/versions`) e dispara o `AiWorksheetService`.
3. O JSON retornado é validado, mapeado e persistido; então `PdfService` renderiza duas variantes (aluno/professor) e salva no storage local.
4. O cliente pode baixar o PDF (`GET /api/worksheets/versions/{versionId}/download?audience=STUDENTS|TEACHERS`) ou consultar o JSON (`GET /api/worksheets/versions/{versionId}/spec`).

## Endpoints essenciais
| Método | Caminho | Descrição |
| --- | --- | --- |
| `POST` | `/api/worksheets` | Cria nova atividade com metadados (assunto, série, quantidade de questões). |
| `POST` | `/api/worksheets/{id}/versions` | Gera versão baseada no Gemini; permite configurar respostas/explicações. |
| `GET` | `/api/worksheets` | Lista atividades do usuário com paginação e filtro por disciplina. |
| `GET` | `/api/worksheets/versions/{versionId}/spec` | Retorna o JSON da versão. |
| `GET` | `/api/worksheets/versions/{versionId}/download` | Download do PDF filtrando por `Audience`. |
| `DELETE` | `/api/worksheets/{id}` | Remove atividade pertencente ao usuário. |

Todos os endpoints exigem JWT válido com claim `userId`.

## Testes
Execute a suíte integrada (JUnit + Spring Boot Test):
```bash
./mvnw test
```

