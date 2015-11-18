package maps.rubio.andrea.kantoishere;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragmento que carga la web de Kish
 *
 * @author Andrea
 */
public class FragmentoWeb extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_web, container, false);

    }

    /**
     * Llama al metodo cargar web que se encuentrq el la actividad {@link Principal}.
     * @param view
     * @param savedInstanceState
     */
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Principal principal = (Principal) getActivity();
        principal.cargarWeb();
    }
}
