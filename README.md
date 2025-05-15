# 🎉 Eventopia – Java Event Planning Platform

![Java](https://img.shields.io/badge/Java-17+-blue.svg)
![License](https://img.shields.io/github/license/your-username/eventopia-java)
![Build](https://img.shields.io/badge/build-passing-brightgreen)
![Status](https://img.shields.io/badge/status-in%20development-orange)

**Eventopia** is a scalable and extensible event planning platform built in Java. Designed for managing conferences, meetups, corporate events, and more, it offers robust features for handling users, venues, schedules, and notifications—all built on modern Java frameworks.

---

## 📌 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Screenshots](#-screenshots)
- [Getting Started](#-getting-started)
- [Configuration](#-configuration)
- [Roadmap](#-roadmap)
- [Contributing](#-contributing)
- [License](#-license)
- [Contact](#-contact)

---

## ✨ Features

- ✅ Role-based access for Admins, Organizers, and Clients
- 📅 Event scheduling and calendar integration
- 🏛 Venue and resource booking
- 🔔 Email and push notifications
- 📊 Admin dashboards and reporting tools
- 🔐 JWT-based authentication and secure session handling
- ⚙️ Modular, microservice-ready architecture

---

## 🧰 Tech Stack

| Layer        | Technology             |
|--------------|------------------------|
| Language     | Java 17+               |
| Framework    | Java / JavaFX          |
| Database     | MySQL / PostgreSQL     |
| ORM          | Hibernate / JPA        |
| Frontend     | JavaFX (UI Toolkit)    |
| Build Tool   | Maven / Gradle         |
| Security     | Java Security / Custom |
| Deployment   | Native Packaging, GitHub Actions |

---

## 🖼 Screenshots

> *(Add real screenshots or UI previews of your application)*

![Dashboard](docs/screenshots/admin-dashboard.png)
![Event Form](docs/screenshots/event-form.png)

---

## ⚙️ Getting Started

To run the project locally:

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/eventopia-java.git
cd eventopia-java
```

### 2. Configure the Environment

Copy the example environment file and adjust it:

```bash
cp .env.example .env
```

Update `application.properties` or `application.yml` as needed:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/eventopia
spring.datasource.username=your_user
spring.datasource.password=your_password
jwt.secret=your_jwt_secret
```

### 3. Build and Run the Application

If you're using **Maven**:

```bash
./mvnw spring-boot:run
```

Or with **Gradle**:

```bash
./gradlew bootRun
```

> The app should be running at `http://localhost:8080`

---

## 🛠 Configuration

- **Mailer**: Configure SMTP in `application.properties`:
  ```properties
  spring.mail.host=smtp.mailtrap.io
  spring.mail.port=2525
  spring.mail.username=your_username
  spring.mail.password=your_password
  ```

- **Database**: Make sure your DB service is running and credentials match your config.

---

## 🚧 Roadmap

- [ ] Frontend SPA with React or Vue.js
- [ ] REST API for mobile clients
- [ ] OAuth2 login (Google, GitHub)
- [ ] Integration with Google Calendar
- [ ] Admin panel with analytics

---

## 🤝 Contributing

1. Fork this repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some feature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

Distributed under the **MIT License**. See [`LICENSE`](LICENSE) for more information.

---

## 📬 Contact

**Project Maintainer**  
👤 CodeCatalyst

---

> Built with ❤️ in Java – Helping you organize events efficiently, securely, and at scale.
