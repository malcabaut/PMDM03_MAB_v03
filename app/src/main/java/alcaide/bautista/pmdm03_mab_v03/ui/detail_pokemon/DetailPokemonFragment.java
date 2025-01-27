package alcaide.bautista.pmdm03_mab_v03.ui.detail_pokemon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import alcaide.bautista.pmdm03_mab_v03.R;
import alcaide.bautista.pmdm03_mab_v03.data.Pokemon;
import alcaide.bautista.pmdm03_mab_v03.data.PokemonApiService;
import alcaide.bautista.pmdm03_mab_v03.data.RetrofitClient;
import alcaide.bautista.pmdm03_mab_v03.databinding.FragmentDetailPokemonBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragmento que muestra los detalles de un Pokémon seleccionado.
 * Se comunica con un servicio remoto para obtener los datos y los muestra
 * en una interfaz de usuario vinculada mediante View Binding.
 */
public class DetailPokemonFragment extends Fragment {

    private FragmentDetailPokemonBinding binding; // Variable para manejar View Binding

    /**
     * Constructor del fragmento.
     * Establece el layout asociado al fragmento.
     */
    public DetailPokemonFragment() {
        super(R.layout.fragment_detail_pokemon);
    }

    /**
     * Infla la vista del fragmento utilizando View Binding.
     *
     * @param inflater           Objeto LayoutInflater para inflar vistas.
     * @param container          Contenedor padre donde se incluirá la vista.
     * @param savedInstanceState Estado guardado previamente.
     * @return La vista inflada del fragmento.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetailPokemonBinding.inflate(inflater, container, false);
        return binding.getRoot(); // Devuelve la raíz de la vista vinculada
    }

    /**
     * Se llama después de que la vista ha sido creada.
     * Aquí se configuran los elementos de la interfaz y se cargan los datos.
     *
     * @param view               La vista creada.
     * @param savedInstanceState Estado guardado previamente.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ocultar el BottomNavigationView
        toggleBottomNavigationView(View.GONE);

        // Obtener el ID del Pokémon desde el Bundle de argumentos
        Bundle args = getArguments();
        if (args != null) {
            int pokemonId = args.getInt("pokemonId", -1); // Valor predeterminado: -1
            if (pokemonId != -1) {
                fetchPokemonDetails(pokemonId); // Llamar a la API para obtener detalles
            } else {
                showError(); // Mostrar error si no hay ID válido
            }
        }
    }

    /**
     * Realiza una llamada a la API para obtener los detalles del Pokémon.
     *
     * @param pokemonId ID del Pokémon.
     */
    private void fetchPokemonDetails(int pokemonId) {
        PokemonApiService service = RetrofitClient.getInstance().create(PokemonApiService.class);
        service.getPokemonDetails(String.valueOf(pokemonId)).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Pokemon> call, @NonNull Response<Pokemon> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Pokemon pokemon = response.body();
                    // Cargar detalles del Pokémon en la interfaz
                    loadPokemonDetails(
                            pokemon.getId(),
                            pokemon.getName(),
                            pokemon.getImageUrl(),
                            pokemon.getTypes().get(0).getType().getName(),
                            pokemon.getTypes().size() > 1 ? pokemon.getTypes().get(1).getType().getName() : null,
                            pokemon.getWeight() / 10.0,
                            pokemon.getHeight() / 10.0
                    );
                } else {
                    showError(); // Mostrar error si no hay respuesta válida
                }
            }

            @Override
            public void onFailure(@NonNull Call<Pokemon> call, @NonNull Throwable t) {
                showError(); // Mostrar error en caso de fallo
            }
        });
    }

    /**
     * Carga los detalles del Pokémon en la interfaz de usuario.
     *
     * @param index         Índice del Pokémon en la Pokédex.
     * @param name          Nombre del Pokémon.
     * @param imageUrl      URL de la imagen del Pokémon.
     * @param primaryType   Tipo principal del Pokémon.
     * @param secondaryType Tipo secundario (si existe).
     * @param pokemonWeight Peso del Pokémon en kilogramos.
     * @param pokemonHeight Altura del Pokémon en metros.
     */
    private void loadPokemonDetails(int index, String name, String imageUrl, String primaryType, String secondaryType, double pokemonWeight, double pokemonHeight) {
        binding.pokedexIndex.setText(getString(R.string.pokedex_index_format, index)); // Mostrar el índice con formato
        binding.pokemonName.setText(name); // Mostrar el nombre del Pokémon

        // Cargar la imagen del Pokémon utilizando Picasso
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.ic_error_drawable)
                .into(binding.pokemonImage);

        // Configurar los tipos
        setType(binding.type1, primaryType);
        setType(binding.type2, secondaryType);

        // Mostrar peso y altura con el formato adecuado
        binding.weight.setText(getString(R.string.weight_label, pokemonWeight));
        binding.height.setText(getString(R.string.height_label, pokemonHeight));
    }

    /**
     * Establece el tipo de Pokémon en un TextView.
     * Si el tipo es nulo, oculta el TextView.
     *
     * @param typeTextView TextView donde se mostrará el tipo.
     * @param type         Tipo de Pokémon (puede ser nulo).
     */
    private void setType(TextView typeTextView, String type) {
        if (type != null) {
            typeTextView.setText(type); // Mostrar el tipo
            typeTextView.setVisibility(View.VISIBLE); // Hacer visible el TextView
        } else {
            typeTextView.setVisibility(View.GONE); // Ocultar el TextView si no hay tipo
        }
    }

    /**
     * Muestra un mensaje de error genérico en la interfaz.
     */
    private void showError() {
        binding.pokemonName.setText(R.string.msg_error_generic); // Mensaje de error en el TextView del nombre
    }

    /**
     * Cambia la visibilidad del BottomNavigationView.
     *
     * @param visibility Visibilidad deseada (View.VISIBLE, View.GONE, etc.).
     */
    private void toggleBottomNavigationView(int visibility) {
        View bottomNavigationView = requireActivity().findViewById(R.id.nav_view);
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(visibility); // Cambiar visibilidad
        }
    }

    /**
     * Se llama al destruir la vista.
     * Limpia la referencia de binding para evitar fugas de memoria.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Liberar referencia
        toggleBottomNavigationView(View.VISIBLE); // Restaurar la visibilidad del BottomNavigationView
    }
}
