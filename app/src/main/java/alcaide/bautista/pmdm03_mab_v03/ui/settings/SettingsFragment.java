package alcaide.bautista.pmdm03_mab_v03.ui.settings;

// Importaciones necesarias para el funcionamiento del fragmento.
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import alcaide.bautista.pmdm03_mab_v03.databinding.FragmentSettingsBinding;

/**
 * Fragmento que gestiona la interfaz de usuario de la configuración de la aplicación.
 * Utiliza Data Binding para interactuar con el diseño XML y un ViewModel para manejar la lógica de datos.
 */
public class SettingsFragment extends Fragment {

    // Enlace (binding) que conecta las vistas definidas en el XML con el código.
    private FragmentSettingsBinding binding;

    /**
     * Método que se llama para inicializar la vista del fragmento.
     *
     * @param inflater  Objeto que infla el diseño del fragmento.
     * @param container Contenedor padre en el que se insertará la vista.
     * @param savedInstanceState Estado previamente guardado del fragmento, si existe.
     * @return La vista inflada del fragmento.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Se inicializa el ViewModel asociado a este fragmento.
        SettingsViewModel settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        // Se inicializa el objeto de enlace con el archivo de diseño correspondiente.
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        // Se obtiene la raíz de la vista generada por el enlace.
        View root = binding.getRoot();

        // Vincula el TextView del diseño con los datos proporcionados por el ViewModel.
        final TextView textView = binding.textSettings;
        settingsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Devuelve la vista raíz para que se muestre en la pantalla.
        return root;
    }

    /**
     * Método llamado cuando la vista del fragmento se destruye.
     * Libera el objeto de enlace para evitar fugas de memoria.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
