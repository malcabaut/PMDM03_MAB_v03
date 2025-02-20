package alcaide.bautista.pmdm03_mab_v03.ui.pokedex;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alcaide.bautista.pmdm03_mab_v03.R;
import alcaide.bautista.pmdm03_mab_v03.data.PokemonResponse;

public class PokedexAdapter extends RecyclerView.Adapter<PokedexHolder> {
    private final List<PokemonResponse.Result> pokemonList;
    private final String userId;
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance(); // Instancia compartida de FirebaseFirestore

    public PokedexAdapter(List<PokemonResponse.Result> pokemonList, String userId) {
        this.pokemonList = pokemonList;
        this.userId = userId;
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

        // Agregar un listener para manejar clics en el ítem
        holder.itemView.setOnClickListener(v -> manejarClickPokemon(pokemon, holder.itemView.getContext()));
    }

    @Override
    public int getItemCount() {
        return pokemonList != null ? pokemonList.size() : 0;
    }

    /**
     * Maneja el evento de clic en un Pokémon.
     */
    private void manejarClickPokemon(PokemonResponse.Result pokemon, Context context) {
        if (userId == null || userId.isEmpty()) {
            Log.w("PokedexAdapter", "El userId es nulo o está vacío, no se puede guardar la información del Pokémon.");
            Toast.makeText(context, "Error: Usuario no autenticado.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear un mapa con los datos del Pokémon
        Map<String, Object> datosPokemon = new HashMap<>();
        datosPokemon.put("user_id", userId);
        datosPokemon.put("pokemon_id", pokemon.getId());
        datosPokemon.put("pokemon_name", pokemon.getName()); // Guardar el nombre del Pokémon
        datosPokemon.put("pokemon_imagen_url", pokemon.getImageUrl());

        // Guardar los datos en Firestore
        guardarDatosPokemonEnFirestore(datosPokemon, context);
    }

    /**
     * Guarda los datos del Pokémon en Firestore.
     */
    private void guardarDatosPokemonEnFirestore(Map<String, Object> datosPokemon, Context context) {
        db.collection("pokemon_data")
                .add(datosPokemon)
                .addOnSuccessListener(documentReference -> {
                    Log.d("PokedexAdapter", "Documento añadido con ID: " + documentReference.getId());
                    // Muestra un Toast notificando que el Pokémon ha sido capturado.
                    Toast.makeText(context, context.getString(R.string.pok_mon_capturados), Toast.LENGTH_SHORT).show();

                })
                .addOnFailureListener(e -> {
                    Log.e("PokedexAdapter", "Error al añadir el documento", e);
                    // Muestra un Toast notificando que hubo un error.
                    Toast.makeText(context, context.getString(R.string.no_captured_pokemon), Toast.LENGTH_SHORT).show();
                });
    }

}
