# 🔧 Spring Boot Common Platform Microservice

This repository contains modular Spring Boot API projects designed for learning, demonstrations, and backend service architecture.

## 📁 Projects

- [Authentication Service](./authentication-service) – Stateless service responsible for processing login requests (via social providers) and delegating credential verification to external services.

- [Communication Service](./communication-service) – Manages real-time chat, private messaging, and channel subscriptions. Built with WebSocket or Redis pub/sub for scalable delivery.
  
- [Client Gateway](./client-gateway) – Acts as a secure gateway for client requests, responsible for routing, authenticating JWT tokens, and forwarding validated requests to internal APIs.

- [Eureka Server](./eureka-server) – the central service registry module responsible for registering and managing all microservices within the distributed system.

Each module is independently runnable and documented in its own README.md file.

---

### 🛠 Contribute

If you’d like to contribute or share your code, **do not commit directly to 'main'**

Please create a new branch using the allowed proper prefixes: **feature/** , **bugfix/** , **hotfix/** , **dev/** , **release/**

#### ⚠️ If you don’t use a proper prefix, your branch might not be protected and could be modified by others

#### 💡 Before committing:
Make sure to run **spotlessApply** to auto-format your code using **palantirJavaFormat()** and avoid unnecessary formatting conflicts

You can run it with:
```bash
./gradlew spotlessApply
```

Then open a Pull Request (PR) to merge into 'main'

#### ➡️ All changes will be reviewed before merging, use meaningful branch names and commit messages

---

### ⚖️ License:
This repository is licensed under the **MIT** License

---

### 🧑‍💻 Author:
[William Phan](https://github.com/wliamp)

---

### 📫 Contact:
`phnam230197@gmail.com`

