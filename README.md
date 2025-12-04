# 🟦 Sistema de Gestión de Productos e Inventario -- Microservicios (Spring Boot)

Este proyecto implementa una arquitectura de microservicios en **Spring
Boot 4**, utilizando comunicación interna mediante **RestTemplate**,
autenticación básica mediante **API Keys**, persistencia con **JPA +
SQLite**, y pruebas unitarias/integración con **JUnit 5**.

Incluye los microservicios:

-   **Producto Service** -- CRUD de productos\
-   **Inventario Service** -- Gestión de stock y proceso de compra\
-   **Gateway / Comunicaciones** -- Lógica de consumo entre servicios y
    filtros de autenticación

## 📑 Tabla de Contenido

1.  Arquitectura General\
2.  Tecnologías Utilizadas\
3.  Estructura del Proyecto\
4.  Flujo de Comunicación entre Servicios\
5.  Autenticación con API Keys\
6.  Configuración de Base de Datos\
7.  Endpoints\
8.  Proceso de Compra\
9.  Pruebas Unitarias\
10. Pruebas de Integración\
11. Ejecución del Proyecto\
12. Docker Compose\
13. Diagramas\
14. Mejoras Futuras

## 🏗️ Arquitectura General

El sistema está compuesto por dos microservicios independientes:

-   producto-service\
-   inventario-service

##Diagrama

┌──────────────────┐        ┌──────────────────┐         ┌──────────────────┐
│  ProductoService  │ ----> │ InventarioService │ <----- │  CompraService   │
└──────────────────┘        └──────────────────┘         └──────────────────┘
          │                          │                         │
          └────────────── API KEY Security ────────────────────┘


Cada servicio tiene su propia base de datos SQLite y expone sus
operaciones a través de REST.

## 🔧 Tecnologías Utilizadas

  Componente      Tecnología
  --------------- -------------------------
  Backend         Spring Boot 4
  Persistencia    Spring Data JPA
  Base de datos   SQLite
  Cliente HTTP    RestTemplate
  Seguridad       API Key Authentication
  Testing         JUnit 5, Mockito
  Contenedores    Docker & Docker Compose

## 📁 Estructura del Proyecto

    /producto-service
      /src/main/java
      /src/test/java
      producto.db

    /inventario-service
      /src/main/java
      /src/test/java
      inventario.db

    docker-compose.yml
    README.md

## 🔄 Flujo de Comunicación entre Servicios

1.  InventarioService recibe la compra.\
2.  Valida stock.\
3.  Llama a ProductoService.\
4.  Actualiza inventario.\
5.  Retorna respuesta.

## 🔐 Autenticación con API Keys

Se envía en cada request:

    X-API-KEY: <clave>

## 🗄️ Configuración de Base de Datos

    spring.datasource.url=jdbc:sqlite:producto.db
    spring.datasource.driver-class-name=org.sqlite.JDBC
    spring.jpa.hibernate.ddl-auto=update

## 📌 Endpoints

### Producto Service

  Método   Endpoint          Descripción
  -------- ----------------- ------------------
  POST     /productos        Crear producto
  GET      /productos/{id}   Obtener producto
  GET      /productos        Listar productos

### Inventario Service

  Método   Endpoint             Descripción
  -------- -------------------- ------------------
  POST     /inventario          Crear inventario
  POST     /inventario/compra   Procesar compra
  GET      /inventario/{id}     Ver inventario

## 🛒 Proceso de Compra

1.  Validar stock\
2.  Consultar producto\
3.  Actualizar inventario\
4.  Retornar respuesta

## 🧪 Pruebas Unitarias

✔ Creación de productos\
✔ Gestión de inventario\
✔ Compra\
✔ Comunicación entre microservicios\
✔ Errores: no encontrado, stock insuficiente

## 🔍 Pruebas de Integración

Se valida:

-   Repositorios JPA\
-   Servicios reales\
-   Endpoints usando RestClient

## ▶️ Ejecución del Proyecto

    mvn clean install
    mvn spring-boot:run

## 🐳 Docker Compose

    version: "3.9"
    services:
      producto-service:
        build: ./producto-service
        ports:
          - "8081:8081"

      inventario-service:
        build: ./inventario-service
        ports:
          - "8082:8082"

## ⚙️ Configuración y Ejecución

1. Clonar el repositorio

git clone <https://github.com/acaucali/PruebaLinktic.git>
cd proyecto

2. Ejecutar cada microservicio localmente:

mvn spring-boot:run

3. Puertos recomendados

| Servicio   | Puerto |
| ---------- | ------ |
| Inventario | 8090   |
| Compras    | 8090   |
| Productos  | 9090   |

## 📬 Colección Postman

Postman/coleccion-servicios.json

Incluye:

✔ Inventario
✔ Productos
✔ Compras
✔ Autenticación API KEY
✔ Variables de entorno (host, api_key)

## ▶️ Ejecutar la Colección en Postman

1. Abrir Postman

2. Importar la colección

3. Crear un Environment con variables:

| Variable          | Ejemplo                                        |
| ----------------- | ---------------------------------------------- |
| `host_inventario` | [http://localhost:8081](http://localhost:8090) |
| `host_productos`  | [http://localhost:8083](http://localhost:9090) |
| `host_compras`    | [http://localhost:8082](http://localhost:8090) |
| `api_key`         | prueba-linktic-service                            |


4. Seleccionar el environment

5. Ejecutar cualquier request o usar Collection Runner
