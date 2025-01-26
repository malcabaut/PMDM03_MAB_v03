package alcaide.bautista.pmdm03_mab_v03.ui.captured_pokemon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import alcaide.bautista.pmdm03_mab_v03.R;
import alcaide.bautista.pmdm03_mab_v03.data.Pokemon;

/**
 * Adaptador para gestionar y mostrar una lista de Pokémon capturados en un RecyclerView.
 */
public class CapturedPokemonAdapter extends RecyclerView.Adapter<CapturedPokemonAdapter.CapturedPokemonViewHolder> {

    // Lista de Pokémon capturados.
    private List<Pokemon> capturedPokemonList;

    // Listeners para manejar eventos de clic en los elementos y en el botón de eliminar.
    private OnItemClickListener onItemClickListener;
    private OnDeleteClickListener onDeleteClickListener;

    /**
     * Interfaz para manejar clics en los elementos de la lista.
     */
    public interface OnItemClickListener {
        void onItemClick(Pokemon pokemon);
    }

    /**
     * Interfaz para manejar clics en el botón de eliminar.
     */
    public interface OnDeleteClickListener {
        void onDeleteClick(Pokemon pokemon);
    }

    /**
     * Constructor del adaptador.
     *
     * @param capturedPokemonList Lista inicial de Pokémon capturados.
     */
    public CapturedPokemonAdapter(List<Pokemon> capturedPokemonList) {
        // Si la lista es nula, inicializamos con una lista vacía para evitar errores.
        this.capturedPokemonList = capturedPokemonList != null ? capturedPokemonList : List.of();
    }

    /**
     * Configura un listener para manejar clics en los elementos.
     *
     * @param listener Listener para clics en los elementos.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    /**
     * Configura un listener para manejar clics en el botón de eliminar.
     *
     * @param listener Listener para clics en el botón de eliminar.
     */
    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    @NonNull
    @Override
    public CapturedPokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el diseño del elemento individual desde XML.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_captured_pokemon, parent, false);
        return new CapturedPokemonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CapturedPokemonViewHolder holder, int position) {
        // Obtiene el Pokémon de la lista en la posición actual.
        Pokemon pokemon = capturedPokemonList.get(position);

        // Vincula los datos del Pokémon al ViewHolder.
        holder.bind(pokemon);

        // Configura el listener para clic en el elemento completo.
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(pokemon);
            }
        });

        // Configura el listener para clic en el botón de eliminar.
        holder.deleteIcon.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(pokemon);
            }
        });
    }

    @Override
    public int getItemCount() {
        // Devuelve el tamaño de la lista de Pokémon.
        return capturedPokemonList.size();
    }

    /**
     * Actualiza la lista de Pokémon y notifica al adaptador para refrescar la vista.
     *
     * @param newPokemonList Nueva lista de Pokémon capturados.
     */
    public void updatePokemonList(List<Pokemon> newPokemonList) {
        this.capturedPokemonList = newPokemonList != null ? newPokemonList : List.of();
        notifyDataSetChanged(); // Notifica que los datos han cambiado.
    }

    /**
     * ViewHolder para gestionar las vistas de cada elemento en el RecyclerView.
     */
    static class CapturedPokemonViewHolder extends RecyclerView.ViewHolder {

        private final TextView pokemonName;  // Texto para el nombre del Pokémon.
        private final ImageView pokemonImage;  // Imagen del Pokémon.
        private final ImageView deleteIcon;  // Icono para eliminar el Pokémon.

        public CapturedPokemonViewHolder(@NonNull View itemView) {
            super(itemView);

            // Asociamos las vistas del diseño.
            pokemonName = itemView.findViewById(R.id.pokemon_name);
            pokemonImage = itemView.findViewById(R.id.pokemon_image);
            deleteIcon = itemView.findViewById(R.id.delete_icon);
        }

        /**
         * Vincula los datos del Pokémon a las vistas.
         *
         * @param pokemon Objeto Pokémon con los datos a mostrar.
         */
        public void bind(Pokemon pokemon) {
            // Configura el nombre del Pokémon.
            pokemonName.setText(pokemon.getName());

            // Carga la imagen del Pokémon usando Picasso.
            Picasso.get()
                    .load(pokemon.getImageUrl()) // Obtiene la URL de la imagen desde el objeto Pokémon.
                    .placeholder(R.drawable.placeholder_image) // Imagen de marcador de posición mientras se carga.
                    .error(R.drawable.ic_error_drawable) // Imagen en caso de error al cargar.
                    .into(pokemonImage); // Establece la imagen en el ImageView.
        }
    }
}
