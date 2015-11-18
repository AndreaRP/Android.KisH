package maps.rubio.andrea.kantoishere;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represeta una zona de la BD
 * @author Andrea
 */


public class Zona implements Parcelable{
    private static final String TAG = "Zona";
        private int codigo;
        private double longitud;
        private double lat;
        private int radio = 250;

    /**
     * Constructor por defecto
     */
    public Zona() {
    }

    /**
     * Constructor
     *
     * @param codigo
     * @param longitud
     * @param lat
     * @param radio
     */
    public Zona(int codigo, double longitud, double lat, int radio) {
        this.codigo = codigo;
        this.longitud = longitud;
        this.lat = lat;
        this.radio = radio;
    }

    /**
     * Devuelve el codigo de la zona.
     * @return
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * Establece el codigo de la zona.
     * @param codigo
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    /**
     * Devuelve la longitud de centro de la zona.
     * @return
     */
    public double getLongitud() {
        return longitud;
    }

    /**
     * Establece la longitud del centro de la zona.
     * @param longitud
     */
    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    /**
     * Devuelve la latitud del centro de la zona.
     * @return
     */
    public double getLat() {
        return lat;
    }

    /**
     * Establece la latitud del centro de la zona.
     * @param lat
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * Devuelve el radio de la zona.
     * @return
     */
    public int getRadio() {
        return radio;
    }

    /**
     * Establece el radio de la zona.
     * @param radio
     */
    public void setRadio(int radio) {
        this.radio = radio;
    }

    protected Zona(Parcel in) {
        codigo = in.readInt();
        longitud = in.readDouble();
        lat = in.readDouble();
        radio = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(codigo);
        dest.writeDouble(longitud);
        dest.writeDouble(lat);
        dest.writeInt(radio);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Zona> CREATOR = new Parcelable.Creator<Zona>() {
        @Override
        public Zona createFromParcel(Parcel in) {
            return new Zona(in);
        }

        @Override
        public Zona[] newArray(int size) {
            return new Zona[size];
        }
    };

    /**
     * Comprueba si el punto que se le pasa esta dentro de su area
     * @param lat
     * @param longitud
     * @return
     */
        public boolean estaDentro(double lat, double longitud){
        float[] distance = new float[2];
        Location.distanceBetween(lat, longitud, this.lat, this.longitud, distance);
        if( distance[0] > this.radio){
            //Log.d(TAG, "fuera");
            return false;
        } else {
            //Log.d(TAG, "dentro");
            return true;
        }
    }
}