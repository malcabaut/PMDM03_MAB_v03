package alcaide.bautista.pmdm03_mab_v03.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interfaz para definir los endpoints de la API de Pokémon.
 * Utiliza Retrofit para realizar solicitudes HTTP.
 */
public interface PokemonApiService {

    /**
     * Obtener una lista de Pokémon desde el endpoint principal de la API.
     * Se usa paginación para manejar grandes conjuntos de datos.
     *
     * @param offset Número de Pokémon a omitir desde el inicio (para la paginación).
     * @param limit Número máximo de Pokémon a devolver por página.
     * @return Una llamada de Retrofit que contiene una respuesta de tipo PokemonResponse.
     */
    @GET("pokemon")
    Call<PokemonResponse> getPokemonList(
            @Query("offset") int offset,  // Parámetro de consulta para paginación (inicio)
            @Query("limit") int limit     // Parámetro de consulta para el tamaño de la página
    );

    /**
     * Obtener los detalles completos de un Pokémon usando su ID o nombre.
     * Esto incluye atributos detallados como estadísticas, tipos y habilidades.
     *
     * @param idOrName ID único (número) o nombre del Pokémon (por ejemplo, "pikachu").
     * @return Una llamada de Retrofit que contiene un objeto Pokémon con todos los detalles.
     */
    @GET("pokemon/{idOrName}")
    Call<Pokemon> getPokemonDetails(
            @Path("idOrName") String idOrName // Parámetro dinámico en la URL para ID o nombre
    );

    // Posibles mejoras adicionales:
    // 1. Añadir manejo de excepciones o respuesta genérica con Result<T> para errores.
    // 2. Soporte para obtener más información (ejemplo: abilities, species) desde otros endpoints.
}
