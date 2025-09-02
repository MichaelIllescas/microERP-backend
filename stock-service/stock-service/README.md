# 📦 Stock Service

Microservicio responsable de la **gestión de inventario** dentro de la plataforma **MicroERP**.  
Implementa arquitectura **hexagonal (Ports & Adapters)** y se integra con otros servicios a través de **eventos en RabbitMQ**.

---

## 🎯 Responsabilidades

- Mantener el stock disponible de cada producto (`StockItem`).  
- Responder consultas sobre el inventario actual.  
- Permitir ajustes manuales de cantidades.  
- Sincronizar el stock en base a eventos emitidos por otros microservicios:
  - **`product.created`** → inicializa stock para un nuevo producto.
  - **`product.deleted`** → elimina el stock asociado al producto.
  - **`sale.confirmed`** (futuro) → descuenta el stock según la cantidad vendida.

---

## 🏗️ Arquitectura

El servicio sigue el patrón **Hexagonal / Ports & Adapters**:

- **Domain**:  
  Contiene el modelo de negocio (`StockItem`), sin dependencias externas.

- **Application**:  
  Define casos de uso (`UpdateStockByProductIdUseCase`, `GetStockByProductIdUseCase`, `ListAllStockUseCase`, `DeleteStockByProductIdUseCase`).  
  Orquesta la lógica utilizando **puertos de salida** hacia la persistencia.

- **Infrastructure**:  
  - **Adapters de entrada**: Controladores REST y Listeners de RabbitMQ.  
  - **Adapters de salida**: Repositorios JPA que implementan `StockRepositoryPort`.  
  - **Mapper**: conversión entre DTOs y modelo de dominio.  
  - **Config**: configuración de colas de RabbitMQ.

---

## 🔄 Endpoints REST

### 1️⃣ Obtener stock por productId  
`GET /stock/{productId}`  

### 2️⃣ Listar todos los stocks  
`GET /stock`  

### 3️⃣ Actualizar stock por productId  
`PUT /stock/{productId}`  
```json
{
  "quantity": 50
}
```

---

## 📬 Eventos RabbitMQ

El Stock Service se integra de forma **asíncrona** con otros servicios mediante colas:

- **Cola `product.created`**  
  - Consumida al registrar un nuevo producto.  
  - Inicializa el stock en la base de datos (cantidad inicial o `0` si no se envía).

- **Cola `product.deleted`**  
  - Consumida cuando un producto es eliminado.  
  - Borra el `StockItem` asociado al `productId`.

⚠️ La lógica de negocio de estos eventos se delega a casos de uso, manteniendo el listener lo más simple posible.

---

## ✅ Beneficios

- **Separación de responsabilidades**: catálogo (Product Service) y stock gestionados de forma independiente.  
- **Desacoplamiento por eventos**: comunicación asíncrona vía RabbitMQ.  
- **Escalabilidad**: preparado para soportar futuros eventos como ventas, devoluciones o reservas de stock.  
- **Arquitectura limpia**: dominio puro y adaptadores desacoplados.  
