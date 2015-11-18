package maps.rubio.andrea.kantoishere;

/**
 * Clase auxiliar para mostrar la lista de bichos. Contiene la referencia a la imagen, la experiencia y el nombre.
 *
 * @author Andrea Rubio
 */

public class BichoItem{
    private int imagen;
    private int experiencia;
    private String nombre;

    /**
     * Constructor vacio
     *
     */
    public BichoItem() {
    }

    /**
     * Constructor
     * @param imagen
     * @param experiencia
     * @param nombre
     */
    public BichoItem(int imagen, int experiencia, String nombre) {
        this.imagen = imagen;
        this.experiencia = experiencia;
        this.nombre = nombre;
    }

    /**
     * Getter del int referencia de la imagen
     * @return
     */

    public int getImagen() {
        return imagen;
    }

    /**
     * Setter de la imagen
     * @param imagen
     */
    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    /**
     * Getter de la experiencia
     * @return
     */
    public int getExperiencia() {
        return experiencia;
    }

    /**
     * Setter de la experiencia
     * @param experiencia
     */
    public void setExperiencia(int experiencia) {
        this.experiencia = experiencia;
    }

    /**
     * Getter del nombre
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Setter del nombre
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
