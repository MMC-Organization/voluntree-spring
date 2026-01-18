# Implementação do CRUD de Atividades - Voluntree

## 📋 Visão Geral

Este documento descreve a implementação completa do **CRUD de Atividades** no sistema Voluntree, incluindo todos os DTOs, serviços, controladores e funcionalidades relacionadas.

## 🗂️ Arquivos Criados/Modificados

### DTOs (Data Transfer Objects)

#### 1. `ActivityRequest.java`
**Localização:** `src/main/java/com/voluntree/backend/dto/activity/`

DTO para criação de novas atividades. Contém validações completas:

**Campos:**
- `name` (String, obrigatório) - Nome da atividade (máx. 200 caracteres)
- `description` (String, opcional) - Descrição detalhada (máx. 5000 caracteres)
- `spots` (Short, opcional) - Número de vagas disponíveis (deve ser positivo)
- `cep` (String, obrigatório) - CEP do local (8 dígitos numéricos)
- `number` (String, opcional) - Número do endereço (máx. 10 caracteres)
- `activityDate` (LocalDateTime, obrigatório) - Data e hora da atividade (deve ser futura)

**Validações:**
- `@NotBlank` para campos obrigatórios
- `@Size` para limitar tamanho de strings
- `@Pattern` para validar formato do CEP
- `@Future` para garantir que a data é futura
- `@Positive` para número de vagas

#### 2. `ActivityUpdateRequest.java`
**Localização:** `src/main/java/com/voluntree/backend/dto/activity/`

DTO para atualização de atividades existentes. Todos os campos são opcionais, permitindo atualizações parciais.

**Campos:** Mesmos de `ActivityRequest`, porém todos opcionais.

#### 3. `ActivityResponse.java`
**Localização:** `src/main/java/com/voluntree/backend/dto/activity/`

DTO de resposta com informações completas da atividade.

**Campos:**
- `id` (Long) - ID da atividade
- `name` (String) - Nome da atividade
- `description` (String) - Descrição
- `spots` (Short) - Vagas disponíveis
- `cep` (String) - CEP
- `number` (String) - Número do endereço
- `activityDate` (LocalDateTime) - Data/hora da atividade
- `organizationId` (Long) - ID da organização criadora
- `organizationName` (String) - Nome da organização
- `organizationCompanyName` (String) - Razão social da organização
- `canceled` (Boolean) - Status de cancelamento

#### 4. `ActivityListResponse.java`
**Localização:** `src/main/java/com/voluntree/backend/dto/activity/`

DTO simplificado para listagens, contendo apenas informações essenciais.

**Campos:**
- `id` (Long)
- `name` (String)
- `description` (String)
- `spots` (Short)
- `cep` (String)
- `activityDate` (LocalDateTime)
- `organizationName` (String)
- `canceled` (Boolean)

---

### Entidade

#### `Activity.java` (Modificado)
**Localização:** `src/main/java/com/voluntree/backend/domain/`

**Modificações realizadas:**
- Adicionadas anotações Lombok: `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`
- Facilita acesso aos campos e criação de instâncias

---

### Exceções

#### 1. `ActivityNotFoundException.java`
**Localização:** `src/main/java/com/voluntree/backend/exception/`

Exceção lançada quando uma atividade não é encontrada pelo ID.

#### 2. `UnauthorizedActivityAccessException.java`
**Localização:** `src/main/java/com/voluntree/backend/exception/`

Exceção lançada quando um usuário tenta acessar/modificar uma atividade sem permissão.

#### 3. `InvalidUserTypeException.java`
**Localização:** `src/main/java/com/voluntree/backend/exception/`

Exceção lançada quando o tipo de usuário é inválido para a operação.

---

### Repositório

#### `ActivityRepository.java` (Modificado)
**Localização:** `src/main/java/com/voluntree/backend/repository/`

**Métodos adicionados:**

1. **`findByCanceledFalse(Pageable)`**
   - Lista todas as atividades não canceladas com paginação

2. **`findByOrganizationId(Long, Pageable)`**
   - Lista todas as atividades de uma organização específica

3. **`findUpcomingActivities(LocalDateTime, Pageable)`**
   - Lista atividades futuras não canceladas (query JPQL customizada)

4. **`findUpcomingActivitiesByOrganization(Long, LocalDateTime, Pageable)`**
   - Lista atividades futuras de uma organização específica

---

### Serviço

#### `ActivityService.java` (Implementado)
**Localização:** `src/main/java/com/voluntree/backend/service/`

**Dependências:**
- `ActivityRepository` - Acesso ao banco de dados
- `OrganizationRepository` - Validação de organizações
- `ApplicationEventPublisher` - Publicação de eventos de auditoria

**Métodos implementados:**

1. **`createActivity(ActivityRequest, Long)`**
   - Cria nova atividade
   - Valida existência da organização
   - Publica evento de auditoria
   - Retorna `ActivityResponse`

