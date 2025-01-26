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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_captured_pokemon, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(CapturedPokemonViewModel.class);
        setupRecyclerView(view);
        observeViewModel();

        if (isPokemonListEmpty()) {
            viewModel.fetchCapturedPokemon();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.fetchCapturedPokemon();
    }

    private void setupRecyclerView(@NonNull View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_captured_pokemon);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new CapturedPokemonAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(pokemon -> {
            if (pokemon.getId() > 0) {
                Log.d(TAG, "ID del Pokémon capturado: " + pokemon.getId());
                Bundle bundle = new Bundle();
                bundle.putInt("pokemonId", pokemon.getId());
                Navigation.findNavController(view).navigate(R.id.nav_detail, bundle);
            } else {
                Log.e(TAG, "ID del Pokémon es inválido o nulo");
                Toast.makeText(getContext(), "ID del Pokémon inválido", Toast.LENGTH_SHORT).show();
            }
        });

        adapter.setOnDeleteClickListener(pokemon -> {
            if (pokemon.getId() > 0) {
                Log.d(TAG, "Eliminando Pokémon con ID: " + pokemon.getId());

                // Llama al método deleteCapturedPokemon en el ViewModel
                viewModel.deleteCapturedPokemon(pokemon.getId());

                // Elimina el Pokémon de la lista actual en el RecyclerView
                removePokemonFromList(pokemon);

                // Notifica al usuario
                Toast.makeText(getContext(), "Pokémon eliminado", Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "No se puede eliminar: ID del Pokémon inválido o nulo");
                Toast.makeText(getContext(), "No se pudo eliminar el Pokémon", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void observeViewModel() {
        viewModel.getCapturedPokemonList().observe(getViewLifecycleOwner(), this::updatePokemonList);

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                Toast.makeText(getContext(), R.string.loading_captured_pokemon, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updatePokemonList(List<Pokemon> capturedPokemonList) {
        adapter.updatePokemonList(capturedPokemonList);
    }

    private boolean isPokemonListEmpty() {
        List<Pokemon> currentList = viewModel.getCapturedPokemonList().getValue();
        return currentList == null || currentList.isEmpty();
    }

    private void removePokemonFromList(Pokemon pokemon) {
        List<Pokemon> currentList = viewModel.getCapturedPokemonList().getValue();
        if (currentList != null) {
            currentList.remove(pokemon);
            updatePokemonList(currentList);
            viewModel.deleteCapturedPokemon(pokemon.getId());
        }
    }
}
