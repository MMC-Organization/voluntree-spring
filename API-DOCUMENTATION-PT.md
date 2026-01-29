# Documentação da API - Voluntree Backend

## Visão Geral

Esta é a documentação completa da API REST do backend Voluntree para integração com o frontend Angular.

**Base URL (Desenvolvimento):** `http://localhost:8080/api`
**Base URL (Produção):** `https://[seu-dominio]/api`

**Tecnologia:** Spring Boot 4.0.1 com Spring Security
**Banco de Dados:** PostgreSQL
**Autenticação:** Session-based (Redis)

---

## Autenticação e Segurança

### Modelo de Autenticação
O sistema utiliza autenticação baseada em sessão com Redis. Após o login bem-sucedido, um cookie de sessão é automaticamente gerenciado pelo navegador.

### Headers Necessários
```
Content-Type: application/json
X-CSRF-TOKEN: [token-csrf] (obrigatório para requisições POST, PUT, PATCH, DELETE)
```

### Obter Token CSRF
```http
GET /api/auth/csrf
```

**Response:**
```json
{
  "token": "string",
  "headerName": "X-CSRF-TOKEN",
  "parameterName": "_csrf"
}
```

### Tipos de Usuário
- **VOLUNTEER** - Voluntário
- **ORGANIZATION** - Organização

---

## Endpoints de Autenticação

