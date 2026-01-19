#!/bin/bash

# Script de Teste da API Voluntree
# Execute após iniciar a aplicação: ./mvnw spring-boot:run

BASE_URL="http://localhost:8080/api"
COOKIE_FILE="/tmp/voluntree-cookies.txt"

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  Teste da API Voluntree${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# Função para fazer requisições
function make_request() {
    local method=$1
    local endpoint=$2
    local data=$3
    local description=$4
    
    echo -e "${YELLOW}➜ ${description}${NC}"
    
    if [ -z "$data" ]; then
        response=$(curl -s -X $method \
            -b "$COOKIE_FILE" -c "$COOKIE_FILE" \
            -H "Content-Type: application/json" \
            -H "X-XSRF-TOKEN: $CSRF_TOKEN" \
            "$BASE_URL$endpoint")
    else
        response=$(curl -s -X $method \
            -b "$COOKIE_FILE" -c "$COOKIE_FILE" \
            -H "Content-Type: application/json" \
            -H "X-XSRF-TOKEN: $CSRF_TOKEN" \
            -d "$data" \
            "$BASE_URL$endpoint")
    fi
    
    echo "$response" | jq '.' 2>/dev/null || echo "$response"
    echo ""
    echo "$response"
}

# Limpar cookies anteriores
rm -f "$COOKIE_FILE"

echo -e "${GREEN}=== 1. Obter Token CSRF ===${NC}"
CSRF_RESPONSE=$(curl -s -c "$COOKIE_FILE" "$BASE_URL/auth/csrf")
CSRF_TOKEN=$(echo $CSRF_RESPONSE | jq -r '.token' 2>/dev/null)
echo "CSRF Token: $CSRF_TOKEN"
echo ""

echo -e "${GREEN}=== 2. Criar Voluntário ===${NC}"
VOLUNTEER_DATA='{
  "name": "João Silva",
  "email": "joao@voluntree.com",
  "password": "Senha@123",
  "phoneNumber": "11987654321",
  "cpf": "11144477735",
  "cep": "01310100",
  "number": "1000"
}'
make_request "POST" "/auth/signup/volunteer" "$VOLUNTEER_DATA" "Criando voluntário..."

echo -e "${GREEN}=== 3. Criar Organização ===${NC}"
ORG_DATA='{
  "name": "ONG Ajuda Mútua",
  "email": "contato@ajudamutua.org",
  "password": "Senha@123",
  "phoneNumber": "11912345678",
  "cnpj": "11222333000181",
  "companyName": "Associação Ajuda Mútua",
  "cause": "Combate à fome e assistência social",
  "cep": "01310100",
  "number": "500"
}'
make_request "POST" "/auth/signup/organization" "$ORG_DATA" "Criando organização..."

echo -e "${GREEN}=== 4. Login como Organização ===${NC}"
LOGIN_DATA='{
  "email": "contato@ajudamutua.org",
  "password": "Senha@123"
}'
LOGIN_RESPONSE=$(make_request "POST" "/auth/login" "$LOGIN_DATA" "Fazendo login como organização...")
ORG_ID=$(echo $LOGIN_RESPONSE | jq -r '.userId' 2>/dev/null)
echo "Organization ID: $ORG_ID"

echo -e "${GREEN}=== 5. Criar Atividade ===${NC}"
ACTIVITY_DATA='{
  "name": "Distribuição de Alimentos",
  "description": "Distribuição de cestas básicas para famílias carentes",
  "spots": 20,
  "cep": "01310100",
  "number": "500",
  "activityDate": "2026-02-15T14:00:00"
}'
ACTIVITY_RESPONSE=$(make_request "POST" "/activity" "$ACTIVITY_DATA" "Criando atividade...")
ACTIVITY_ID=$(echo $ACTIVITY_RESPONSE | jq -r '.id' 2>/dev/null)
echo "Activity ID: $ACTIVITY_ID"

echo -e "${GREEN}=== 6. Criar Outra Atividade ===${NC}"
ACTIVITY2_DATA='{
  "name": "Plantio de Árvores",
  "description": "Mutirão de plantio de árvores no parque municipal",
  "spots": 15,
  "cep": "04567890",
  "number": "200",
  "activityDate": "2026-03-20T09:00:00"
}'
make_request "POST" "/activity" "$ACTIVITY2_DATA" "Criando segunda atividade..."

echo -e "${GREEN}=== 7. Listar Minhas Atividades (Organização) ===${NC}"
make_request "GET" "/activity/my-activities" "" "Listando minhas atividades..."

echo -e "${GREEN}=== 8. Logout da Organização ===${NC}"
make_request "POST" "/auth/logout" "" "Fazendo logout..."

echo -e "${GREEN}=== 8.1. Renovar Token CSRF ===${NC}"
CSRF_RESPONSE=$(curl -s -b "$COOKIE_FILE" -c "$COOKIE_FILE" "$BASE_URL/auth/csrf")
CSRF_TOKEN=$(echo $CSRF_RESPONSE | jq -r '.token' 2>/dev/null)
echo "Novo CSRF Token: $CSRF_TOKEN"
echo ""

echo -e "${GREEN}=== 9. Login como Voluntário ===${NC}"
VOLUNTEER_LOGIN='{
  "email": "joao@voluntree.com",
  "password": "Senha@123"
}'
make_request "POST" "/auth/login" "$VOLUNTEER_LOGIN" "Fazendo login como voluntário..."

echo -e "${GREEN}=== 10. Listar Todas as Atividades (Voluntário) ===${NC}"
make_request "GET" "/activity" "" "Listando todas as atividades..."

echo -e "${GREEN}=== 11. Listar Atividades Futuras ===${NC}"
make_request "GET" "/activity/upcoming" "" "Listando atividades futuras..."

echo -e "${GREEN}=== 12. Ver Detalhes de Uma Atividade ===${NC}"
if [ ! -z "$ACTIVITY_ID" ] && [ "$ACTIVITY_ID" != "null" ]; then
    make_request "GET" "/activity/$ACTIVITY_ID" "" "Visualizando atividade $ACTIVITY_ID..."
fi

echo -e "${GREEN}=== 13. Logout Final ===${NC}"
make_request "POST" "/auth/logout" "" "Fazendo logout final..."

# Limpar cookies
rm -f "$COOKIE_FILE"

echo ""
echo -e "${BLUE}========================================${NC}"
echo -e "${GREEN}✓ Testes Concluídos!${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""
echo -e "Dados criados:"
echo -e "  • Voluntário: joao@voluntree.com (senha: Senha@123)"
echo -e "  • Organização: contato@ajudamutua.org (senha: Senha@123)"
echo -e "  • 2 Atividades criadas"
echo ""
