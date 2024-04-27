                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. Crear una biblioteca
        Biblioteca biblioteca = new Biblioteca("Biblioteca Central", "Miguel Angel Gumiel Urosa");

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
        System.out.println("Información de todos los libros:");
        biblioteca.mostrarCatalogo();

        // 5. Exportar todos los libros del catálogo a un fichero llamado libros.obj
        try {
            biblioteca.exportarCatalogo("libros.obj");
        } catch (CatalogoInexistenteException | IOException e) {
            System.out.println("Error al exportar el catálogo: " + e.getMessage());
        }
    }
}
