package alcaide.bautista.pmdm03_mab_v03.ui.captured_pokemon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import alcaide.bautista.pmdm03_mab_v03.R;
import alcaide.bautista.pmdm03_mab_v03.data.Pokemon;


/**
 * Adaptador para mostrar la lista de Pokémon capturados en un RecyclerView.
 */
public class CapturedPokemonAdapter extends RecyclerView.Adapter<CapturedPokemonViewHolder> {

    private List<Pokemon> capturedPokemonList;
    private OnItemClickListener onItemClickListener;

    /**
     * Interfaz para manejar clics en los ítems.
     */
    public interface OnItemClickListener {
        void onItemClick(Pokemon pokemon);
    }

    /**
     * Constructor del adaptador.
     *
     * @param capturedPokemonList Lista inicial de Pokémon capturados.
     */
    public CapturedPokemonAdapter(List<Pokemon> capturedPokemonList) {
        this.capturedPokemonList = capturedPokemonList != null ? capturedPokemonList : List.of();
    }

    /**
     * Configura un listener para clics en los ítems.
     *
     * @param listener Listener de clics.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public CapturedPokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el diseño del elemento individual.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_captured_pokemon, parent, false);
        return new CapturedPokemonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CapturedPokemonViewHolder holder, int position) {
        // Asigna los datos del Pokémon al ViewHolder.
        Pokemon pokemon = capturedPokemonList.get(position);
        holder.bind(pokemon);

        // Configura el listener para el clic.
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(pokemon);
            }
        });
    }

    @Override
    public int getItemCount() {
        return capturedPokemonList.size();
    }

    /**
     * Actualiza la lista de Pokémon y notifica al adaptador.
     *
     * @param newPokemonList Nueva lista de Pokémon capturados.
     */
    public void updatePokemonList(List<Pokemon> newPokemonList) {
        this.capturedPokemonList = newPokemonList != null ? newPokemonList : List.of();
        notifyDataSetChanged();
    }
}
