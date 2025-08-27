# 📘 MicroERP Backend (En Desarrollo 🚧)

**Estado:** 🚧 *En desarrollo activo*  
Este repositorio contiene la implementación del **backend de MicroERP**, un sistema modular de gestión empresarial basado en **microservicios con Spring Boot y Spring Cloud**, orientado a PyMEs que buscan digitalizar sus procesos.  

---

## 🎯 Objetivo del Proyecto
- Construir un **ERP escalable y desacoplado** bajo arquitectura de microservicios.  
- Aplicar **buenas prácticas de ingeniería de software**:  
  - Principios **SOLID**  
  - **Clean Architecture**  
  - **Domain-Driven Design (DDD)**  
  - **Test-Driven Development (TDD)**  
- Asegurar **seguridad, resiliencia y observabilidad** desde la fase inicial.  

---

## 🏗️ Arquitectura General

### 🔑 Infraestructura
- **Config Server** → Configuración centralizada.  
- **Eureka Server** → Registro y descubrimiento de servicios.  
- **API Gateway** → Entrada única al sistema (Spring Cloud Gateway).  
- **Keycloak** → Autenticación y autorización (OAuth2 + JWT).  
- **Message Broker** → RabbitMQ/Kafka para mensajería asíncrona.  

### ⚙️ Microservicios de Negocio
- **User Service** → Gestión de usuarios (datos de perfil, dirección, etc.).  
- **Customer Service** → Gestión de clientes.  
- **Product Service** → CRUD de productos, métricas con Actuator.  
- **Sales Service** → Registro de ventas y facturación.  
- **Stock Service** → Actualización automática de inventario vía eventos.  
- **Notification Service** → Envío de correos/notificaciones.  

### 🔍 Observabilidad
- **Prometheus** → Recolección de métricas.  
- **Grafana** → Dashboards de visualización.  
- **Jaeger/Zipkin** → Trazas distribuidas.  
- **ELK Stack (opcional)** → Centralización de logs.  

---

## 📂 Estructura de Carpetas

```
microerp-backend/
│
├── config-server/         # Microservicio Spring Boot
├── eureka-server/         # Microservicio Spring Boot
├── api-gateway/           # Microservicio Spring Boot
├── user-service/          # Primer microservicio de negocio
├── customer-service/      # Futuro microservicio (clientes)
├── product-service/       # Futuro microservicio (productos)
├── sales-service/         # Futuro microservicio (ventas)
├── stock-service/         # Futuro microservicio (stock)
├── notification-service/  # Futuro microservicio (notificaciones)
├── config-repo/           # Repositorio externo de configuraciones (.properties)
├── desing/                # Documentación, diagramas, especificaciones
├── .gitignore             # Ignorar compilados, IDE, logs y config-repo
└── README.md              # Documentación del proyecto
```

---

## 🛠️ Tecnologías y Herramientas

- **Backend**:  
  - Java 21  
  - Spring Boot 3.x  
  - Spring Cloud (Config, Eureka, Gateway)  
  - Spring Data JPA / Hibernate  
- **Seguridad**:  
  - Keycloak (OAuth2, JWT)  
  - Spring Security  
- **Mensajería**:  
  - RabbitMQ / Kafka  
- **Persistencia**:  
  - MySQL / PostgreSQL (una DB por microservicio)  
- **Observabilidad**:  
  - Spring Actuator  
  - Prometheus + Grafana  
  - Jaeger / Zipkin  
- **Testing**:  
  - JUnit 5  
  - Mockito  
  - Testcontainers  
- **DevOps**:  
  - Docker + Docker Compose  
  - GitHub Actions (CI/CD)  
  - Kubernetes (opcional)  

---

## 🚦 Estado de Avance (Fases)

1️⃣ **Infraestructura mínima**  
   - Config Server ✅  
   - Eureka Server ✅  
   - API Gateway ✅  

2️⃣ **Seguridad con Keycloak** (en curso)  
3️⃣ **Primer microservicio (User Service)**  
4️⃣ **Microservicios de negocio (Customer, Product, Sales, Stock, Notification)**  
5️⃣ **Mensajería asíncrona con RabbitMQ/Kafka**  
6️⃣ **Observabilidad con Prometheus/Grafana**  
7️⃣ **Resiliencia (Resilience4j)**  
8️⃣ **Testing (unit, integration, contract)**  
9️⃣ **CI/CD con Docker y GitHub Actions**  

---

## 📑 Buenas Prácticas Aplicadas

- **Arquitectura en capas** → separación clara de controladores, servicios, repositorios.  
- **DDD (Domain-Driven Design)** → entidades y casos de uso bien definidos.  
- **SOLID** → clases y servicios pequeños, desacoplados y fáciles de mantener.  
- **TDD (Test-Driven Development)** → desarrollo acompañado de pruebas unitarias e integrales.  
- **12-Factor App** → configuraciones externas, dependencia en servicios, portabilidad.  
- **Resiliencia** → circuit breakers, reintentos y tolerancia a fallos.  
- **CI/CD** → pipelines de integración continua y despliegue automatizado.  

---

## 🚀 Cómo empezar (modo desarrollo local)

### 1. Clonar el repositorio
```bash
git clone git@github.com:TU-USUARIO/microerp-backend.git
cd microerp-backend
```

### 2. Levantar infraestructura base
```bash
cd config-server
mvn spring-boot:run
```

En otra terminal:
```bash
cd eureka-server
mvn spring-boot:run
```

En otra terminal:
```bash
cd api-gateway
mvn spring-boot:run
```

### 3. Verificar
- Config Server → [http://localhost:8888](http://localhost:8888)  
- Eureka Server → [http://localhost:8761](http://localhost:8761)  
- API Gateway → [http://localhost:8080](http://localhost:8080)  

---

## 👨‍💻 Autor

**Michael Jonathan Illescas**  
Backend Developer | Arquitecturas Limpias | Buenas Prácticas  

- GitHub: [@MichaelIllescas](https://github.com/MichaelIllescas)  
- LinkedIn: [Perfil Profesional](https://www.linkedin.com/in/michael-jonathan-illescas/)  
