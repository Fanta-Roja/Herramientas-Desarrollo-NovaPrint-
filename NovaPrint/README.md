# NovaPrint Multiservicios

Sistema web para un negocio de impresiones, fotocopias y servicios de oficina.

- Portada con servicios, cotizador de precios y formulario de contacto.
- Registro e inicio de sesión de clientes.
- Pedidos de los clientes y seguimiento de su estado.
- Panel y dashboard del administrador.

Tecnologías: Java 17, Spring Boot, Spring Security, Spring Data JPA, Thymeleaf, MySQL y Bootstrap 5.

## Ejecutar
Sin MySQL (base en memoria):
```powershell
$env:SPRING_PROFILES_ACTIVE="local"; .\mvnw.cmd spring-boot:run
```
Con MySQL en `localhost:3306` (usuario y clave `root`): `.\mvnw.cmd spring-boot:run`

Abre http://localhost:8080 — Administrador: `admin@novaprint.pe` / `admin123`
