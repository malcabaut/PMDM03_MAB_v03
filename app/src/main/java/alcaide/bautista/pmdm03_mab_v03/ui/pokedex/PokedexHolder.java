package alcaide.bautista.pmdm03_mab_v03.ui.pokedex;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import alcaide.bautista.pmdm03_mab_v03.R;
import alcaide.bautista.pmdm03_mab_v03.data.PokemonResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PokedexHolder extends RecyclerView.ViewHolder {
    private final TextView pokemonNameTextView;
    private final TextView pokemonIdTextView;
    private final ImageView pokemonImageView;

    public PokedexHolder(@NonNull View itemView) {
        super(itemView);
        pokemonNameTextView = itemView.findViewById(R.id.pokemon_name);
        pokemonIdTextView = itemView.findViewById(R.id.pokemon_id);
        pokemonImageView = itemView.findViewById(R.id.pokemon_image);
    }

    public void bind(PokemonResponse.Result pokemon) {
        // Establece el nombre del Pokémon
        pokemonNameTextView.setText(pokemon.getName());

        // Deriva el ID del Pokémon desde su URL (por ejemplo: "https://pokeapi.co/api/v2/pokemon/25/")
        String[] urlParts = pokemon.getUrl().split("/");
        String pokemonId = urlParts[urlParts.length - 1];
        pokemonIdTextView.setText("#" + pokemonId);

        // Construye la URL de la imagen del Pokémon y la carga con Picasso
        String imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + pokemonId + ".png";
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.pokebola_transformadose) // Imagen durante la carga
                .error(R.drawable.ic_error_drawable) // Imagen en caso de error
                .into(pokemonImageView);
    }
}
