package alcaide.bautista.pmdm03_mab_v03.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PokemonApiService {

    /**
     * Obtener una lista de Pokémon con paginación.
     *
     * @param offset Número de Pokémon a saltar.
     * @param limit Número máximo de Pokémon a devolver.
     * @return Lista de Pokémon paginada.
     */
    @GET("pokemon")
    Call<PokemonResponse> getPokemonList(
            @Query("offset") int offset,
            @Query("limit") int limit
    );

    /**
     * Obtener los detalles de un Pokémon por ID o nombre.
     *
     * @param idOrName ID (número) o nombre del Pokémon.
     * @return Detalles del Pokémon.
     */
    @GET("pokemon/{idOrName}")
    Call<Pokemon> getPokemonDetails(
            @Path("idOrName") String idOrName
    );
}
