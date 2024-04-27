import java.io.*;
import java.util.*;

// Enum para los tipos de libros
enum TipoLibro {
    TERROR, COMEDIA, POLICIACA
}

// Interfaz para objetos que pueden ser exportados a un archivo
interface Exportable {
    void exportar(String archivo) throws IOException;
}

// Excepción personalizada para cuando el catálogo está lleno
class CatalogoLlenoException extends Exception {
    public CatalogoLlenoException(String mensaje) {
        super(mensaje);
    }
}

// Excepción personalizada para cuando se intenta realizar una operación en un catálogo inexistente
class CatalogoInexistenteException extends RuntimeException {
    public CatalogoInexistenteException(String mensaje) {
        super(mensaje);
    }
}

// Excepción personalizada para cuando se busca un libro inexistente
class LibroNoEncontradoException extends Exception {
    public LibroNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}

// Clase base para los libros
abstract class Libro implements Serializable {
    private String autor;
    private int numeroPaginas;
    private String ISBN;

    public Libro(String autor, int numeroPaginas, String ISBN) {
        this.autor = autor;
        this.numeroPaginas = numeroPaginas;
        this.ISBN = ISBN;
    }

    public String getAutor() {
        return autor;
    }

    public int getNumeroPaginas() {
        return numeroPaginas;
    }

    public String getISBN() {
        return ISBN;
    }

    @Override
    public String toString() {
        return "Autor: " + autor + "\nNúmero de páginas: " + numeroPaginas + "\nISBN: " + ISBN;
    }
}

// Clases específicas para cada tipo de libro
class LibroTerror extends Libro {
    private int calificacion;

    public LibroTerror(String autor, int numeroPaginas, String ISBN, int calificacion) {
        super(autor, numeroPaginas, ISBN);
        this.calificacion = calificacion;
    }

    @Override
    public String toString() {
        return "Tipo: Terror\n" + super.toString() + "\nCalificación: " + calificacion;
    }
}

class LibroComedia extends Libro {
    private String tipoHumor;

    public LibroComedia(String autor, int numeroPaginas, String ISBN, String tipoHumor) {
        super(autor, numeroPaginas, ISBN);
        this.tipoHumor = tipoHumor;
    }

    @Override
    public String toString() {
        return "Tipo: Comedia\n" + super.toString() + "\nTipo de Humor: " + tipoHumor;
    }
}

class LibroPoliciaca extends Libro {
    private String trama;
    private List<String> personajes;

    public LibroPoliciaca(String autor, int numeroPaginas, String ISBN, String trama, List<String> personajes) {
        super(autor, numeroPaginas, ISBN);
        this.trama = trama;
        this.personajes = personajes;
    }

    @Override
    public String toString() {
        return "Tipo: Policiaca\n" + super.toString() + "\nTrama: " + trama + "\nPersonajes: " + personajes;
    }
}

// Clase para representar el catálogo de la biblioteca
class Catalogo implements Exportable {
    private int capacidad;
    private Map<String, Libro> libros;

    public Catalogo(int capacidad) {
        this.capacidad = capacidad;
        this.libros = new HashMap<>();
    }

    public void agregarLibro(Libro libro) throws CatalogoLlenoException {
        if (libros.size() >= capacidad) {
            throw new CatalogoLlenoException("No hay espacio en el catálogo para agregar más libros.");
        }
        libros.put(libro.getISBN(), libro);
    }

    public void sacarLibro(String ISBN) throws LibroNoEncontradoException {
        if (!libros.containsKey(ISBN)) {
            throw new LibroNoEncontradoException("No hay libro con ISBN " + ISBN + " en el catálogo.");
        }
        libros.remove(ISBN);
    }

    public Libro buscarLibro(String ISBN) throws LibroNoEncontradoException {
        Libro libro = libros.get(ISBN);
        if (libro == null) {
            throw new LibroNoEncontradoException("No hay libro con ISBN " + ISBN + " en el catálogo.");
        }
        return libro;
    }