2. **`getActivityById(Long)`**
   - Busca atividade por ID
   - Lança `ActivityNotFoundException` se não encontrada
   - Retorna `ActivityResponse`

3. **`getAllActivities(Pageable)`**
   - Lista todas as atividades não canceladas
   - Suporta paginação e ordenação
   - Retorna `Page<ActivityListResponse>`

4. **`getUpcomingActivities(Pageable)`**
   - Lista apenas atividades futuras não canceladas
   - Usa `LocalDateTime.now()` como referência
   - Retorna `Page<ActivityListResponse>`

5. **`getActivitiesByOrganization(Long, Pageable)`**
   - Lista atividades de uma organização específica
   - Retorna `Page<ActivityListResponse>`

6. **`updateActivity(Long, ActivityUpdateRequest, Long)`**
   - Atualiza atividade existente
   - Valida propriedade (apenas organização dona pode atualizar)
   - Valida se atividade não está cancelada
   - Suporta atualizações parciais (campos opcionais)
   - Publica evento de auditoria
   - Retorna `ActivityResponse`

7. **`cancelActivity(Long, Long)`**
   - Cancela atividade (soft delete)
   - Valida propriedade
   - Valida se já não está cancelada
   - Publica evento de auditoria

8. **`deleteActivity(Long, Long)`**
   - Deleta atividade permanentemente (hard delete)
   - Valida propriedade
   - Publica evento de auditoria

**Métodos auxiliares privados:**
- `mapToActivityResponse(Activity)` - Converte entidade para DTO completo
- `mapToActivityListResponse(Activity)` - Converte entidade para DTO simplificado

**Eventos de Auditoria:**
Todos os métodos de modificação (CREATE, UPDATE, DELETE) publicam eventos de auditoria com:
- Mensagem descritiva
- ID do usuário (organização)
- ID do recurso afetado
- Tipo de usuário (ORGANIZATION)
- Tipo de ação (CREATE, UPDATE, DELETE)
- Resultado (SUCCESS)
- Módulo (ACTIVITY)

---

### Controlador

#### `ActivityController.java` (Implementado)
**Localização:** `src/main/java/com/voluntree/backend/controller/`

**Base URL:** `/api/activity`

**Endpoints implementados:**

#### 1. **POST `/api/activity`**
- **Descrição:** Criar nova atividade
- **Autenticação:** Requerida (apenas organizações)
- **Body:** `ActivityRequest` (JSON)
- **Resposta:** `ActivityResponse` (201 Created)
- **Validações:** Campos obrigatórios, formato de dados

#### 2. **GET `/api/activity/{id}`**
- **Descrição:** Buscar atividade por ID
- **Autenticação:** Não requerida (público)
- **Parâmetros:** `id` (path)
- **Resposta:** `ActivityResponse` (200 OK)

#### 3. **GET `/api/activity`**
- **Descrição:** Listar todas as atividades não canceladas
- **Autenticação:** Não requerida (público)
- **Query Params:**
  - `page` (int, padrão: 0) - Número da página
  - `size` (int, padrão: 20) - Itens por página
  - `sort` (string, padrão: "activityDate") - Campo de ordenação
- **Resposta:** `Page<ActivityListResponse>` (200 OK)

#### 4. **GET `/api/activity/upcoming`**
- **Descrição:** Listar apenas atividades futuras não canceladas
- **Autenticação:** Não requerida (público)
- **Query Params:** Mesmos de listagem geral
- **Resposta:** `Page<ActivityListResponse>` (200 OK)

#### 5. **GET `/api/activity/organization/{organizationId}`**
- **Descrição:** Listar atividades de uma organização específica
- **Autenticação:** Não requerida (público)
- **Parâmetros:** `organizationId` (path)
- **Query Params:** Paginação
- **Resposta:** `Page<ActivityListResponse>` (200 OK)

#### 6. **GET `/api/activity/my-activities`**
- **Descrição:** Listar atividades da organização autenticada
- **Autenticação:** Requerida (apenas organizações)
- **Query Params:** Paginação
- **Resposta:** `Page<ActivityListResponse>` (200 OK)

#### 7. **PUT `/api/activity/{id}`**
- **Descrição:** Atualizar atividade
- **Autenticação:** Requerida (apenas organização dona)
- **Parâmetros:** `id` (path)
- **Body:** `ActivityUpdateRequest` (JSON)
- **Resposta:** `ActivityResponse` (200 OK)
- **Validações:** Propriedade, atividade não cancelada

#### 8. **PATCH `/api/activity/{id}/cancel`**
- **Descrição:** Cancelar atividade (soft delete)
- **Autenticação:** Requerida (apenas organização dona)
- **Parâmetros:** `id` (path)
- **Resposta:** 204 No Content
- **Validações:** Propriedade, não cancelada previamente

