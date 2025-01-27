package alcaide.bautista.pmdm03_mab_v03.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import alcaide.bautista.pmdm03_mab_v03.LoginActivity;
import alcaide.bautista.pmdm03_mab_v03.PreferencesHelper;
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

        // Observa el evento de eliminación de Pokémon
        settingsViewModel.getDeletePokemonEvent().observe(getViewLifecycleOwner(), event -> {
            if (event != null && event) {
                // Lógica para eliminar Pokémon
                // Aquí podrías añadir la lógica que elimine el Pokémon, o realizar una acción visual.
                settingsViewModel.updateText("Pokemon eliminado");
            }
        });

        // Observa el evento de logout
        settingsViewModel.getLogoutEvent().observe(getViewLifecycleOwner(), event -> {
            if (event != null && event) {
                // Llama al método de logout en el ViewModel para cerrar sesión en Firebase
                settingsViewModel.logoutFromFirebase();
                // Realiza la lógica para redirigir o actualizar la UI tras cerrar sesión
                // Por ejemplo, ir al LoginActivity
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    // Vincula el botón de cambiar idioma
        binding.buttonChangeLanguage.setOnClickListener(v -> {
            // Alterna el idioma actual
            boolean isEnglish = PreferencesHelper.getLanguage(requireContext()).equals("en");
            PreferencesHelper.setLanguage(requireContext(), !isEnglish);

            // Reinicia la actividad para aplicar el cambio de idioma
            requireActivity().recreate();
        });

        // Configura los botones
        binding.buttonDeletePokemon.setOnClickListener(v -> settingsViewModel.deleteAllCapturedPokemon());
        binding.buttonAbout.setOnClickListener(v -> settingsViewModel.onAbout(requireContext()));
        binding.buttonLogout.setOnClickListener(v -> settingsViewModel.onLogout());

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
