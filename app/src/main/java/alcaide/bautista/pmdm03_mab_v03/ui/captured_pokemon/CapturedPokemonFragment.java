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

    private CapturedPokemonViewModel viewModel;
    private CapturedPokemonAdapter adapter;
    private static final String TAG = "CapturedPokemonFragment";
    private static final String KEY_POKEMON_ID = "pokemonId"; // Clave constante para navegación.

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_captured_pokemon, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViewModel();
        setupRecyclerView(view);
        observeViewModel();
    }

    /**
     * Inicializa el ViewModel del fragmento.
     */
    private void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(CapturedPokemonViewModel.class);
    }

    /**
     * Configura el RecyclerView y sus adaptadores.
     */
    private void setupRecyclerView(@NonNull View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_captured_pokemon);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new CapturedPokemonAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        setupAdapterListeners(view);
    }

    /**
     * Configura los listeners para los elementos del adaptador.
     */
    private void setupAdapterListeners(@NonNull View view) {
        // Listener para clic en un Pokémon.
        adapter.setOnItemClickListener(pokemon -> navigateToPokemonDetail(view, pokemon));

        // Listener para clic en el botón de eliminar.
        adapter.setOnDeleteClickListener(this::deletePokemon);
    }

    /**
     * Observa los cambios en el ViewModel.
     */
    private void observeViewModel() {
        viewModel.getCapturedPokemonList().observe(getViewLifecycleOwner(), this::updatePokemonList);
        viewModel.observeCapturedPokemon();
    }

    /**
     * Navega al detalle del Pokémon seleccionado.
     */
    private void navigateToPokemonDetail(@NonNull View view, @NonNull Pokemon pokemon) {
        if (pokemon.getId() > 0) {
            Log.d(TAG, "ID del Pokémon capturado: " + pokemon.getId());
            Bundle bundle = new Bundle();
            bundle.putInt(KEY_POKEMON_ID, pokemon.getId());
            Navigation.findNavController(view).navigate(R.id.nav_detail, bundle);
        } else {
            showErrorMessage("ID del Pokémon inválido");
            Log.e(TAG, "ID del Pokémon es inválido o nulo");
        }
    }

    /**
     * Elimina el Pokémon seleccionado.
     */
    private void deletePokemon(@NonNull Pokemon pokemon) {
        if (pokemon.getId() > 0) {
            Log.d(TAG, "Eliminando Pokémon con ID: " + pokemon.getId());
            viewModel.deleteCapturedPokemon(pokemon.getId());
        } else {
            showErrorMessage("No se pudo eliminar el Pokémon");
            Log.e(TAG, "No se puede eliminar: ID del Pokémon inválido o nulo");
        }
    }

    /**
     * Actualiza la lista de Pokémon en el adaptador.
     */
    private void updatePokemonList(@NonNull List<Pokemon> capturedPokemonList) {
        adapter.updatePokemonList(capturedPokemonList);
    }

    /**
     * Muestra un mensaje de error como un Toast.
     */
    private void showErrorMessage(@NonNull String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
