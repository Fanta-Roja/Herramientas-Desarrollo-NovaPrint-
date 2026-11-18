NovaPrint Multiservicios

Aplicación web hecha con Java y Spring Boot para un negocio de impresiones, fotocopias y servicios de oficina. Rama de integración: develop. Versión estable: main.

Proyecto individual: una sola persona desarrolla, documenta, prueba y administra el repositorio.

Abrir la aplicación

Requisitos: Java 17 o superior. No hace falta instalar Maven (el proyecto incluye mvnw). Todos los comandos se ejecutan dentro de la carpeta NovaPrint, la que contiene el pom.xml.

Sin MySQL (base en memoria)

Los datos se borran al apagar la aplicación. Usa el perfil local, que ya trae el administrador y los límites de acceso:

powershell
$env:SPRING_PROFILES_ACTIVE="local"; .\mvnw.cmd spring-boot:run

Funciones
Portada con los servicios del negocio, sus precios y un formulario de contacto.
Cotizador de precios en pantalla: (precio + recargo por color) x cantidad, con 10 % de descuento por doble cara en los servicios que lo permiten. El servidor vuelve a calcular el total al guardar el pedido.
Registro e inicio de sesión de clientes, con contraseñas cifradas (BCrypt).
Validaciones en los formularios:
Registro: nombre obligatorio (máximo 100 caracteres), correo válido, teléfono de 9 dígitos, contraseña de 6 a 72 caracteres y confirmación de la contraseña.
Contacto: nombre y correo obligatorios, teléfono de hasta 20 caracteres y mensaje de hasta 1000 caracteres.
Bloqueo de cuenta: tras 5 contraseñas incorrectas seguidas, la cuenta se bloquea 15 minutos y se muestra un aviso. Un ingreso correcto reinicia el contador. Los valores se cambian con app.login.max-intentos y app.login.bloqueo-minutos.
Pedidos: el cliente crea un pedido (servicio, cantidad, color, doble cara, notas) y sigue su estado en Mis pedidos.
Panel del administrador: lista todos los pedidos, cambia su estado (Recibido, En proceso, Listo, Entregado, Cancelado) y muestra los mensajes de contacto.
Dashboard del administrador: tarjetas con pedidos, pendientes, ventas estimadas y clientes; gráficos de pedidos por servicio y por estado; últimos pedidos; lista de usuarios con cambio de rol.
Página de error propia cuando no se encuentra lo que se busca.
Interfaz adaptable a móvil con Bootstrap 5.
Tecnologías

Java 17 · Spring Boot · Spring Security · Spring Data JPA · Bean Validation · Flyway · Thymeleaf · MySQL (H2 en memoria para el perfil local y las pruebas) · Bootstrap 5 · Maven
Usuarios y permisos
Cliente (USER): se registra desde /registro. Puede cotizar, hacer pedidos y ver los suyos.
Administrador (ADMIN): se crea al arrancar con ADMIN_EMAIL y ADMIN_PASSWORD. En el perfil local es admin@novaprint.pe / admin123. Usa otra clave antes de publicar. Nadie puede registrarse como administrador desde la página.
Las rutas /admin/** solo las puede abrir un administrador. Un administrador no puede cambiarse el rol a sí mismo.
Comprobaciones
powershell
.\mvnw.cmd verify

Ejecuta las pruebas automáticas con una base en memoria, sin necesitar MySQL:

CotizadorServiceTest: cálculo de precios (blanco y negro, color, doble cara y cantidad inválida).
DashboardServiceTest: totales, ventas, barras de los gráficos y últimos pedidos.
NovaPrintApplicationTests: la aplicación arranca.
Objetivo del proyecto

Desarrollar un sitio web para un negocio de impresiones, fotocopias y servicios de oficina, aplicando control de versiones con Git y GitHub: ramas por funcionalidad, Pull Requests y un historial ordenado que permita mantener y entregar el proyecto por etapas.

Alcance

Incluye: portada, cotizador, registro e inicio de sesión con validaciones y bloqueo por intentos, pedidos, panel y dashboard del administrador, y migraciones de base de datos.

No incluye: pagos en línea, facturación electrónica ni aplicación móvil.
