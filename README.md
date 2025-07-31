# 🔧 Spring Boot API Portfolio

This repository contains modular Spring Boot API projects designed for learning, demonstrations, and backend service architecture.

## 📁 Projects

- [Database Connector API](./database-connector-api) – A dynamic API that connects to various SQL and NoSQL databases at runtime to execute queries securely and flexibly.

- [Token Issuer API](./token-issuer-api) – Stateless service responsible for issuing, verifying, and refreshing different types of JWT tokens (access, refresh, service).

- [Client Gateway API](./client-gateway-api) – Acts as a secure gateway for client requests, responsible for routing, authenticating JWT tokens, and forwarding validated requests to internal APIs.

- [Third Authentication Provider API](./authen-3rd-api) – Handles third-party authentication via OAuth (Google, Facebook) and OTP, forwarding verified identities to the JWT issuer service for token generation.

Each module is independently runnable and documented in its own README.md file.

---

### 🛠 Contribute

If you’d like to contribute or share your code, **do not commit directly to 'main'**

Please create a new branch using the allowed proper prefixes below:

feature/

bugfix/

hotfix/

dev/

release/
#### ⚠️ If you don’t use a proper prefix, your branch might not be protected and could be modified by others
Then open a Pull Request (PR) to merge into 'main'
#### ➡️ All changes will be reviewed before merging, use meaningful branch names and commit messages

---

### ⚖️ License:
This repository is licensed under the [MIT License](./LICENSE)

---

### 🧑‍💻 Author:
[William Phan](https://github.com/phnam2301)

---

### 📫 Contact:
`phnam230197@gmail.com`

