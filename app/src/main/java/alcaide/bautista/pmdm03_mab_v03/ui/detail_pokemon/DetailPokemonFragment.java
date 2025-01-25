package alcaide.bautista.pmdm03_mab_v03.ui.detail_pokemon;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import alcaide.bautista.pmdm03_mab_v03.R;
import alcaide.bautista.pmdm03_mab_v03.data.Pokemon;
import alcaide.bautista.pmdm03_mab_v03.data.PokemonApiService;
import alcaide.bautista.pmdm03_mab_v03.data.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPokemonFragment extends Fragment {


    // Vistas necesarias para mostrar los detalles del Pokémon
    private TextView pokedexIndex, pokemonName, type1, type2, weight, height;
    private ImageView pokemonImage;
    private LinearLayout typesContainer;

    // Constructor sin cambios
    public DetailPokemonFragment() {
        super(R.layout.fragment_detail_pokemon); // Asegúrate de que el nombre del XML es correcto
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ocultar el BottomNavigationView
        View bottomNavigationView = requireActivity().findViewById(R.id.nav_view);
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.GONE);
        }

        // Vincular vistas con sus IDs desde el layout XML
        pokedexIndex = view.findViewById(R.id.pokedex_index);
        pokemonImage = view.findViewById(R.id.pokemon_image);
        pokemonName = view.findViewById(R.id.pokemon_name);
        type1 = view.findViewById(R.id.type_1);
        type2 = view.findViewById(R.id.type_2);
        weight = view.findViewById(R.id.weight);
        height = view.findViewById(R.id.height);
        typesContainer = view.findViewById(R.id.types_container);

        // Obtener el Pokémon ID desde el Bundle
        Bundle args = getArguments();
        if (args != null) {
            int pokemonId = args.getInt("pokemonId", -1);  // Recuperar el ID, -1 es el valor por defecto

            if (pokemonId != -1) {
                Log.d("DetailPokemonFragment", "ID recibido: " + pokemonId);
                fetchPokemonDetails(pokemonId);  // Llamar a la función para obtener los detalles
            } else {
                Log.e("DetailPokemonFragment", "No se recibió el ID del Pokémon");
                pokemonName.setText(R.string.msg_error_generic);  // Mostrar mensaje de error si no se recibe el ID
            }
        }
    }

    // Método para obtener los detalles del Pokémon usando Retrofit
    private void fetchPokemonDetails(int pokemonId) {
        // Log para verificar el ID recibido
        Log.d("DetailPokemonFragment", "Iniciando fetch de detalles para el Pokémon con ID: " + pokemonId);

        // Crear el servicio de Retrofit
        PokemonApiService service = RetrofitClient.getInstance().create(PokemonApiService.class);

        // Llamar al endpoint para obtener los detalles del Pokémon
        Call<Pokemon> call = service.getPokemonDetails(String.valueOf(pokemonId));  // Usar el ID del Pokémon
        Log.d("DetailPokemonFragment", "Llamada a Retrofit creada con el endpoint getPokemonDetails.");

        call.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                // Log para indicar que se recibió una respuesta
                Log.d("DetailPokemonFragment", "Respuesta recibida de la API. Código HTTP: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    // Log para indicar que la respuesta es exitosa
                    Log.d("DetailPokemonFragment", "Respuesta exitosa. Procesando datos del Pokémon.");

                    // Si la respuesta es exitosa, extraemos el Pokémon y lo mostramos
                    Pokemon pokemon = response.body();

                    // Log para verificar los detalles del Pokémon recibido
                    Log.d("DetailPokemonFragment", "Detalles del Pokémon:");
                    Log.d("DetailPokemonFragment", "ID: " + pokemon.getId());
                    Log.d("DetailPokemonFragment", "Nombre: " + pokemon.getName());
                    Log.d("DetailPokemonFragment", "URL de la imagen: " + pokemon.getImageUrl());
                    Log.d("DetailPokemonFragment", "Tipo primario: " + pokemon.getTypes().get(0).getType().getName());
                    if (pokemon.getTypes().size() > 1) {
                        Log.d("DetailPokemonFragment", "Tipo secundario: " + pokemon.getTypes().get(1).getType().getName());
                    } else {
                        Log.d("DetailPokemonFragment", "No hay tipo secundario.");
                    }
                    Log.d("DetailPokemonFragment", "Peso: " + pokemon.getWeight());
                    Log.d("DetailPokemonFragment", "Altura: " + pokemon.getHeight());

                    // Cargar los detalles del Pokémon en las vistas
                    loadPokemonDetails(
                            pokemon.getId(),
                            pokemon.getName(),
                            pokemon.getImageUrl(),  // Usar el método getImageUrl() para obtener la URL de la imagen
                            pokemon.getTypes().get(0).getType().getName(),
                            pokemon.getTypes().size() > 1 ? pokemon.getTypes().get(1).getType().getName() : null,
                            pokemon.getWeight() / 10.0,  // Peso en kg (API retorna en hectogramos)
                            pokemon.getHeight() / 10.0  // Altura en metros (API retorna en decímetros)
                    );
                } else {
                    // Log para indicar que la respuesta no es exitosa
                    Log.e("DetailPokemonFragment", "Error en la respuesta de la API. Código HTTP: " + response.code());
                    pokemonName.setText(R.string.msg_error_generic);
                }
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                // Log para indicar que hubo un error en la conexión
                Log.e("DetailPokemonFragment", "Fallo al conectar con la API. Verifica la conexión a internet.", t);
                pokemonName.setText(R.string.msg_error_generic);
            }
        });
    }

    // Método para cargar los detalles del Pokémon en las vistas
    private void loadPokemonDetails(int index, String name, String imageUrl, String primaryType, String secondaryType, double pokemonWeight, double pokemonHeight) {

        // Establecer detalles básicos del Pokémon
        pokedexIndex.setText(String.format("#%03d", index));  // Formatear el índice de la Pokédex
        pokemonName.setText(name);

        // Log para verificar la URL de la imagen
        Log.d("DetailPokemonFragment", "URL de la imagen: " + imageUrl);

        // Cargar imagen del Pokémon usando Picasso
        Picasso.get()
                .load(imageUrl)  // Usar la URL proporcionada por getImageUrl()
                .placeholder(R.drawable.placeholder_image)  // Imagen de placeholder mientras carga
                .error(R.drawable.ic_error_drawable)  // Imagen que se muestra en caso de error
                .into(pokemonImage);

        // Mostrar el tipo primario y secundario (si existe)
        setType(type1, primaryType);
        setType(type2, secondaryType);

        // Mostrar peso y altura
        weight.setText(String.format("Peso: %.1f kg", pokemonWeight));
        height.setText(String.format("Altura: %.1f m", pokemonHeight));
    }

    // Método auxiliar para configurar y mostrar los tipos de Pokémon
    private void setType(TextView typeTextView, String type) {
        if (type != null && !type.isEmpty()) {
            typeTextView.setText(type);
            typeTextView.setVisibility(View.VISIBLE);  // Hacer visible
        } else {
            typeTextView.setVisibility(View.GONE);  // Ocultar si no hay tipo
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Mostrar el BottomNavigationView
        View bottomNavigationView = requireActivity().findViewById(R.id.nav_view);
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }
}
