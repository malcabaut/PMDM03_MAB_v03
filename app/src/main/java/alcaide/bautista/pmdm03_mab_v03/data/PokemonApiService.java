package alcaide.bautista.pmdm03_mab_v03.data;

import alcaide.bautista.pmdm03_mab_v03.data.PokemonResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PokemonApiService {
    @GET("pokemon")
    Call<PokemonResponse> getPokemonList(
            @Query("offset") int offset,
            @Query("limit") int limit
    );
}