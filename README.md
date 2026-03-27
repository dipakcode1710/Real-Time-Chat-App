# Real-Time Chat Application (Spring Boot + WebSocket)

Production-oriented backend starter inspired by **WhatsApp + Instagram + Snapchat** with:
- JWT auth with refresh tokens
- STOMP over WebSocket for real-time messaging
- MySQL persistence (JPA/Hibernate)
- Redis presence tracking
- Stories, follow system, notifications, media metadata
- Swagger OpenAPI docs

## Tech Stack
- Spring Boot 3
- Spring Security + JWT
- Spring Data JPA
- Spring WebSocket (STOMP)
- MySQL + Redis
- Lombok

## Project Structure
- `controller` REST APIs
- `service` business logic
- `repository` data access
- `dto` request/response models
- `websocket` STOMP handlers
- `exception` global error handler


## Run in Eclipse
1. Use **JDK 17+** for the project (this project is configured for Java 17).
2. Import as Maven project: **File -> Import -> Maven -> Existing Maven Projects**.
3. Right-click project -> **Maven -> Update Project** (force update).
4. Run the main class **`com.example.chatapp.ChatApplication`** (note the exact package spelling: `example`, not `exmple`).
5. If Eclipse still shows `ClassNotFoundException`, delete the old run configuration and create a new one:
   - **Run Configurations -> Spring Boot App -> New**
   - Project: `realtime-chat-app`
   - Main type: `com.example.chatapp.ChatApplication`

## Run Locally
```bash
docker compose up -d mysql redis
mvn spring-boot:run
```
Or build image:
```bash
mvn -DskipTests package
docker compose up --build
```

## WebSocket Endpoints
- Handshake: `/ws`
- App destinations:
  - `/app/chat.send`
  - `/app/chat.typing`
  - `/app/chat.presence.online`
  - `/app/chat.presence.offline`
- Subscriptions:
  - `/topic/messages/{chatId}`
  - `/topic/typing/{chatId}`
  - `/topic/presence`
  - `/user/queue/private`

## API Docs
- Swagger UI: `http://localhost:8080/swagger-ui.html`

## Sample REST Endpoints
- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/auth/refresh`
- `GET /api/users/{id}`
- `GET /api/users/search?q=alice`
- `GET /api/chats/{chatId}/messages`
- `GET /api/chats/messages/search?q=hello`
- `POST /api/stories`
- `GET /api/stories/active`
- `POST /api/follows`
- `DELETE /api/follows`

## Notes for Production
- Move JWT secret to environment/secret manager.
- Replace local media with S3-compatible object storage.
- Add Flyway/Liquibase migrations and Kafka for scale.
- Add distributed WebSocket broker (RabbitMQ/Redis STOMP relay).
- Integrate Firebase push notifications.
- Add message retention jobs for expiring/view-once content.
