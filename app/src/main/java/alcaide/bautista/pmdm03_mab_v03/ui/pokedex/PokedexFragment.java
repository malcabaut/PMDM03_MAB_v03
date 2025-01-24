package alcaide.bautista.pmdm03_mab_v03.ui.pokedex;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import alcaide.bautista.pmdm03_mab_v03.R;

public class PokedexFragment extends Fragment {
    private PokedexViewModel pokedexViewModel;
    private RecyclerView recyclerView;
    private PokedexAdapter adapter;

    private String userId; // Almacenar el userId

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pokedex, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtener el userId de la actividad que contiene el fragmento
        userId = getActivity().getIntent().getStringExtra("user_id");

        recyclerView = view.findViewById(R.id.recycler_view_pokedex);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        pokedexViewModel = new ViewModelProvider(this).get(PokedexViewModel.class);

        pokedexViewModel.getPokemonList().observe(getViewLifecycleOwner(), pokemonResponse -> {
            if (pokemonResponse != null) {
                // Asegurarse de pasar el userId al adaptador
                adapter = new PokedexAdapter(pokemonResponse.getResults(), userId);
                recyclerView.setAdapter(adapter);
            }
        });

        pokedexViewModel.fetchPokemonList(0, 150);
    }
}
