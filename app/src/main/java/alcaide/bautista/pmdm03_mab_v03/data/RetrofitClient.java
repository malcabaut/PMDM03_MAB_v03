package alcaide.bautista.pmdm03_mab_v03.data;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Clase para gestionar una instancia única de Retrofit.
 * Se utiliza para realizar solicitudes HTTP a la API de Pokémon.
 */
public class RetrofitClient {

    // URL base de la API de Pokémon.
    private static final String BASE_URL = "https://pokeapi.co/api/v2/";

    // Instancia única de Retrofit (singleton).
    private static Retrofit retrofit;

    /**
     * Método estático para obtener la instancia única de Retrofit.
     * Si no existe una instancia, se crea y se configura.
     *
     * @return Instancia configurada de Retrofit.
     */
    public static Retrofit getInstance() {
        // Verificar si la instancia ya está creada.
        if (retrofit == null) {
            // Crear una nueva instancia de Retrofit.
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // Establecer la URL base para todas las solicitudes.
                    .addConverterFactory(GsonConverterFactory.create()) // Usar Gson para convertir JSON a objetos Java.
                    .build(); // Construir la instancia de Retrofit.
        }
        // Retornar la instancia de Retrofit.
        return retrofit;
    }
}
