
# Proyecto Biblioteca Alura

Este proyecto es una aplicación desarrollada en **Java** con **Spring Boot** que consume la API de Gutendex para gestionar libros y autores. Además, utiliza **PostgreSQL** como base de datos para almacenar la información.

## Funcionalidades

1. **Buscar libro por título**:
   - Busca un libro en la API de Gutendex por su título.
   - Si el libro no existe en la base de datos, lo registra junto con el autor asociado.

2. **Listar libros registrados**:
   - Muestra todos los libros que están almacenados en la base de datos.

3. **Listar autores registrados**:
   - Muestra la información de todos los autores almacenados en la base de datos.

4. **Listar autores vivos en un determinado año**:
   - Permite buscar autores que estaban vivos en un año específico.

5. **Listar libros por idioma**:
   - Lista los libros registrados en la base de datos según el idioma seleccionado.

6. **Top 10 libros más descargados** (funcionalidad opcional):
   - Muestra los 10 libros con más descargas, utilizando la API o la base de datos.

## Tecnologías utilizadas

- **Java 17**
- **Spring Boot 3.4**
- **PostgreSQL**
- **RestTemplate** para el consumo de la API de Gutendex
- **Hibernate** para la gestión de la base de datos

## Requisitos previos

1. **Instalar Java y Maven**:
   - [Java](https://www.oracle.com/java/technologies/javase-downloads.html)
   - [Maven](https://maven.apache.org/download.cgi)

2. **Configurar PostgreSQL**:
   - Crear una base de datos para el proyecto.
   - Configurar las credenciales en el archivo `application.properties`.

3. **Clonar el repositorio**:
   ```bash
   git clone https://github.com/Edi-fran/proyecto-biblioteca-alura.git
   cd proyecto-biblioteca-alura
   ```

4. **Compilar y ejecutar el proyecto**:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

## Enlaces importantes

- **Video para comprobar la funcionalidad**: [Ver video](https://drive.google.com/file/d/1gQ-U5iCb0wySNIUSPdLxUc_1wnm3togO/view?usp=sharing)
- **Base de datos**: [Descargar base de datos](https://drive.google.com/file/d/1xDgyTrE0gfy4veAfkSfl-B3-l562-w5z/view?usp=sharing)
- **Demostración de la funcionalidad**: [Ver funcionalidad](https://drive.google.com/file/d/12RRlgNtSabKfUl7ewatYiNQYr6cn-mhu/view?usp=sharing)

## Cómo contribuir

1. Haz un fork de este repositorio.
2. Crea una nueva rama para tus cambios:
   ```bash
   git checkout -b feature/nueva-funcionalidad
   ```
3. Realiza los cambios y haz un commit:
   ```bash
   git commit -m "Agrega nueva funcionalidad"
   ```
4. Envía tus cambios al repositorio remoto:
   ```bash
   git push origin feature/nueva-funcionalidad
   ```
5. Abre un Pull Request para revisión.

---

### Autor

Desarrollado por **Edilson Guillin** como parte del proyecto **Biblioteca Alura**.
