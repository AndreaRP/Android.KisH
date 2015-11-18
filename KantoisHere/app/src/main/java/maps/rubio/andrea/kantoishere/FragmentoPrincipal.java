package maps.rubio.andrea.kantoishere;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragmento que se carga por defecto. Contiene el boton que inicia {@link MapsActivity}.
 *
 * @author Andrea
 */
public class FragmentoPrincipal extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_frag_principal, container, false);
    }
}
