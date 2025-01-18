package alcaide.bautista.pmdm03_mab_v03.ui.pokedex;

// Importación de clases necesarias para la funcionalidad del fragmento.
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import alcaide.bautista.pmdm03_mab_v03.R;

/**
 * Fragmento que representa la interfaz de usuario de la Pokédex.
 * Utiliza un ViewModel para manejar la lógica de datos y las actualizaciones del UI.
 */
public class PokedexFragment extends Fragment {

    // ViewModel asociado a este fragmento para manejar los datos relacionados con la Pokédex.
    private PokedexViewModel viewModel;

    /**
     * Método que se llama para inicializar la vista del fragmento.
     *
     * @param inflater  Objeto que infla el diseño del fragmento.
     * @param container Contenedor padre en el que se insertará la vista.
     * @param savedInstanceState Estado previamente guardado del fragmento, si existe.
     * @return La vista inflada del fragmento.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Se inicializa el ViewModel para el ciclo de vida de este fragmento.
        viewModel = new ViewModelProvider(this).get(PokedexViewModel.class);

        // Se infla el diseño del fragmento desde el archivo XML correspondiente.
        View view = inflater.inflate(R.layout.fragment_pokedex, container, false);

        // Observador del ViewModel: escucha cambios en los datos y actualiza la interfaz de usuario.
        viewModel.getText().observe(getViewLifecycleOwner(), text -> {
            // Aquí se manejarán las actualizaciones de la UI cuando los datos cambien.
            // Ejemplo: actualizar un TextView con el texto observado.
        });

        // Se devuelve la vista inflada para que se muestre en la pantalla.
        return view;
    }
}
