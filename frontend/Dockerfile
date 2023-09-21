FROM node:alpine AS build
WORKDIR /app
COPY ./package.json .
RUN npm i
COPY . .
RUN npx tailwindcss -i ./src/input.css -o ./dist/output.css
RUN npm run build

FROM nginx AS final
COPY ./react-app.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html