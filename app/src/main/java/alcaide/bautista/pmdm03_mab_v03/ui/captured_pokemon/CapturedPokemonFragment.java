package alcaide.bautista.pmdm03_mab_v03.ui.captured_pokemon;

import android.os.Bundle;
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el diseño del fragmento
        return inflater.inflate(R.layout.fragment_captured_pokemon, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar el ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(CapturedPokemonViewModel.class);

        // Configurar RecyclerView
        setupRecyclerView(view);

        // Observar los cambios en los datos
        observeViewModel();

        // Cargar datos iniciales si es necesario
        if (isPokemonListEmpty()) {
            viewModel.fetchCapturedPokemon();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Actualizar la lista al recuperar el fragmento
        viewModel.fetchCapturedPokemon();
    }

    private void setupRecyclerView(@NonNull View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_captured_pokemon);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializar el adaptador con una lista vacía
        adapter = new CapturedPokemonAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Configurar el listener para los clics en los elementos
        adapter.setOnItemClickListener(pokemon -> {
            // Navegar al detalle del Pokémon
            Bundle bundle = new Bundle();
            bundle.putInt("pokemonId", pokemon.getId()); // Asegúrate de que Pokémon tiene un ID válido
            Navigation.findNavController(view).navigate(R.id.nav_settings, bundle);
        });
    }

    private void observeViewModel() {
        // Observar cambios en la lista de Pokémon capturados
        viewModel.getCapturedPokemonList().observe(getViewLifecycleOwner(), this::updatePokemonList);

        // Observar el estado de carga
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                Toast.makeText(getContext(), R.string.loading_captured_pokemon, Toast.LENGTH_SHORT).show();
            }
        });

        // Observar mensajes de error
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
}
