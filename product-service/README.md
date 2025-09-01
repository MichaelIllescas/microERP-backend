# 📦 Product Service

Microservicio responsable de la **gestión de productos** dentro de la plataforma **MicroERP**.  
Implementa arquitectura **hexagonal (Ports & Adapters)** con **Spring Boot**.

---

## 🎯 Objetivo
Permitir la administración de productos del negocio, incluyendo:
- Registro de productos con categoría.
- Consulta, actualización y eliminación.
- Exposición de métricas (Actuator + Prometheus).
- Integración con el **Stock Service** mediante eventos (en futuras fases).

---

## 🏗️ Arquitectura

El servicio sigue **arquitectura hexagonal**, separando la lógica de negocio de la infraestructura:

### 1. **Domain**
- `model/` → Entidades de negocio (`Product`, `Category`).
- Sin dependencias externas (ni JPA, ni frameworks).

### 2. **Application**
- `dto/` → Objetos de transferencia (`CreateProductRequest`, `ProductResponse`, `CategoryResponse`).
- `port/`
  - `in/` → Interfaces de casos de uso (`CreateProductUseCase`, `GetProductUseCase`, etc.).
  - `out/` → Interfaces hacia infraestructura (`ProductRepositoryPort`, `CategoryRepositoryPort`).
- `usecase/` → Casos de uso del negocio (`CreateProductService`, `GetProductService`, etc.).

### 3. **Infrastructure**
- `adapter/`
  - `in/` → Controladores REST (`ProductController`).
  - `out/` → Adaptadores de persistencia (repositorios JPA).
- `persistence/`
  - `entity/` → Entidades JPA (`ProductEntity`, `CategoryEntity`).
  - `repository/` → Interfaces JPA (`ProductJpaRepository`, `CategoryJpaRepository`).
- `mapper/` → Conversión entre entidades de dominio, DTOs y JPA.
- `config/` → Configuración de Spring, Swagger, etc.

---

## 🔄 Flujo típico

1. Una petición llega al **ProductController**.
2. El controlador invoca un **caso de uso** (ej: `CreateProductService`).
3. El caso de uso valida la categoría y mapea el request a un objeto de dominio (`Product`).
4. Se utiliza el puerto de salida `ProductRepositoryPort` para persistir.
5. El adaptador de persistencia (`ProductRepositoryAdapter`) convierte el modelo de dominio a entidad JPA y lo guarda.
6. El resultado vuelve al caso de uso → mapper → DTO de respuesta → API REST.

---

## 🚀 Endpoints principales

Todos los endpoints expuestos están documentados con **Swagger/OpenAPI** en:  
`http://localhost:{puerto}/swagger-ui.html`

| Método | Endpoint          | Descripción                 |
|--------|-------------------|-----------------------------|
| POST   | `/products`       | Crear producto              |
| GET    | `/products`       | Listar todos los productos  |
| GET    | `/products/{id}`  | Obtener producto por ID     |
| PUT    | `/products/{id}`  | Actualizar producto         |
| DELETE | `/products/{id}`  | Eliminar producto           |
| POST   | `/categories`     | Crear categoría             |
| GET    | `/categories`     | Listar todos las categorías |
| GET    | `/categories/{id}`| Obtener categoría por ID    |
| PUT    | `/categories/{id}`| Actualizar categoría        |
| DELETE | `/categories/{id}`| Eliminar categoría          |

---

## 📊 Observabilidad

- **Spring Boot Actuator** habilitado → `/actuator/*`.
- Métricas disponibles para **Prometheus** en `/actuator/prometheus`.

---

## 🧪 Testing

- **Unit Tests** con **JUnit 5** y **Mockito**.
  - Casos de uso probados con puertos mockeados.
- **Integration Tests** con **Spring Boot Test** (opcional).
- Ejemplo:
  - `CreateProductServiceTest` valida creación correcta y error cuando la categoría no existe.

---

## ⚙️ Tecnologías

- **Backend:** Java 21, Spring Boot 3.3.x
- **Persistencia:** Spring Data JPA + MySQL
- **Seguridad:** Keycloak (JWT via API Gateway)
- **Testing:** JUnit 5, Mockito
- **Observabilidad:** Actuator, Prometheus
- **Documentación:** Swagger / OpenAPI

---

## 📂 Estructura del proyecto

```
product-service
 ├── application
 │   ├── dto
 │   ├── port
 │   │   ├── in
 │   │   └── out
 │   └── usecase
 ├── domain
 │   └── model
 └── infrastructure
     ├── adapter
     │   ├── in
     │   └── out
     ├── config
     ├── mapper
     └── persistence
         ├── entity
         ├── repository
         └── adapter
```

---

## ✅ Beneficios de este diseño

- Desacoplamiento entre dominio y frameworks.
- Facilidad de testear con mocks.
- Flexibilidad para cambiar infraestructura (ej: reemplazar JPA por MongoDB).
- Documentación automática de APIs.
- Observabilidad lista para producción.

---

