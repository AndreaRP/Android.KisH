package maps.rubio.andrea.kantoishere;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Clase parcelable que representa a un bicho
 * @author Andrea Rubio
 */
public class Bicho implements Parcelable {
    private int pk;
    private int nPokemon;
    private String nombreGenerico;
    private String zonaCapturado;
    private String nombre;
    private int felicidad;
    private int experiencia;
    private int hambre;

    /**
     * Constructor vacio
     *
     * @return Bicho.
     * @author Andrea Rubio
     */

    public Bicho() {
    }

    /**
     * Constructor
     * @author Andrea Rubio
     * @param nPokemon Numero del pokemon
     * @return Bicho.
     */
    public Bicho(int nPokemon) {
        this.nPokemon = nPokemon;
    }

    /**
     * Constructor
     * @author Andrea Rubio
     * @param nPokemon Numero del pokemon
     * @param nombreGenerico Nombre generico del pokemon
     * @return Bicho.
     */
    public Bicho(int nPokemon, String nombreGenerico) {
        this.nPokemon = nPokemon;
        this.nombreGenerico = nombreGenerico;
    }

    /**
     * Constructor
     * @author Andrea Rubio
     * @param nPokemon Numero del pokemon
     * @param nombreGenerico Nombre generico del pokemon
     * @param zonaCapturado nombre de la zona en la que fue capturado.
     * @param nombre nombre que le ha dado el usuario.
     * @param felicidad Puntos de felicidad.
     * @param experiencia Puntos de experiencia.
     * @param hambre Puntos de hambre.
     * @return Bicho.
     */
    public Bicho(int nPokemon, String nombreGenerico, String zonaCapturado, String nombre, int felicidad, int experiencia, int hambre) {
        this.nPokemon = nPokemon;
        this.nombreGenerico = nombreGenerico;
        this.zonaCapturado = zonaCapturado;
        this.nombre = nombre;
        this.felicidad = felicidad;
        this.experiencia = experiencia;
        this.hambre = hambre;
    }

    /**
     * Recibe un parcel y lo convierte en Bicho
     * @param in
     */
    protected Bicho(Parcel in) {
        pk = in.readInt();
        nPokemon = in.readInt();
        nombreGenerico = in.readString();
        zonaCapturado = in.readString();
        nombre = in.readString();
        felicidad = in.readInt();
        experiencia = in.readInt();
        hambre = in.readInt();
    }

    /**
     * Getter de la Pk
     * @return pk
     */

    public int getPk() {
        return pk;
    }

    /**
     * Setter de la pk
     * @param pk
     */

    public void setPk(int pk) {
        this.pk = pk;
    }

    /**
     * Getter del numero de pokemon.
     * @return nPokemon.
     */
    public int getnPokemon() {
        return nPokemon;
    }

    /**
     * Setter del numero de pokemon
     * @param nPokemon
     */

    public void setnPokemon(int nPokemon) {
        this.nPokemon = nPokemon;
    }

    /**
     * Getter de nombre generico
     * @return
     */

    public String getNombreGenerico() {
        return nombreGenerico;
    }

    /**
     * Setter del nombre generico
     * @param nombreGenerico
     */

    public void setNombreGenerico(String nombreGenerico) {
        this.nombreGenerico = nombreGenerico;
    }

    /**
     * Getter de la zona de captura
     * @return
     */

    public String getZonaCapturado() {
        return zonaCapturado;
    }

    /**
     * Setter de la zona de captura
     * @param zonaCapturado
     */
    public void setZonaCapturado(String zonaCapturado) {
        this.zonaCapturado = zonaCapturado;
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

    /**
     * Getter de la felicidad
     * @return
     */

    public int getFelicidad() {
        return felicidad;
    }

    /**
     * Setter de la felicidad
     * @param felicidad
     */
    public void setFelicidad(int felicidad) {
        this.felicidad = felicidad;
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
     * Getter del hambre
     * @return
     */

    public int getHambre() {
        return hambre;
    }

    /**
     * Setter del hambre
     * @param hambre
     */
    public void setHambre(int hambre) {
        this.hambre = hambre;
    }

    /**
     * Describe los contenidos del parcel
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Convierte a parcel
     * @param dest
     * @param flags
     */

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pk);
        dest.writeInt(nPokemon);
        dest.writeString(nombreGenerico);
        dest.writeString(zonaCapturado);
        dest.writeString(nombre);
        dest.writeInt(felicidad);
        dest.writeInt(experiencia);
        dest.writeInt(hambre);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Bicho> CREATOR = new Parcelable.Creator<Bicho>() {
        @Override
        public Bicho createFromParcel(Parcel in) {
            return new Bicho(in);
        }

        @Override
        public Bicho[] newArray(int size) {
            return new Bicho[size];
        }
    };
}