### 1. Login
```http
POST /api/auth/login
```

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "SenhaForte123!"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Usuário autenticado com sucesso!"
}
```

**Response (400 Bad Request):**
```json
{
  "success": false,
  "message": "E-mail ou senha inválidos!"
}
```

---

### 2. Verificar Status de Autenticação
```http
GET /api/auth
```

**Response (200 OK):**
```json
{
  "status": true,
  "message": "Usuário autenticado",
  "userType": "VOLUNTEER"
}
```

**Response (401 Unauthorized):**
```json
{
  "status": false,
  "message": "Usuário não autenticado",
  "userType": null
}
```

---

### 3. Cadastro de Voluntário
```http
POST /api/auth/signup/volunteer
```

**Request Body:**
```json
{
  "name": "João Silva",
  "email": "joao@example.com",
  "password": "SenhaForte123!",
  "phoneNumber": "+55 11 98765-4321",
  "cep": "01310100",
  "number": "123",
  "cpf": "12345678901"
}
```

**Validações:**
- `name`: máximo 150 caracteres, obrigatório
- `email`: formato válido de e-mail, obrigatório
- `password`: 8-20 caracteres, deve conter maiúsculas, minúsculas, números e símbolos
- `phoneNumber`: máximo 25 caracteres, obrigatório
- `cep`: exatamente 8 dígitos numéricos
- `number`: máximo 10 caracteres
- `cpf`: exatamente 11 dígitos numéricos

**Response (201 Created):**
```json
{
  "id": 1,
  "name": "João Silva",
  "email": "joao@example.com",
  "message": "Voluntário cadastrado com sucesso!"
}
```

---

### 4. Cadastro de Organização
```http
POST /api/auth/signup/organization
```

**Request Body:**
```json
{
  "name": "Maria Santos",
  "email": "contato@ong.org.br",
  "password": "SenhaForte123!",
  "phoneNumber": "+55 11 98765-4321",
  "cep": "01310100",
  "number": "456",
  "cnpj": "12345678000190",
  "companyName": "ONG Exemplo",
  "cause": "Ajuda a comunidades carentes"
}
```

**Validações:**
- `name`: máximo 150 caracteres, obrigatório
- `email`: formato válido de e-mail, obrigatório
- `password`: 8-20 caracteres, deve conter maiúsculas, minúsculas, números e símbolos
- `phoneNumber`: máximo 25 caracteres, obrigatório
- `cep`: exatamente 8 dígitos numéricos
- `number`: máximo 10 caracteres
- `cnpj`: exatamente 14 dígitos numéricos
- `companyName`: máximo 255 caracteres, obrigatório
- `cause`: máximo 1000 caracteres, opcional

**Response (201 Created):**
```json
{
  "id": 2,
  "name": "Maria Santos",
  "email": "contato@ong.org.br",
  "companyName": "ONG Exemplo",
  "message": "Organização cadastrada com sucesso!"
}
```

---

## Endpoints de Usuário

### 1. Obter Perfil do Usuário Autenticado
```http
GET /api/user/me
```

**Requer:** Usuário autenticado

**Response (200 OK) - Voluntário:**
```json
{
  "name": "João Silva",
  "email": "joao@example.com",
  "phoneNumber": "+55 11 98765-4321",
  "cep": "01310100",
  "number": "123",
  "userType": "VOLUNTEER",
  "cpf": "12345678901",
  "cnpj": null,
  "companyName": null,
  "cause": null
}
```

**Response (200 OK) - Organização:**
```json
{
  "name": "Maria Santos",
  "email": "contato@ong.org.br",
  "phoneNumber": "+55 11 98765-4321",
  "cep": "01310100",
  "number": "456",
  "userType": "ORGANIZATION",
  "cpf": null,
  "cnpj": "12345678000190",
  "companyName": "ONG Exemplo",
  "cause": "Ajuda a comunidades carentes"
}
```

---

### 2. Atualizar Perfil
```http
PUT /api/user/me
```

**Requer:** Usuário autenticado

**Request Body:**
```json
{
  "name": "João Silva Santos",
  "email": "joao.novo@example.com",
  "phoneNumber": "+55 11 99999-9999",
  "cep": "01310200",
  "number": "456"
}
```

**Response (204 No Content)**

---

### 3. Atualizar Senha
```http
PATCH /api/user/me/password
```

**Requer:** Usuário autenticado

**Request Body:**
```json
{
  "currentPassword": "SenhaAntiga123!",
  "newPassword": "SenhaNova456!"
}
```

**Validações:**
- `newPassword`: 8-20 caracteres, deve conter maiúsculas, minúsculas, números e símbolos

**Response (204 No Content)**

---

## Endpoints de Atividades

### 1. Criar Atividade
```http
POST /api/activity
```

**Requer:** Usuário autenticado como ORGANIZATION

**Request Body:**
```json
{
  "name": "Distribuição de Alimentos",
  "description": "Atividade de distribuição de alimentos para famílias carentes",
  "spots": 20,
  "cep": "01310100",
  "number": "789",
  "activityDate": "2026-03-15T14:00:00"
}
```

**Validações:**
- `name`: máximo 200 caracteres, obrigatório
- `description`: máximo 5000 caracteres, opcional
- `spots`: número positivo, obrigatório
- `cep`: 8 dígitos numéricos, obrigatório
- `number`: máximo 10 caracteres, opcional
- `activityDate`: data no futuro, obrigatório

**Response (201 Created):**
```json
{
  "id": 1,
  "name": "Distribuição de Alimentos",
  "description": "Atividade de distribuição de alimentos para famílias carentes",
  "spots": 20,
  "cep": "01310100",
  "number": "789",
  "activityDate": "2026-03-15T14:00:00",
  "organizationId": 2,
  "organizationName": "Maria Santos",
  "organizationCompanyName": "ONG Exemplo",
  "canceled": false
}
```

---

### 2. Obter Atividade por ID
```http
GET /api/activity/{id}
```

**Requer:** Qualquer usuário (público)

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "Distribuição de Alimentos",
  "description": "Atividade de distribuição de alimentos para famílias carentes",
  "spots": 20,
  "cep": "01310100",
  "number": "789",
  "activityDate": "2026-03-15T14:00:00",
  "organizationId": 2,
  "organizationName": "Maria Santos",
  "organizationCompanyName": "ONG Exemplo",
  "canceled": false
}
```

---

### 3. Listar Todas as Atividades (Paginado)
```http
GET /api/activity?page=0&size=20&sort=activityDate,desc
```

