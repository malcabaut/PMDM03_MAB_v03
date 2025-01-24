package alcaide.bautista.pmdm03_mab_v03.ui.captured_pokemon;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import alcaide.bautista.pmdm03_mab_v03.R;
import alcaide.bautista.pmdm03_mab_v03.data.Pokemon;

/**
 * ViewHolder que representa cada Pokémon en el RecyclerView.
 */
public class CapturedPokemonViewHolder extends RecyclerView.ViewHolder {

    private final TextView pokemonNameTextView;
    private final ImageView pokemonImageView;

    public CapturedPokemonViewHolder(@NonNull View itemView) {
        super(itemView);
        pokemonNameTextView = itemView.findViewById(R.id.pokemon_name);
        pokemonImageView = itemView.findViewById(R.id.pokemon_image);
    }

    /**
     * Vincula los datos de un Pokémon a las vistas.
     *
     * @param pokemon El Pokémon a mostrar.
     */
    public void bind(Pokemon pokemon) {
        pokemonNameTextView.setText(pokemon.getName());
        Picasso.get()
                .load(pokemon.getImageUrl())
                .placeholder(R.drawable.ic_pokemon_captured) // Imagen por defecto mientras carga.
                .error(R.drawable.error_image)       // Imagen en caso de error.
                .into(pokemonImageView);
    }
}