#### 9. **DELETE `/api/activity/{id}`**
- **Descrição:** Deletar atividade permanentemente
- **Autenticação:** Requerida (apenas organização dona)
- **Parâmetros:** `id` (path)
- **Resposta:** 204 No Content
- **Validações:** Propriedade

---

## 🔒 Segurança e Autorizações

### Endpoints Públicos (Sem Autenticação)
- `GET /api/activity` - Listar todas as atividades
- `GET /api/activity/{id}` - Ver detalhes de uma atividade
- `GET /api/activity/upcoming` - Listar atividades futuras
- `GET /api/activity/organization/{organizationId}` - Ver atividades de uma organização

### Endpoints Autenticados (Apenas Organizações)
- `POST /api/activity` - Criar atividade
- `GET /api/activity/my-activities` - Ver minhas atividades
- `PUT /api/activity/{id}` - Atualizar atividade (apenas dona)
- `PATCH /api/activity/{id}/cancel` - Cancelar atividade (apenas dona)
- `DELETE /api/activity/{id}` - Deletar atividade (apenas dona)

### Validações de Propriedade
Operações de modificação (UPDATE, DELETE, CANCEL) validam que:
1. O usuário autenticado é uma organização
2. A organização é a proprietária da atividade
3. Lança `UnauthorizedActivityAccessException` caso contrário

---

## 📊 Auditoria

Todas as operações de modificação são auditadas automaticamente através do `ApplicationEventPublisher`. Os logs incluem:

- **CREATE:** "Atividade criada: [nome]"
- **UPDATE:** "Atividade atualizada: [nome]"
- **DELETE/CANCEL:** "Atividade cancelada/deletada: [nome]"

Informações registradas:
- ID do usuário (organização)
- ID do recurso (atividade)
- Tipo de ação
- Resultado
- Módulo (ACTIVITY)
- Timestamp automático

---

## 🎯 Funcionalidades Principais

### 1. **Listagem Pública**
Qualquer usuário (autenticado ou não, voluntário ou organização) pode:
- Ver todas as atividades disponíveis
- Filtrar por atividades futuras
- Ver atividades de organizações específicas
- Pesquisar com paginação e ordenação

### 2. **Gestão por Organizações**
Organizações podem:
- Criar novas atividades
- Ver suas próprias atividades
- Atualizar informações das atividades
- Cancelar atividades (mantém registro no banco)
- Deletar atividades permanentemente

### 3. **Validações de Negócio**
- Data de atividade deve ser futura
- Vagas devem ser positivas (se informadas)
- CEP no formato correto (8 dígitos)
- Apenas organização dona pode modificar
- Atividades canceladas não podem ser atualizadas

### 4. **Paginação e Performance**
- Todas as listagens suportam paginação
- Queries otimizadas no repositório
- Fetch LAZY para relacionamentos
- DTOs específicos para listagem (menos dados)

---

## 🧪 Exemplos de Uso

### Criar Atividade
```json
POST /api/activity
{
  "name": "Doação de Alimentos",
  "description": "Campanha de arrecadação de alimentos não perecíveis",
  "spots": 10,
  "cep": "12345678",
  "number": "100",
  "activityDate": "2026-02-15T14:00:00"
}
```

### Atualizar Atividade
```json
PUT /api/activity/1
{
  "spots": 15,
  "description": "Descrição atualizada com mais detalhes"
}
```

### Listar Atividades com Paginação
```
GET /api/activity?page=0&size=10&sort=activityDate,asc
```

### Listar Apenas Atividades Futuras
```
GET /api/activity/upcoming?page=0&size=20
```

---

## 📝 Notas Técnicas

1. **Transações:** Todos os métodos de modificação usam `@Transactional`
2. **Read-Only:** Métodos de consulta usam `@Transactional(readOnly = true)` para otimização
3. **Validação Bean:** Usa Jakarta Validation (`@Valid`) nos endpoints
4. **Tratamento de Exceções:** Exceções customizadas para erros de negócio
5. **Paginação:** Spring Data Pageable com defaults configurados
6. **Soft Delete:** Cancelamento de atividades mantém registro no banco

---

## ✅ Status da Implementação

- ✅ DTOs criados e validados
- ✅ Entidade Activity atualizada com Lombok
- ✅ Repositório com queries customizadas
- ✅ Exceções customizadas
- ✅ Service completo com todas as regras de negócio
- ✅ Controller com todos os endpoints REST
- ✅ Integração com sistema de auditoria
- ✅ Suporte completo a paginação
- ✅ Validações de segurança e propriedade

---

## 🚀 Próximos Passos Sugeridos

1. Implementar tratamento global de exceções (GlobalExceptionHandler)
2. Adicionar testes unitários e de integração
3. Implementar busca por filtros (nome, data, localização)
4. Adicionar suporte a imagens de atividades
5. Implementar notificações quando atividades forem canceladas
6. Criar endpoint de estatísticas de atividades