    public int numeroLibros() {
        return libros.size();
    }

    public void mostrarLibros() {
        System.out.println("=== Información de libros en el catálogo ===");
        for (Libro libro : libros.values()) {
            System.out.println("--------------------------------------------");
            System.out.println(libro);
            System.out.println("--------------------------------------------");
        }
    }

    @Override
    public void exportar(String archivo) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(archivo))) {
            out.writeObject(libros);
        }
        System.out.println("Catálogo exportado exitosamente a " + archivo);
    }
}

// Clase para representar una biblioteca
class Biblioteca {
    private String nombre;
    private String director;
    private Catalogo catalogo;

    public Biblioteca(String nombre, String director) {
        this.nombre = nombre;
        this.director = director;
    }

    public void construirCatalogo(int capacidad) {
        catalogo = new Catalogo(capacidad);
    }

    public void agregarLibro(Libro libro) throws CatalogoInexistenteException, CatalogoLlenoException {
        if (catalogo == null) {
            throw new CatalogoInexistenteException("El catálogo no ha sido construido.");
        }
        catalogo.agregarLibro(libro);
    }

    public void sacarLibro(String ISBN) throws CatalogoInexistenteException, LibroNoEncontradoException {
        if (catalogo == null) {
            throw new CatalogoInexistenteException("El catálogo no ha sido construido.");
        }
        catalogo.sacarLibro(ISBN);
    }

    public Libro buscarLibro(String ISBN) throws CatalogoInexistenteException, LibroNoEncontradoException {
        if (catalogo == null) {
            throw new CatalogoInexistenteException("El catálogo no ha sido construido.");
        }
        return catalogo.buscarLibro(ISBN);
    }

    public int consultarNumeroLibros() {
        return (catalogo != null) ? catalogo.numeroLibros() : 0;
    }

    public void mostrarCatalogo() {
        if (catalogo == null) {
            System.out.println("El catálogo no ha sido construido.");
            return;
        }
        catalogo.mostrarLibros();
    }

    public void exportarCatalogo(String archivo) throws CatalogoInexistenteException, IOException {
        if (catalogo == null) {
            throw new CatalogoInexistenteException("El catálogo no ha sido construido.");
        }
        catalogo.exportar(archivo);
    }
}

public class Entrada {
    public static void main(String[] args) {
        // 1. Crear una biblioteca
        Biblioteca biblioteca = new Biblioteca("Biblioteca Central", "Juan Pérez");

        // 2. Construir un catálogo de 4 libros
        biblioteca.construirCatalogo(4);

        // 3. Agregar 5 libros al catálogo (debería fallar)
        try {
            biblioteca.agregarLibro(new LibroTerror("Stephen King", 300, "123456789", 9));
            biblioteca.agregarLibro(new LibroComedia("Terry Pratchett", 250, "234567890", "Absurdo"));
            biblioteca.agregarLibro(new LibroPoliciaca("Agatha Christie", 200, "345678901", "Misterio", List.of("Hercule Poirot", "Miss Marple")));
            biblioteca.agregarLibro(new LibroTerror("H.P. Lovecraft", 350, "456789012", 8));
            biblioteca.agregarLibro(new LibroComedia("Douglas Adams", 280, "567890123", "Surrealista"));
        } catch (CatalogoLlenoException | CatalogoInexistenteException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // 4. Mostrar la información de todos los libros
        System.out.println("\n=== Información de todos los libros ===");
        biblioteca.mostrarCatalogo();

        // 5. Exportar todos los libros del catálogo a un fichero llamado libros.obj
        try {
            biblioteca.exportarCatalogo("libros.obj");
        } catch (CatalogoInexistenteException | IOException e) {
            System.out.println("Error al exportar el catálogo: " + e.getMessage());
        }
    }
}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              