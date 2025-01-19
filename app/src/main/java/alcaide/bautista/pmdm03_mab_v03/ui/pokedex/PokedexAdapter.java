package alcaide.bautista.pmdm03_mab_v03.ui.pokedex;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import alcaide.bautista.pmdm03_mab_v03.R;
import alcaide.bautista.pmdm03_mab_v03.data.PokemonResponse;

import java.util.List;

public class PokedexAdapter extends RecyclerView.Adapter<PokedexHolder> {
    private final List<PokemonResponse.Result> pokemonList;

    public PokedexAdapter(List<PokemonResponse.Result> pokemonList) {
        this.pokemonList = pokemonList;
    }

    @NonNull
    @Override
    public PokedexHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pokemon, parent, false);
        return new PokedexHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokedexHolder holder, int position) {
        PokemonResponse.Result pokemon = pokemonList.get(position);
        holder.bind(pokemon);
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }
}