**Parâmetros de Query:**
- `page`: número da página (default: 0)
- `size`: tamanho da página (default: 20)
- `sort`: campo de ordenação (default: activityDate)

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Distribuição de Alimentos",
      "description": "Atividade de distribuição de alimentos",
      "spots": 20,
      "cep": "01310100",
      "activityDate": "2026-03-15T14:00:00",
      "organizationName": "ONG Exemplo",
      "canceled": false
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 5,
  "totalElements": 100,
  "last": false,
  "first": true,
  "size": 20,
  "number": 0,
  "numberOfElements": 20,
  "empty": false
}
```

---

### 4. Listar Atividades Futuras (Paginado)
```http
GET /api/activity/upcoming?page=0&size=20
```

**Descrição:** Retorna apenas atividades que ainda não ocorreram

**Parâmetros de Query:** Mesmos da listagem geral

**Response:** Mesmo formato da listagem geral

---

### 5. Listar Atividades por Organização (Paginado)
```http
GET /api/activity/organization/{organizationId}?page=0&size=20
```

**Parâmetros de Path:**
- `organizationId`: ID da organização

**Response:** Mesmo formato da listagem geral

---

### 6. Listar Minhas Atividades (Organização)
```http
GET /api/activity/my-activities?page=0&size=20
```

**Requer:** Usuário autenticado como ORGANIZATION

**Descrição:** Retorna atividades criadas pela organização autenticada

**Response:** Mesmo formato da listagem geral

---

### 7. Atualizar Atividade
```http
PUT /api/activity/{id}
```

**Requer:** Usuário autenticado como ORGANIZATION (proprietário da atividade)

**Request Body:**
```json
{
  "name": "Distribuição de Alimentos - ATUALIZADA",
  "description": "Descrição atualizada",
  "spots": 25,
  "cep": "01310100",
  "number": "789",
  "activityDate": "2026-03-15T15:00:00"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "Distribuição de Alimentos - ATUALIZADA",
  "description": "Descrição atualizada",
  "spots": 25,
  "cep": "01310100",
  "number": "789",
  "activityDate": "2026-03-15T15:00:00",
  "organizationId": 2,
  "organizationName": "Maria Santos",
  "organizationCompanyName": "ONG Exemplo",
  "canceled": false
}
```

---

### 8. Cancelar Atividade
```http
PATCH /api/activity/{id}/cancel
```

**Requer:** Usuário autenticado como ORGANIZATION (proprietário da atividade)

**Descrição:** Marca a atividade como cancelada (soft delete)

**Response (204 No Content)**

---

### 9. Deletar Atividade
```http
DELETE /api/activity/{id}
```

**Requer:** Usuário autenticado como ORGANIZATION (proprietário da atividade)

**Descrição:** Remove permanentemente a atividade

**Response (204 No Content)**

---

## Endpoints de Inscrições (Registrations)

### 1. Inscrever-se em uma Atividade
```http
POST /api/registration/activity/{activityId}
```

**Requer:** Usuário autenticado como VOLUNTEER

**Parâmetros de Path:**
- `activityId`: ID da atividade

**Response (200 OK):**
```json
"Inscrição realizada com sucesso!"
```

**Response (400 Bad Request):**
```json
"Mensagem de erro específica"
```

**Possíveis Erros:**
- Atividade não encontrada
- Atividade já ocorreu
- Atividade cancelada
- Sem vagas disponíveis
- Já inscrito nesta atividade

---

### 2. Cancelar Inscrição
```http
DELETE /api/registration/activity/{activityId}
```

**Requer:** Usuário autenticado como VOLUNTEER

**Parâmetros de Path:**
- `activityId`: ID da atividade

**Response (200 OK):**
```json
"Inscrição cancelada."
```

---

### 3. Listar Minhas Inscrições
```http
GET /api/registration/my
```

**Requer:** Usuário autenticado como VOLUNTEER

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "activityId": 1,
    "activityName": "Distribuição de Alimentos",
    "volunteerName": "João Silva",
    "registeredAt": "2026-01-15T10:30:00Z",
    "canceled": false
  },
  {
    "id": 2,
    "activityId": 3,
    "activityName": "Limpeza de Praia",
    "volunteerName": "João Silva",
    "registeredAt": "2026-01-16T14:20:00Z",
    "canceled": false
  }
]
```

---

## Endpoints de Auditoria

