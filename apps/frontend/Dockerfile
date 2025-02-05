FROM node:18-alpine AS build
WORKDIR /app
COPY ../package.json ../yarn.lock ./
RUN yarn install --frozen-lockfile
COPY . .
RUN yarn build:frontend

# Stage 2: Serve the production build with a Node.js server
FROM node:18-alpine
WORKDIR /app
COPY --from=build /app/dist/apps/frontend/.next ./.next
COPY --from=build /app/dist/apps/frontend/public ./public
COPY --from=build /app/dist/apps/frontend/package.json ./package.json
RUN yarn install --production --frozen-lockfile
EXPOSE 3000
CMD ["yarn", "start"]
