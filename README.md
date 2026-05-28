# 🐄 GanaBáscula — Plataforma Integral Ganadera

Sistema web para la gestión comercial del ganadero bovino colombiano. Automatiza el registro de ventas en báscula, el cálculo de destare, bono por animal y gastos adicionales.

---

## 👥 Integrantes

| Nombre | Código | Rol |
|--------|--------|-----|
| Luis Carlos Moreno Espejo | 2436399 | Frontend, entidades, repositorios, DTOs, módulo de ventas, documentación del proyecto |
| Jesús David Sánchez Peña | — | Seguridad JWT, lógica ganadera, dashboard |
| Julián David Moreno Serna | — | Módulo ICA, reportes |

**Universidad Santo Tomás — Seccional Villavicencio**
**Ingeniería de Sistemas — V Semestre — 2026**

---

## 📋 Descripción del proyecto

GanaBáscula es una plataforma web modular que digitaliza la gestión comercial del ganadero bovino individual. Resuelve el problema del 79% de los predios ganaderos colombianos que operan sin herramientas tecnológicas, dependiendo de papel, esfero y calculadora para registrar sus ventas.

### Problema que resuelve
- Cálculos manuales de ventas generan errores frecuentes
- Sin historial digital de transacciones
- Sin análisis de rentabilidad del negocio
- Dependencia total de agenda física

### Solución
- Cálculo automático de destare (5% machos / 6% hembras)
- Bono por animal: $42.800
- Historial digital privado por ganadero
- Panel de administración con métricas reales
- Módulo de documentos ICA

---

## 🛠️ Stack tecnológico

| Capa | Tecnología |
|------|-----------|
| Backend | Java 17 + Spring Boot 4.0.6 |
| Seguridad | Spring Security 6 + JWT |
| Base de datos | PostgreSQL 16 |
| ORM | Spring Data JPA + Hibernate |
| Mapeo DTOs | MapStruct 1.5.5 |
| Validaciones | Jakarta Bean Validation |
| Documentación API | SpringDoc OpenAPI (Swagger) |
| Frontend | HTML5 + CSS3 + JavaScript |
| Plantillas | Thymeleaf |

---

## 🚀 Cómo ejecutar el proyecto

### Requisitos previos
- Java 17 o superior
- PostgreSQL 16
- IntelliJ IDEA (recomendado)

### Paso 1 — Clonar el repositorio
```bash
git clone https://github.com/yisus25-star/ganaBascula.git
cd ganaBascula
```

### Paso 2 — Crear la base de datos
```sql
CREATE DATABASE ganabascula;
```

### Paso 3 — Configurar application.properties
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ganabascula
spring.datasource.username=postgres
spring.datasource.password=TU_CONTRASEÑA
spring.jpa.hibernate.ddl-auto=update
```

### Paso 4 — Ejecutar el proyecto
Desde IntelliJ IDEA: **Shift + F10**

O desde terminal:
```bash
./mvnw spring-boot:run
```

### Paso 5 — Crear usuario ADMIN manualmente en la BD
```sql
-- Primero arranca el proyecto para que se creen las tablas
-- Luego ejecuta en PostgreSQL:

INSERT INTO usuarios (nombre, cedula, password, rol_id, estado)
VALUES (
  'Administrador',
  '1122513380',
  '$2a$10$N.xQiGQwBjhP1DpB1sF5OeuKkT0bPWCBYUZPZPGKGnB1Y2gB2BSBO',
  (SELECT id FROM roles WHERE nombre = 'ROLE_ADMIN'),
  'ACTIVO'
);
```
> La contraseña encriptada corresponde a: `valery1234`

---

## 🔑 Credenciales de prueba

### Administrador
| Campo | Valor |
|-------|-------|
| Cédula | `1122513380` |
| Contraseña | `valery1234` |
| Rol | ADMIN |

### Ganadero de prueba
| Campo | Valor |
|-------|-------|
| Cédula | `1234567890` |
| Contraseña | `1234567890` |
| Rol | GANADERO |
| Estado | ACTIVO |

---

## 📡 Endpoints principales

### Autenticación
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/v1/auth/registro` | Registrar ganadero |
| POST | `/api/v1/auth/login` | Iniciar sesión — devuelve JWT |
| PUT | `/api/v1/auth/aprobar/{id}` | Aprobar ganadero (ADMIN) |
| PUT | `/api/v1/auth/rechazar/{id}` | Rechazar ganadero (ADMIN) |