### 1. Obter Histórico de Auditoria do Usuário
```http
GET /api/audit/history?page=0&size=10
```

**Requer:** Usuário autenticado

**Parâmetros de Query:**
- `page`: número da página (default: 0)
- `size`: tamanho da página (default: 10)

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "userId": 1,
      "action": "CREATE_ACTIVITY",
      "entityType": "Activity",
      "entityId": 5,
      "timestamp": "2026-01-29T12:00:00Z",
      "details": "Criada atividade: Distribuição de Alimentos"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalPages": 3,
  "totalElements": 25,
  "last": false,
  "first": true
}
```

---

## Tratamento de Erros

### Estrutura de Resposta de Erro

**Erro de Validação (400 Bad Request):**
```json
{
  "timestamp": "2026-01-29T13:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Erro de validação",
  "errors": {
    "email": "E-mail inválido",
    "password": "A senha deve ter entre 8 e 20 caracteres"
  }
}
```

**Erro de Autorização (401 Unauthorized):**
```json
{
  "timestamp": "2026-01-29T13:00:00Z",
  "status": 401,
  "error": "Unauthorized",
  "message": "Usuário não autenticado"
}
```

**Erro de Permissão (403 Forbidden):**
```json
{
  "timestamp": "2026-01-29T13:00:00Z",
  "status": 403,
  "error": "Forbidden",
  "message": "Acesso negado"
}
```

**Recurso Não Encontrado (404 Not Found):**
```json
{
  "timestamp": "2026-01-29T13:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Atividade não encontrada"
}
```

**Erro Interno do Servidor (500 Internal Server Error):**
```json
{
  "timestamp": "2026-01-29T13:00:00Z",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Erro interno do servidor"
}
```

---

## Configuração CORS

O backend está configurado para aceitar requisições de origens específicas. Em desenvolvimento, normalmente aceita `http://localhost:4200` (porta padrão do Angular).

Para produção, certifique-se de que o domínio do frontend está na lista de origens permitidas.

---

## Exemplos de Integração com Angular

### Serviço de Autenticação (auth.service.ts)

```typescript
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, { email, password });
  }

  checkAuthStatus(): Observable<any> {
    return this.http.get(`${this.apiUrl}`);
  }

  signupVolunteer(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/signup/volunteer`, data);
  }

  signupOrganization(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/signup/organization`, data);
  }

  getCsrfToken(): Observable<any> {
    return this.http.get(`${this.apiUrl}/csrf`);
  }
}
```

### Serviço de Atividades (activity.service.ts)

```typescript
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ActivityService {
  private apiUrl = 'http://localhost:8080/api/activity';

  constructor(private http: HttpClient) {}

  getActivities(page: number = 0, size: number = 20): Observable<any> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get(this.apiUrl, { params });
  }

  getActivityById(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/${id}`);
  }

  createActivity(activity: any): Observable<any> {
    return this.http.post(this.apiUrl, activity);
  }

  updateActivity(id: number, activity: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, activity);
  }

  cancelActivity(id: number): Observable<any> {
    return this.http.patch(`${this.apiUrl}/${id}/cancel`, {});
  }

  deleteActivity(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}
```

### Interceptor HTTP para CSRF (csrf.interceptor.ts)

```typescript
import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpInterceptor,
  HttpHandler,
  HttpRequest,
  HttpXsrfTokenExtractor
} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class CsrfInterceptor implements HttpInterceptor {
  constructor(private tokenExtractor: HttpXsrfTokenExtractor) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Adiciona o token CSRF para requisições que modificam dados
    if (['POST', 'PUT', 'PATCH', 'DELETE'].includes(req.method)) {
      const token = this.tokenExtractor.getToken();
      if (token) {
        req = req.clone({
          setHeaders: {
            'X-CSRF-TOKEN': token
          }
        });
      }
    }
    return next.handle(req);
  }
}
```

### Configuração do HttpClient (app.config.ts)

```typescript
import { ApplicationConfig } from '@angular/core';
import { provideHttpClient, withInterceptors, withXsrfConfiguration } from '@angular/common/http';
import { CsrfInterceptor } from './interceptors/csrf.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(
      withXsrfConfiguration({
        cookieName: 'XSRF-TOKEN',
        headerName: 'X-CSRF-TOKEN'
      }),
      withInterceptors([CsrfInterceptor])
    )
  ]
};
```

---

## Modelos TypeScript para Angular

### auth.models.ts
```typescript
export interface LoginRequest {
  email: string;
  password: string;
}

