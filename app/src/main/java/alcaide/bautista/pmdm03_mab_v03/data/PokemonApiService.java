package alcaide.bautista.pmdm03_mab_v03.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PokemonApiService {

    // Endpoint para obtener una lista de Pokémon con paginación
    @GET("pokemon")
    Call<PokemonResponse> getPokemonList(
            @Query("offset") int offset,
            @Query("limit") int limit
    );

    // Endpoint para obtener los detalles de un Pokémon por su ID
    @GET("pokemon/{id}")
    Call<PokemonResponse.Result> getPokemonDetails(
            @Path("id") int id
    );
}
