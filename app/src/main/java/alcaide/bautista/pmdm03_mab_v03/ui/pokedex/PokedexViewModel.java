package alcaide.bautista.pmdm03_mab_v03.ui.pokedex;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Random;

import alcaide.bautista.pmdm03_mab_v03.data.PokemonApiService;
import alcaide.bautista.pmdm03_mab_v03.data.RetrofitClient;
import alcaide.bautista.pmdm03_mab_v03.data.PokemonResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokedexViewModel extends ViewModel {
    private final MutableLiveData<PokemonResponse> pokemonList = new MutableLiveData<>();
    private final PokemonApiService apiService;

    public PokedexViewModel() {
        apiService = RetrofitClient.getInstance().create(PokemonApiService.class);
    }

    public LiveData<PokemonResponse> getPokemonList() {
        return pokemonList;
    }

    public void fetchPokemonList(int offset, int limit) {
        // Limpiar la lista antes de obtener nuevos datos
        pokemonList.postValue(null);

        apiService.getPokemonList(offset, limit).enqueue(new Callback<PokemonResponse>() {
            @Override
            public void onResponse(Call<PokemonResponse> call, Response<PokemonResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    pokemonList.postValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<PokemonResponse> call, Throwable t) {
                // Manejo de errores
            }
        });
    }

}
