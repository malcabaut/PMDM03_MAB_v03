package alcaide.bautista.pmdm03_mab_v03.ui.settings;

import android.content.Context;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.text.HtmlCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import alcaide.bautista.pmdm03_mab_v03.R;

public class SettingsViewModel extends ViewModel {

    // MutableLiveData para gestionar el estado del texto en el fragmento
    private final MutableLiveData<String> mText = new MutableLiveData<>();
    // MutableLiveData para gestionar el evento de cerrar sesión
    private final MutableLiveData<Boolean> logoutEvent = new MutableLiveData<>();
    // MutableLiveData para gestionar el cambio de idioma
    private final MutableLiveData<Boolean> languageChanged = new MutableLiveData<>();
    // Firebase instances
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Estado de carga
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    // Mensajes de error
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>(null);

    public SettingsViewModel() {
        mText.setValue("This is the settings fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void updateText(String newText) {
        mText.setValue(newText);
    }

    public LiveData<Boolean> getLogoutEvent() {
        return logoutEvent;
    }

    public void onLogout() {
        logoutEvent.setValue(true);
    }

    public void logoutFromFirebase() {
        auth.signOut();

    }

    public LiveData<Boolean> getLanguageChanged() {
        return languageChanged;
    }

    public void setLanguageChanged(boolean isChanged) {
        languageChanged.setValue(isChanged);
    }

    public LiveData<Boolean> getDeletePokemonEvent() {
        MutableLiveData<Boolean> deletePokemonEvent = new MutableLiveData<>();
        return deletePokemonEvent;
    }

    public void onDeletePokemon() {
        updateText("Pokémon eliminado");
    }

    public void onAbout(Context context) {
        TextView textView = new TextView(context);
        textView.setText(HtmlCompat.fromHtml(
                context.getString(R.string.app_about_info),
                HtmlCompat.FROM_HTML_MODE_LEGACY
        ));
        textView.setTextAppearance(R.style.H1);

        new AlertDialog.Builder(context)
                .setTitle(R.string.app_about)
                .setView(textView)
                .setIcon(R.drawable.pokebola_transformadose)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    /**
     * Método para eliminar todos los Pokémon capturados por el usuario actual.
     */
    public void deleteAllCapturedPokemon() {
        FirebaseUser user = auth.getCurrentUser();

        // Verifica si el usuario está autenticado.
        if (user == null) {
            errorMessage.setValue("Usuario no autenticado.");
            return;
        }

        // Inicia el estado de carga.
        isLoading.setValue(true);

        // Obtiene los documentos de Pokémon para el usuario actual.
        db.collection("pokemon_data")
                .whereEqualTo("user_id", user.getUid()) // Filtra por el ID del usuario.
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Verifica si hay documentos para eliminar.
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            // Elimina cada documento de Pokémon.
                            document.getReference().delete()
                                    .addOnSuccessListener(aVoid -> {
                                        // Lógica adicional si necesitas realizar alguna acción cuando un documento se elimine.
                                    })
                                    .addOnFailureListener(e -> {
                                        // Maneja el error si ocurre durante la eliminación del documento.
                                        errorMessage.setValue("Error eliminando Pokémon: " + e.getMessage());
                                    });
                        }

                        // Notifica que la operación se ha completado.
                        isLoading.setValue(false);
                    } else {
                        errorMessage.setValue("No hay Pokémon para eliminar.");
                        isLoading.setValue(false);
                    }
                })
                .addOnFailureListener(e -> {
                    // Maneja el error si ocurre durante la obtención de los documentos.
                    errorMessage.setValue("Error obteniendo los Pokémon: " + e.getMessage());
                    isLoading.setValue(false);
                });
    }

    // Métodos adicionales como getter para isLoading y errorMessage si lo necesitas.
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}
