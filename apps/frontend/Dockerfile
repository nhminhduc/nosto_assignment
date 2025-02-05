# Stage 1: Build the Next.js app
FROM node:18 AS build
WORKDIR /app
COPY ../package.json ../package-lock.json ./
RUN npm install --legacy-peer-deps
COPY . .
RUN npm run build:frontend

# Stage 2: Serve the production build with a Node.js server
FROM node:18-alpine
WORKDIR /app
COPY --from=build app/dist/apps/frontend/.next ./.next
COPY --from=build app/dist/apps/frontend/public ./public
COPY --from=build app/dist/apps/frontend/package.json ./package.json
EXPOSE 3000
CMD ["npm", "start"]
