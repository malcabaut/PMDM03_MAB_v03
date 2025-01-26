package alcaide.bautista.pmdm03_mab_v03.ui.captured_pokemon;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import alcaide.bautista.pmdm03_mab_v03.data.Pokemon;

public class CapturedPokemonViewModel extends ViewModel {

    private final MutableLiveData<List<Pokemon>> capturedPokemonList = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>(null);
    private final MutableLiveData<String> successMessage = new MutableLiveData<>(null);

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    private static final String TAG = "CapturedPokemonVM";

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
     * Obtiene el mensaje de éxito, si existe.
     * @return LiveData con el mensaje de éxito.
     */
    public LiveData<String> getSuccessMessage() {
        return successMessage;
    }

    /**
     * Configura un listener en tiempo real para sincronizar la lista de Pokémon capturados.
     */
    public void observeCapturedPokemon() {
        FirebaseUser user = auth.getCurrentUser();

        // Verifica si el usuario está autenticado.
        if (user == null) {
            errorMessage.setValue("Usuario no autenticado.");
            return;
        }

        // Muestra que la carga está activa.
        isLoading.setValue(true);

        // Configura el listener para actualizaciones en tiempo real.
        db.collection("pokemon_data")
                .whereEqualTo("user_id", user.getUid()) // Filtra por el usuario actual.
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e(TAG, "Error al observar cambios en los Pokémon", error);
                        errorMessage.setValue("Error observando los cambios: " + error.getMessage());
                        isLoading.setValue(false);
                        return;
                    }

                    if (value != null) {
                        List<Pokemon> updatedList = new ArrayList<>();
                        for (DocumentSnapshot document : value) {
                            Pokemon pokemon = parsePokemonFromDocument(document);
                            if (pokemon != null) {
                                updatedList.add(pokemon);
                            }
                        }
                        capturedPokemonList.setValue(updatedList);
                    }
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
            String name = document.getString("pokemon_name"); // Nombre del Pokémon.
            Long idLong = document.getLong("pokemon_id"); // ID del Pokémon.
            String imageUrl = document.getString("pokemon_imagen_url"); // URL de la imagen.

            if (name != null && idLong != null && imageUrl != null) {
                return new Pokemon(name, idLong.intValue(), imageUrl);
            } else {
                Log.w(TAG, "Datos incompletos en el documento: " + document.getId());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error procesando el documento: " + document.getId(), e);
        }
        return null;
    }

    /**
     * Método para eliminar un Pokémon específico capturado por el usuario actual.
     *
     * @param pokemonId El ID único del Pokémon que se desea eliminar.
     */
    public void deleteCapturedPokemon(int pokemonId) {
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            errorMessage.setValue("Usuario no autenticado.");
            return;
        }

        isLoading.setValue(true);

        db.collection("pokemon_data")
                .whereEqualTo("user_id", user.getUid())
                .whereEqualTo("pokemon_id", pokemonId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);

                        document.getReference().delete()
                                .addOnSuccessListener(aVoid -> {
                                    successMessage.setValue("Pokémon eliminado correctamente.");
                                    isLoading.setValue(false);
                                })
                                .addOnFailureListener(e -> {
                                    errorMessage.setValue("Error eliminando Pokémon: " + e.getMessage());
                                    isLoading.setValue(false);
                                });
                    } else {
                        errorMessage.setValue("Pokémon no encontrado.");
                        isLoading.setValue(false);
                    }
                })
                .addOnFailureListener(e -> {
                    errorMessage.setValue("Error buscando el Pokémon: " + e.getMessage());
                    isLoading.setValue(false);
                });
    }
}
