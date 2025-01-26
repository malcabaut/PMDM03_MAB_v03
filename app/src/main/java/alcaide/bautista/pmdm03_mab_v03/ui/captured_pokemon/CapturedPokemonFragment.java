package alcaide.bautista.pmdm03_mab_v03.ui.captured_pokemon;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import alcaide.bautista.pmdm03_mab_v03.R;
import alcaide.bautista.pmdm03_mab_v03.data.Pokemon;

public class CapturedPokemonFragment extends Fragment {

    // Definir las variables para el ViewModel y el adaptador.
    private CapturedPokemonViewModel viewModel;
    private CapturedPokemonAdapter adapter;
    private static final String TAG = "CapturedPokemonFragment"; // Etiqueta para los logs

    // Método que infla el layout del fragmento.
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflamos el diseño del fragmento.
        return inflater.inflate(R.layout.fragment_captured_pokemon, container, false);
    }

    // Método que configura las vistas y observa los cambios en el ViewModel.
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtiene el ViewModel y configura la vista.
        viewModel = new ViewModelProvider(requireActivity()).get(CapturedPokemonViewModel.class);
        setupRecyclerView(view); // Configura el RecyclerView
        observeViewModel(); // Comienza a observar el ViewModel para cambios

        // Observa los cambios en tiempo real para la lista de Pokémon capturados.
        viewModel.observeCapturedPokemon();
    }

    // Configura el RecyclerView con un adaptador y sus listeners.
    private void setupRecyclerView(@NonNull View view) {
        // Inicializamos el RecyclerView y lo configuramos con un LinearLayoutManager.
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_captured_pokemon);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializamos el adaptador con una lista vacía.
        adapter = new CapturedPokemonAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter); // Asignamos el adaptador al RecyclerView.

        // Configura el listener para el clic en un elemento de la lista.
        adapter.setOnItemClickListener(pokemon -> {
            if (pokemon.getId() > 0) {
                // Log para verificar el ID del Pokémon.
                Log.d(TAG, "ID del Pokémon capturado: " + pokemon.getId());
                // Preparamos los datos para navegar al detalle del Pokémon.
                Bundle bundle = new Bundle();
                bundle.putInt("pokemonId", pokemon.getId());
                // Navegamos a la pantalla de detalle.
                Navigation.findNavController(view).navigate(R.id.nav_detail, bundle);
            } else {
                // Log de error si el ID del Pokémon es inválido o nulo.
                Log.e(TAG, "ID del Pokémon es inválido o nulo");
                // Mostramos un mensaje de error.
                Toast.makeText(getContext(), "ID del Pokémon inválido", Toast.LENGTH_SHORT).show();
            }
        });

        // Configura el listener para el clic en el botón de eliminar.
        adapter.setOnDeleteClickListener(pokemon -> {
            if (pokemon.getId() > 0) {
                // Log para verificar que el Pokémon se eliminará correctamente.
                Log.d(TAG, "Eliminando Pokémon con ID: " + pokemon.getId());
                // Llamamos al ViewModel para eliminar el Pokémon.
                viewModel.deleteCapturedPokemon(pokemon.getId());
            } else {
                // Log de error si el ID del Pokémon es inválido para eliminar.
                Log.e(TAG, "No se puede eliminar: ID del Pokémon inválido o nulo");
                // Mostramos un mensaje de error.
                Toast.makeText(getContext(), "No se pudo eliminar el Pokémon", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Observa los cambios en el ViewModel para actualizar la lista de Pokémon capturados.
    private void observeViewModel() {
        // Observa los cambios en la lista de Pokémon capturados.
        viewModel.getCapturedPokemonList().observe(getViewLifecycleOwner(), this::updatePokemonList);

        // Observa el estado de carga y muestra un mensaje cuando se está cargando.
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                // Log para indicar que los datos están siendo cargados.
                Log.d(TAG, "Cargando lista de Pokémon...");
                // Mostramos un mensaje de carga.
                Toast.makeText(getContext(), R.string.loading_captured_pokemon, Toast.LENGTH_SHORT).show();
            }
        });

        // Observa los mensajes de error.
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                // Log para indicar que ocurrió un error.
                Log.e(TAG, "Error: " + errorMessage);
                // Mostramos el mensaje de error.
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        // Observa los mensajes de éxito, como después de eliminar un Pokémon.
        viewModel.getSuccessMessage().observe(getViewLifecycleOwner(), successMessage -> {
            if (successMessage != null && !successMessage.isEmpty()) {
                // Log para indicar un éxito en una operación.
                Log.d(TAG, "Éxito: " + successMessage);
                // Mostramos el mensaje de éxito.
                Toast.makeText(getContext(), successMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Actualiza la lista de Pokémon en el RecyclerView.
    private void updatePokemonList(List<Pokemon> capturedPokemonList) {
        // Llama al método del adaptador para actualizar los datos con la nueva lista.
        adapter.updatePokemonList(capturedPokemonList);
    }
}
