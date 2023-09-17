FROM node:18-alpine AS build
WORKDIR /app
COPY package.json .
RUN npm install
COPY . .
RUN npx tailwindcss -i ./src/input.css -o ./dist/output.css
RUN npm run build

FROM nginx
COPY --from=build /app/dist /usr/share/nginx/html