### Transacciones
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/v1/transacciones` | Crear venta |
| GET | `/api/v1/transacciones` | Listar ventas del ganadero |
| GET | `/api/v1/transacciones/{id}` | Detalle de una venta |
| POST | `/api/v1/transacciones/{id}/categorias` | Agregar categoría de animales |
| POST | `/api/v1/transacciones/{id}/gastos` | Agregar gasto adicional |
| PATCH | `/api/v1/transacciones/{id}/completar` | Completar venta |
| GET | `/api/v1/transacciones/filtrar/fecha` | Filtrar por fechas |
| GET | `/api/v1/transacciones/filtrar/comprador` | Filtrar por comprador |

### Administración
| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/v1/admin/usuarios` | Listar todos los ganaderos |
| GET | `/api/v1/admin/usuarios/{id}` | Detalle de un ganadero |
| PATCH | `/api/v1/admin/usuarios/{id}/estado` | Cambiar estado |
| GET | `/api/v1/admin/metricas` | Métricas del sistema |
| GET | `/api/v1/dashboard` | Dashboard financiero |

### Documentos ICA
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/v1/documentos-ica/subir/{transaccionId}` | Subir documento ICA |

### Reportes
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/v1/reportes` | Generar reporte por fechas |
| GET | `/api/v1/reportes` | Listar reportes del ganadero |

---

## 📖 Documentación Swagger

Con el proyecto corriendo entra a:

```
http://localhost:8080/swagger-ui/index.html
```

---

## 🌐 Páginas del sistema

| Ruta | Descripción |
|------|-------------|
| `/login` | Iniciar sesión |
| `/register` | Registrarse como ganadero |
| `/dashboard` | Menú principal |
| `/ventas` | Registrar nueva venta |
| `/historial` | Historial de ventas |
| `/reportes` | Generar reportes |
| `/admin` | Panel de administración |

---

## 🏗️ Arquitectura del sistema

```
com.ganabascula
├── config          → RoleInitializer
├── controller      → 8 controllers REST + ViewController
├── dto
│   ├── request     → 8 DTOs de entrada con validaciones
│   └── response    → 10 DTOs de salida
├── entity          → 8 entidades JPA
├── exception       → GlobalExceptionHandler
├── mapper          → TransaccionMapper (MapStruct)
├── repository      → 8 repositorios JPA
├── security
│   ├── config      → SecurityConfig
│   ├── jwt         → JwtService + JwtAuthenticationFilter
│   └── service     → CustomUserDetailService
└── service
    └── impl        → 7 servicios con lógica de negocio
```

---

## 🧮 Lógica ganadera

| Cálculo | Fórmula |
|---------|---------|
| Destare machos | Peso bruto × 5% |
| Destare hembras | Peso bruto × 6% |
| Peso neto | Peso bruto − destare |
| Bono | $42.800 × cantidad de animales |
| Subtotal categoría | (Peso neto × precio/kilo) − bono |
| Total bruto | Suma de subtotales de todas las categorías |
| Total neto | Total bruto − suma de gastos adicionales |

---

## 📁 Estructura de ramas

| Rama | Descripción |
|------|-------------|
| `master` | Código estable y probado |
| `feature/ventas` | Módulo de ventas y frontend |
| `feature/correcciones-y-reportes` | Correcciones de arquitectura y reportes |

---

## 📄 Licencia

Proyecto académico — Universidad Santo Tomás, Seccional Villavicencio — 2026.