package maps.rubio.andrea.kantoishere;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Fragment que se carga en la actividad {@link Principal} cuando se elige "ver equipo".
 * @author Andrea
 */
public class FragmentoLista extends Fragment {


    public FragmentoLista() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista, container, false);
    }

    /**
     * Llama al metodo que carga la lista que se encuentra en la actividad {@link Principal}.
     *
     * @param view
     * @param savedInstanceState
     */
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Principal principal = (Principal) getActivity();
        principal.cargarListaBichos();
    }
}
