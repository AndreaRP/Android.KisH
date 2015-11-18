package maps.rubio.andrea.kantoishere;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Clase parcelable que representa un punto activo de la BD
 */
public class Punto implements Parcelable {
    int codigo;
    int zonaCodigo;
    double lat;
    double lon;

    /**
     * Constructor por defecto
     */
    public Punto() {
    }

    /**
     * Constructor
     *
     * @param codigo
     * @param zonaCodigo
     * @param lat
     * @param lon
     */
    public Punto(int codigo, int zonaCodigo, double lat, double lon) {
        this.codigo = codigo;
        this.zonaCodigo = zonaCodigo;
        this.lat = lat;
        this.lon = lon;
    }

    /**
     * Devuelve el codigo del punto
     * @return
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * Establece el codigo del punto
     * @param codigo
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    /**
     * Devuelve el codigo de la zona.
     * @return
     */
    public int getZonaCodigo() {
        return zonaCodigo;
    }

    /**
     * Establece el codigo de la zona
     * @param zonaCodigo
     */
    public void setZonaCodigo(int zonaCodigo) {
        this.zonaCodigo = zonaCodigo;
    }

    /**
     * Devuelve la latitud del punto
     * @return
     */
    public double getLat() {
        return lat;
    }

    /**
     * Establece la latitud del punto.
     * @param lat
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * Devuelve la longitud del punto.
     * @return
     */
    public double getLon() {
        return lon;
    }

    /**
     * Establece la longitud del punto.
     * @param lon
     */
    public void setLon(double lon) {
        this.lon = lon;
    }

    protected Punto(Parcel in) {
        codigo = in.readInt();
        zonaCodigo = in.readInt();
        lat = in.readDouble();
        lon = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(codigo);
        dest.writeInt(zonaCodigo);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Punto> CREATOR = new Parcelable.Creator<Punto>() {
        @Override
        public Punto createFromParcel(Parcel in) {
            return new Punto(in);
        }

        @Override
        public Punto[] newArray(int size) {
            return new Punto[size];
        }
    };

    /**
     * Establece si una coordenada coincide con este punto
     * @param lat
     * @param longitud
     * @return
     */
    public boolean enPunto(double lat, double longitud){
        float[] distance = new float[2];
        Location.distanceBetween(lat, longitud, this.lat, this.lon, distance);
        if( distance[0] > 20){
            //Log.d(TAG, "fuera");
            return false;
        } else {
            //Log.d(TAG, "dentro");
            return true;
        }
    }
}
