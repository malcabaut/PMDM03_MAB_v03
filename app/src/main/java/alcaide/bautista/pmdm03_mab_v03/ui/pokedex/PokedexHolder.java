package alcaide.bautista.pmdm03_mab_v03.ui.pokedex;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import alcaide.bautista.pmdm03_mab_v03.R;
import alcaide.bautista.pmdm03_mab_v03.data.PokemonResponse;

public class PokedexHolder extends RecyclerView.ViewHolder {
    private final TextView pokemonNameTextView;

    public PokedexHolder(@NonNull View itemView) {
        super(itemView);
        pokemonNameTextView = itemView.findViewById(R.id.pokemon_name);
    }

    public void bind(PokemonResponse.Result pokemon) {
        pokemonNameTextView.setText(pokemon.getName());
    }
}
