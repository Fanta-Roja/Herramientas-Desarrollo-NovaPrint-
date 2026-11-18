# NovaPrint Multiservicios

Aplicación web hecha con Java y Spring Boot para un negocio de impresiones, fotocopias y servicios de oficina. Rama de integración: `develop`. Versión estable: `main`.

Proyecto **individual**: una sola persona desarrolla, documenta, prueba y administra el repositorio.

## Abrir la aplicación

Requisitos: Java 17 o superior. No hace falta instalar Maven (el proyecto incluye `mvnw`) ni MySQL para probarla.

Sin MySQL (base de datos en memoria; los datos se borran al apagar):

```powershell
$env:SPRING_PROFILES_ACTIVE="local"; .\mvnw.cmd spring-boot:run
```

Con MySQL en `localhost:3306` (usuario y clave `root` por defecto; la base `bd_novaprint` se crea sola):

```powershell
.\mvnw.cmd spring-boot:run
```

Abre http://localhost:8080 cuando la terminal muestre `Started NovaPrintApplication`. Para apagarla, presiona `Ctrl + C`.

## Funciones

- **Portada** con los servicios del negocio, sus precios y un formulario de contacto.
- **Cotizador** de precios en pantalla: `(precio + recargo por color) x cantidad`, con 10 % de descuento por doble cara en los servicios que lo permiten. El servidor vuelve a calcular el total al guardar el pedido.
- **Registro e inicio de sesión** de clientes, con contraseñas cifradas (BCrypt) y validaciones en el formulario de registro.
- **Bloqueo de cuenta** tras varios intentos fallidos de inicio de sesión.
- **Pedidos:** el cliente crea un pedido (servicio, cantidad, color, doble cara, notas) y sigue su estado en **Mis pedidos**.
- **Panel del administrador:** lista todos los pedidos, cambia su estado (Recibido, En proceso, Listo, Entregado, Cancelado) y muestra los mensajes de contacto.
- **Dashboard del administrador:** tarjetas con pedidos, pendientes, ventas estimadas y clientes; gráficos de pedidos por servicio y por estado; últimos pedidos; lista de usuarios con cambio de rol.
- Interfaz adaptable a móvil con Bootstrap 5.

## Tecnologías

Java 17 · Spring Boot · Spring Security · Spring Data JPA · Thymeleaf · MySQL (H2 en memoria para el perfil `local` y las pruebas) · Bootstrap 5 · Maven

## Estructura del proyecto

```
NovaPrint
├── src/main/java/com/example/novaprint
│   ├── config/        Seguridad y datos iniciales
│   ├── controller/    Controladores web
│   ├── entity/        Usuario, Servicio, Pedido, Contacto
│   ├── repository/    Acceso a la base de datos
│   └── service/       Cotizador, dashboard e inicio de sesión
├── src/main/resources
│   ├── templates/     Vistas Thymeleaf
│   └── static/        Estilos (css) y scripts (js)
├── src/test           Pruebas automáticas
├── pom.xml            Dependencias
└── README.md
```

## Usuarios y permisos

- **Cliente (`USER`):** se registra desde `/registro`. Puede cotizar, hacer pedidos y ver los suyos.
- **Administrador (`ADMIN`):** se crea al arrancar con las variables `ADMIN_EMAIL` y `ADMIN_PASSWORD` (por defecto `admin@novaprint.pe` / `admin123`). **Cámbialas antes de publicar.** Nadie puede registrarse como administrador desde la página.
- Las rutas `/admin/**` solo las puede abrir un administrador. Un administrador no puede cambiarse el rol a sí mismo.

## Variables de entorno



| `DB_URL` | URL JDBC de MySQL | `jdbc:mysql://localhost:3306/bd_novaprint...` |
| `DB_USER` / `DB_PASSWORD` | Credenciales de la base | `root` / `root` |
| `ADMIN_EMAIL` / `ADMIN_PASSWORD` | Cuenta de administrador inicial | ver arriba |
| `PORT` | Puerto del servidor | `8080` |

## Comprobaciones

```powershell
.\mvnw.cmd verify
```

Ejecuta las pruebas automáticas con una base en memoria, sin necesitar MySQL:

- `CotizadorServiceTest`: cálculo de precios (blanco y negro, color, doble cara y cantidad inválida).
- `DashboardServiceTest`: totales, ventas, barras de los gráficos y últimos pedidos.
- `NovaPrintApplicationTests`: la aplicación arranca.

## Objetivo del proyecto

Desarrollar un sitio web para un negocio de impresiones, fotocopias y servicios, aplicando control de versiones con Git y GitHub: ramas por funcionalidad, Pull Requests y un historial ordenado que permita mantener y entregar el proyecto por etapas.

## Alcance

**Incluye:** portada, cotizador, registro e inicio de sesión, pedidos, panel y dashboard del administrador.

**No incluye:** pagos en línea, facturación electrónica ni aplicación móvil.

## Fases y entregables

| Avance | Fase | Entregable |
|---|---|---|
| 1 | Planeación y desarrollo inicial | Repositorio en GitHub, rama `main`, estructura base y este README |
| 2 | Desarrollo con ramas | Funciones del sistema en ramas `feature/*`, integradas con Pull Requests en `develop`, y una versión en `release/*` |
| 3 | Optimización | Refactorización, documentación actualizada y poda de ramas |

## Estrategia de ramas

| Rama | Para qué se usa |
|---|---|
| `main` | Versión estable. Solo recibe cambios ya probados, desde `release/*` o `hotfix/*`. |
| `develop` | Integración: aquí se juntan las ramas `feature/*` antes de preparar una versión. |
| `feature/*` | Una rama por función o mejora. Sale de `develop` y vuelve a `develop` con un Pull Request. |
| `release/*` | Prepara una versión (por ejemplo `release/1.0.0`): se actualiza el README y se fusiona a `main`, donde se crea la etiqueta `v1.0.0`. |
| `hotfix/*` | Corrige un error urgente. Sale de `main` y se fusiona en `main` y en `develop`. |

Flujo:

```
feature/*  ->  develop  ->  release/*  ->  main
hotfix/*   ->  main  (y luego develop)
```

Ramas creadas hasta ahora: `feature/validaciones`, `feature/limite-login` y `fix/credenciales` (fusionadas), `develop` y `release/1.0.0`.

## Roles y ramas

El proyecto es individual. Una sola persona cumple todos los roles, y cada área de trabajo usa su propia rama.

| Integrante | Rol | Actividad | Rama |
|---|---|---|---|
| [Fabioo] | Líder y desarrollador | Planificar el trabajo y programar el sistema | `feature/*` |
| [Fabioo] | Responsable de versiones | Administrar ramas, Pull Requests, versiones y poda | `develop`, `release/*`, `hotfix/*` |
| [Fabio] | Documentación | Mantener el README y registrar las mejoras | `feature/documentacion` |


## Versión 1.0.0

- Cotizador de precios en la portada.
- Registro e inicio de sesión de clientes.
- Pedidos y seguimiento de su estado.
- Panel y dashboard de administración.
- Validaciones del registro y bloqueo de cuenta por intentos fallidos.
Actualizacion