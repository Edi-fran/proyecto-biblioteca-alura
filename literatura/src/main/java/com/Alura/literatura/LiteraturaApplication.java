package com.Alura.literatura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestTemplate;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
public class LiteraturaApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(LiteraturaApplication.class, args);
		mostrarMenu(context);
	}

	private static void mostrarMenu(ApplicationContext context) {
		Scanner scanner = new Scanner(System.in);
		int opcion = 0;

		do {
			System.out.println("\nMenú Principal:");
			System.out.println("1. Buscar libro por título");
			System.out.println("2. Listar libros registrados");
			System.out.println("3. Listar autores registrados");
			System.out.println("4. Listar autores vivos en un determinado año");
			System.out.println("5. Listar libros por idioma");
			System.out.println("6. Generar estadísticas");
			System.out.println("7. Top 10 libros más descargados");
			System.out.println("8. Salir");
			System.out.print("Seleccione una opción: ");

			try {
				opcion = Integer.parseInt(scanner.nextLine().trim());
			} catch (NumberFormatException e) {
				System.out.println("Opción no válida, intente nuevamente.");
				continue;
			}

			switch (opcion) {
				case 1 -> buscarLibroPorTitulo(context, scanner);
				case 2 -> listarLibrosRegistrados(context);
				case 3 -> listarAutoresRegistrados(context);
				case 4 -> listarAutoresVivos(context, scanner);
				case 5 -> listarLibrosPorIdioma(context, scanner);
				case 6 -> generarEstadisticas(context);
				case 7 -> mostrarTopLibros(context);
				case 8 -> System.out.println("Saliendo del programa...");
				default -> System.out.println("Opción no válida, intente nuevamente.");
			}
		} while (opcion != 8);
	}

	private static void buscarLibroPorTitulo(ApplicationContext context, Scanner scanner) {
		System.out.print("Ingrese el título del libro a buscar: ");
		String titulo = scanner.nextLine();

		try {
			RestTemplate restTemplate = new RestTemplate();
			String url = "https://gutendex.com/books?search=" + titulo;
			Map<String, Object> response = restTemplate.getForObject(url, Map.class);

			List<Map<String, Object>> libros = (List<Map<String, Object>>) response.get("results");

			if (libros == null || libros.isEmpty()) {
				System.out.println("Libro no existe en la API.");
				return;
			}

			Map<String, Object> libroData = libros.get(0);
			String tituloLibro = (String) libroData.get("title");
			String idioma = ((List<String>) libroData.get("languages")).get(0);
			int numeroDescargas = (int) libroData.get("download_count");
			String urlLibro = (String) ((Map<String, String>) libroData.get("formats")).get("text/html");

			List<Map<String, Object>> autores = (List<Map<String, Object>>) libroData.get("authors");
			String nombreAutor = autores.isEmpty() ? "Desconocido" : (String) autores.get(0).get("name");
			Integer anioNacimiento = autores.isEmpty() ? null : (Integer) autores.get(0).get("birth_year");
			Integer anioFallecimiento = autores.isEmpty() ? null : (Integer) autores.get(0).get("death_year");

			AutorRepository autorRepo = context.getBean(AutorRepository.class);
			LibroRepository libroRepo = context.getBean(LibroRepository.class);

			Autor autor = autorRepo.findByNombre(nombreAutor).orElseGet(() -> {
				Autor nuevoAutor = new Autor();
				nuevoAutor.setNombre(nombreAutor);
				nuevoAutor.setAnioNacimiento(anioNacimiento);
				nuevoAutor.setAnioFallecimiento(anioFallecimiento);
				autorRepo.save(nuevoAutor);
				return nuevoAutor;
			});

			Optional<Libro> libroExistente = libroRepo.findByTituloAndIdioma(tituloLibro, idioma);
			if (libroExistente.isPresent()) {
				System.out.println("El libro ya está registrado en la base de datos.");
			} else {
				Libro libro = new Libro();
				libro.setTitulo(tituloLibro);
				libro.setIdioma(idioma);
				libro.setNumeroDescargas(numeroDescargas);
				libro.setUrl(urlLibro);
				libro.setAutor(autor);
				libroRepo.save(libro);

				System.out.println("Libro registrado exitosamente:");
				System.out.println("Título: " + tituloLibro);
				System.out.println("Idioma: " + idioma);
				System.out.println("Número de descargas: " + numeroDescargas);
				System.out.println("Autor: " + nombreAutor);
				System.out.println("Año de nacimiento del autor: " + (anioNacimiento != null ? anioNacimiento : "Desconocido"));
				System.out.println("Año de fallecimiento del autor: " + (anioFallecimiento != null ? anioFallecimiento : "Sigue vivo o desconocido"));
				System.out.println("URL: " + urlLibro);
			}
		} catch (Exception e) {
			System.err.println("Error al buscar el libro: " + e.getMessage());
		}
	}

	private static void listarLibrosRegistrados(ApplicationContext context) {
		LibroRepository libroRepo = context.getBean(LibroRepository.class);
		List<Libro> libros = libroRepo.findAll();
		if (libros.isEmpty()) {
			System.out.println("No hay libros registrados.");
		} else {
			libros.forEach(libro -> {
				System.out.println("Título: " + libro.getTitulo());
				System.out.println("Idioma: " + libro.getIdioma());
				System.out.println("Número de descargas: " + libro.getNumeroDescargas());
				System.out.println("Autor: " + libro.getAutor().getNombre());
				System.out.println("URL: " + libro.getUrl());
				System.out.println("---------------------------------");
			});
		}
	}

	private static void listarAutoresRegistrados(ApplicationContext context) {
		AutorRepository autorRepo = context.getBean(AutorRepository.class);
		List<Autor> autores = autorRepo.findAll();
		if (autores.isEmpty()) {
			System.out.println("No hay autores registrados.");
		} else {
			autores.forEach(autor -> {
				System.out.println("Autor: " + autor.getNombre());
				System.out.println("Año de nacimiento: " + autor.getAnioNacimiento());
				System.out.println("Año de fallecimiento: " + (autor.getAnioFallecimiento() != null ? autor.getAnioFallecimiento() : "Vivo"));
				System.out.println("Nacionalidad: " + autor.getNacionalidad());
				System.out.println("---------------------------------");
			});
		}
	}

	private static void listarAutoresVivos(ApplicationContext context, Scanner scanner) {
		try {
			System.out.print("Ingrese el año para buscar autores vivos: ");
			String entrada = scanner.nextLine().trim();
			int anio = Integer.parseInt(entrada);

			AutorRepository autorRepo = context.getBean(AutorRepository.class);
			List<Autor> autores = autorRepo.findByAnioFallecimientoIsNullOrAnioFallecimientoGreaterThan(anio);

			if (autores.isEmpty()) {
				System.out.println("No hay autores vivos en ese año.");
			} else {
				autores.forEach(autor -> {
					System.out.println("Autor: " + autor.getNombre());
					System.out.println("Año de nacimiento: " + autor.getAnioNacimiento());
					System.out.println("Nacionalidad: " + autor.getNacionalidad());
					System.out.println("---------------------------------");
				});
			}
		} catch (NumberFormatException e) {
			System.out.println("Por favor, ingrese un número válido para el año.");
		}
	}

	private static void listarLibrosPorIdioma(ApplicationContext context, Scanner scanner) {
		System.out.println("Ingrese el idioma para buscar los libros:");
		System.out.println("es - español");
		System.out.println("en - inglés");
		System.out.println("fr - francés");
		System.out.println("pt - portugués");
		System.out.print("Seleccione un idioma (código): ");
		String idioma = scanner.nextLine();

		switch (idioma.toLowerCase()) {
			case "es" -> System.out.println("Buscando libros en español...");
			case "en" -> System.out.println("Buscando libros en inglés...");
			case "fr" -> System.out.println("Buscando libros en francés...");
			case "pt" -> System.out.println("Buscando libros en portugués...");
			default -> {
				System.out.println("Idioma no válido. Intente nuevamente.");
				return;
			}
		}

		LibroRepository libroRepo = context.getBean(LibroRepository.class);
		List<Libro> libros = libroRepo.findByIdioma(idioma);
		if (libros.isEmpty()) {
			System.out.println("No hay libros registrados en el idioma seleccionado.");
		} else {
			libros.forEach(libro -> {
				System.out.println("Título: " + libro.getTitulo());
				System.out.println("Idioma: " + libro.getIdioma());
				System.out.println("Número de descargas: " + libro.getNumeroDescargas());
				System.out.println("Autor: " + libro.getAutor().getNombre());
				System.out.println("URL: " + libro.getUrl());
				System.out.println("---------------------------------");
			});
		}
	}

	private static void generarEstadisticas(ApplicationContext context) {
		LibroRepository libroRepo = context.getBean(LibroRepository.class);
		List<Libro> libros = libroRepo.findAll();

		if (libros.isEmpty()) {
			System.out.println("No hay libros registrados para generar estadísticas.");
			return;
		}

		DoubleSummaryStatistics stats = libros.stream()
				.mapToDouble(Libro::getNumeroDescargas)
				.summaryStatistics();

		System.out.println("Estadísticas de descargas:");
		System.out.println("Total de descargas: " + stats.getSum());
		System.out.println("Número promedio de descargas: " + stats.getAverage());
		System.out.println("Mayor número de descargas: " + stats.getMax());
		System.out.println("Menor número de descargas: " + stats.getMin());
		System.out.println("Cantidad de libros: " + stats.getCount());
	}

	private static void mostrarTopLibros(ApplicationContext context) {
		LibroRepository libroRepo = context.getBean(LibroRepository.class);
		List<Libro> libros = libroRepo.findAll();

		if (libros.isEmpty()) {
			System.out.println("No hay libros registrados.");
			return;
		}

		System.out.println("Top 10 libros más descargados:");
		libros.stream()
				.sorted((l1, l2) -> Integer.compare(l2.getNumeroDescargas(), l1.getNumeroDescargas()))
				.limit(10)
				.forEach(libro -> {
					System.out.println("Título: " + libro.getTitulo());
					System.out.println("Número de descargas: " + libro.getNumeroDescargas());
					System.out.println("Autor: " + libro.getAutor().getNombre());
					System.out.println("Idioma: " + libro.getIdioma());
					System.out.println("URL: " + libro.getUrl());
					System.out.println("---------------------------------");
				});
	}
}



