# API de Gestión de Empleados

Esta es una API RESTful construida con Spring Boot para gestionar información de empleados.

- La aplicación tiene la funcionalidad de imprimir los headers de los requests
- Tiene pruebas unitarias automatizadas
- Bitacora de eventos

## Tabla de Contenidos

* [Tecnologías Utilizadas](#tecnologías-utilizadas)
* [Requisitos Previos](#requisitos-previos)
* [Instalación](#instalación)
* [Configuración](#configuración)
* [Ejecución](#ejecución)
* [Documentación de la API (Swagger UI)](#documentación-de-la-api-swagger-ui)
* [Ejemplos de Consultas (cURL)](#ejemplos-de-consultas-curl)

## Tecnologías Utilizadas

* Java
* Spring Boot

## Requisitos Previos

Antes de comenzar, asegúrate de tener instalado lo siguiente:

* Java JDK
* Algún IDE

## Instalación

1.  Clona el repositorio:

    ```bash
    git clone <URL_DEL_REPOSITORIO>
    cd <NOMBRE_DEL_PROYECTO>
    ```
  o descomprime el archivo adjunto

2.  Construye el proyecto con Maven:

    ```bash
    mvn clean install
    ```

## Configuración

La configuración de la base de datos (y otras configuraciones) se encuentra en el archivo `application.properties`.

**Importante:** Para obtener la cadena de conexión de la base de datos y el nombre de la base de datos, consulta el correo electrónico proporcionado.

Ejemplo de `application.properties`:

```properties
spring.data.mongodb.uri=CANDENA DE CONEXION
spring.data.mongodb.database=BASE DE DATOS
 ```
## Ejecución
Ejecuta la aplicación Spring Boot:

mvn spring-boot:run
en caso de ECLIPSE solamente dar click derecho al proyecto -> Run AS -> Java aplication

La aplicación se ejecutará en el puerto 8080 por defecto.

## Documentación de la API (Swagger UI)
La documentación de la API está disponible en Swagger UI:

http://localhost:8080/swagger-ui/index.html

## Ejemplos de Consultas (cURL)
Aquí hay algunos ejemplos de cómo usar la API con cURL:

Obtener todos los empleados
```bash
curl -X GET http://localhost:8080/api/Empleados/allEmpleados
```

Obtener empleado por ID
```bash
curl -X 'GET' \
  'http://localhost:8080/api/Empleados/682cb64b81db6d1e50c5b14b' \
  -H 'accept: */*'
```

Crear empleado
```bash
curl -X 'POST' \
  'http://localhost:8080/api/Empleados' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '[
  {
    "primerNombre": "Peter",
    "segundoNombre": "",
    "primerApellido": "Parker",
    "segundoApellido": "",
    "edad": 25,
    "sexo": "M",
    "fechaNacimiento": "2000-05-20",
    "puesto": "fotografo"
  }
]'
```

Eliminar empleado
```bash
curl -X 'DELETE' \
  'http://localhost:8080/api/Empleados/682ca34923aefb6eac2c7a89' \
  -H 'accept: */*'
```

Actualizar empleado
```bash
curl -X 'PUT' \
  'http://localhost:8080/api/Empleados/682cb64b81db6d1e50c5b14b' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "primerNombre": "Juan",
  "segundoNombre": "Antonio",
  "primerApellido": "Perez",
  "segundoApellido": "Jimenez",
  "edad": 40,
  "sexo": "H",
  "fechaNacimiento": "1985-05-20",
  "puesto": "Desarrollador"

}'
```
Obtener Bitacoras
```bash
curl -X 'GET' \
  'http://localhost:8080/api/logs/logs' \
  -H 'accept: */*'
```