export interface AuthResponse {
  success: boolean;
  message: string;
}

export interface AuthStatus {
  status: boolean;
  message: string;
  userType: 'VOLUNTEER' | 'ORGANIZATION' | null;
}

export interface VolunteerSignupRequest {
  name: string;
  email: string;
  password: string;
  phoneNumber: string;
  cep: string;
  number: string;
  cpf: string;
}

export interface OrganizationSignupRequest {
  name: string;
  email: string;
  password: string;
  phoneNumber: string;
  cep: string;
  number: string;
  cnpj: string;
  companyName: string;
  cause?: string;
}
```

### activity.models.ts
```typescript
export interface Activity {
  id: number;
  name: string;
  description: string;
  spots: number;
  cep: string;
  number: string;
  activityDate: string;
  organizationId: number;
  organizationName: string;
  organizationCompanyName: string;
  canceled: boolean;
}

export interface ActivityListItem {
  id: number;
  name: string;
  description: string;
  spots: number;
  cep: string;
  activityDate: string;
  organizationName: string;
  canceled: boolean;
}

export interface ActivityRequest {
  name: string;
  description?: string;
  spots: number;
  cep: string;
  number?: string;
  activityDate: string;
}

export interface PageResponse<T> {
  content: T[];
  pageable: {
    pageNumber: number;
    pageSize: number;
    offset: number;
  };
  totalPages: number;
  totalElements: number;
  last: boolean;
  first: boolean;
  size: number;
  number: number;
  numberOfElements: number;
  empty: boolean;
}
```

### user.models.ts
```typescript
export interface UserProfile {
  name: string;
  email: string;
  phoneNumber: string;
  cep: string;
  number: string;
  userType: 'VOLUNTEER' | 'ORGANIZATION';
  cpf?: string;
  cnpj?: string;
  companyName?: string;
  cause?: string;
}

export interface UpdateProfileRequest {
  name: string;
  email: string;
  phoneNumber: string;
  cep: string;
  number: string;
}

export interface UpdatePasswordRequest {
  currentPassword: string;
  newPassword: string;
}
```

### registration.models.ts
```typescript
export interface Registration {
  id: number;
  activityId: number;
  activityName: string;
  volunteerName: string;
  registeredAt: string;
  canceled: boolean;
}
```

---

## Notas Importantes

### Formato de Data
- Todas as datas são no formato ISO 8601: `YYYY-MM-DDTHH:mm:ss`
- Timestamps de auditoria incluem timezone: `YYYY-MM-DDTHH:mm:ssZ`

### Paginação
- A paginação segue o padrão Spring Data
- Páginas começam em 0
- Use os parâmetros `page`, `size` e `sort` para controlar a paginação

### Validação
- Todos os campos obrigatórios devem ser preenchidos
- Validações de formato são verificadas no backend
- Mensagens de erro são retornadas em português

### Segurança
- Sempre inclua o token CSRF em requisições que modificam dados
- Cookies de sessão são gerenciados automaticamente pelo navegador
- Use HTTPS em produção
- Configurar `withCredentials: true` no HttpClient para enviar cookies

### Boas Práticas
- Sempre verificar o status de autenticação antes de acessar rotas protegidas
- Implementar guards no Angular para rotas que requerem autenticação
- Tratar erros de forma consistente
- Implementar loading states durante requisições
- Cache de dados quando apropriado

---

## Suporte e Contato

Para dúvidas ou problemas com a API, entre em contato com a equipe de desenvolvimento do Voluntree.

**Repositório:** https://github.com/MMC-Organization/voluntree-spring

---

**Versão da Documentação:** 1.0.0  
**Última Atualização:** 29 de Janeiro de 2026
