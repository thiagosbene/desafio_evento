# === Etapa 1: Build do Angular ===
FROM node:20-alpine AS build

WORKDIR /app
COPY package*.json ./
RUN npm ci

COPY . .
RUN npm run build -- --configuration=production

# === Etapa 2: Servir com Nginx ===
FROM nginx:alpine

# Copia a build de produção
COPY --from=build /app/dist/frontend /usr/share/nginx/html

# Copia configuração do Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]