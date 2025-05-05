# microservicio-reuniones
microservicio 2do parcial tecno web 2 1-2025

Este microservicio, desarrollado con **Spring Boot**, permite gestionar reuniones virtuales de Zoom, incluyendo funcionalidades como la creación, consulta, actualización y eliminación de reuniones a través de la API de Zoom.

---

## Características

- Crear reuniones programadas o instantáneas en Zoom.  
- Listar reuniones existentes asociadas a un usuario.  
- Editar detalles de reuniones como horario, duración o tema.  
- Eliminar reuniones programadas.  
- Autenticación con OAuth2  para acceso a la API de Zoom.  


---

## Tecnologías

- Java 17+  
- Spring Boot 3.x  
- Spring Web  
- Spring Security (OAuth2/JWT)  
- Zoom API v2  
- Maven   
- Lombok  


---

## Instalación

```bash
# Clonar el repositorio
git clone https://github.com/tu-usuario/zoom-meetings-service.git
cd zoom-meetings-service

# Compilar el proyecto
./mvnw clean install

# Ejecutar el microservicio
./mvnw spring-boot:run
