package alcaide.bautista.pmdm03_mab_v03.ui.captured_pokemon;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import alcaide.bautista.pmdm03_mab_v03.data.Pokemon;

public class CapturedPokemonViewModel extends ViewModel {

    private final MutableLiveData<List<Pokemon>> capturedPokemonList = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>(null);

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    /**
     * Obtiene la lista observable de Pokémon capturados.
     * @return LiveData con la lista de Pokémon.
     */
    public LiveData<List<Pokemon>> getCapturedPokemonList() {
        return capturedPokemonList;
    }

    /**
     * Obtiene el estado de carga.
     * @return LiveData con el estado de carga.
     */
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    /**
     * Obtiene el mensaje de error, si existe.
     * @return LiveData con el mensaje de error.
     */
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Método para obtener la lista de Pokémon capturados por el usuario actual.
     */
    public void fetchCapturedPokemon() {
        FirebaseUser user = auth.getCurrentUser();

        // Verifica si el usuario está autenticado.
        if (user == null) {
            errorMessage.setValue("Usuario no autenticado.");
            return;
        }

        // Inicia el estado de carga.
        isLoading.setValue(true);

        db.collection("pokemon_data")
                .whereEqualTo("user_id", user.getUid()) // Filtra por el ID del usuario.
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Pokemon> pokemonList = new ArrayList<>();

                    // Procesa cada documento recibido.
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        Pokemon pokemon = parsePokemonFromDocument(document);
                        if (pokemon != null) {
                            pokemonList.add(pokemon);
                        }
                    }

                    // Actualiza la lista de Pokémon capturados y termina el estado de carga.
                    capturedPokemonList.setValue(pokemonList);
                    isLoading.setValue(false);
                })
                .addOnFailureListener(e -> {
                    // Maneja los errores y termina el estado de carga.
                    errorMessage.setValue("Error obteniendo Pokémon: " + e.getMessage());
                    isLoading.setValue(false);
                });
    }

    /**
     * Método para convertir un documento de Firestore en un objeto Pokémon.
     * @param document El documento de Firestore.
     * @return Objeto Pokémon si los datos son válidos, de lo contrario null.
     */
    private Pokemon parsePokemonFromDocument(DocumentSnapshot document) {
        try {
            // Obtiene los campos necesarios del documento.
            String name = document.getString("pokemon_name"); // Nombre del Pokémon.
            Long idLong = document.getLong("pokemon_id"); // ID del Pokémon.
            String imageUrl = document.getString("pokemon_imagen_url"); // URL de la imagen.

            // Verifica que los datos no sean nulos antes de crear el objeto.
            if (name != null && idLong != null && imageUrl != null) {
                return new Pokemon(name, idLong.intValue(), imageUrl);
            } else {
                Log.w("CapturedPokemonVM", "Datos incompletos en el documento: " + document.getId());
            }
        } catch (Exception e) {
            // Maneja cualquier error al procesar el documento.
            Log.e("CapturedPokemonVM", "Error procesando el documento: " + document.getId(), e);
        }
        return null; // Retorna null si no se pudo procesar el documento.
    }
}
