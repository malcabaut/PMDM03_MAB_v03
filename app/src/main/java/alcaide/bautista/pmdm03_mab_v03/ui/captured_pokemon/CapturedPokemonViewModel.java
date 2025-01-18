package alcaide.bautista.pmdm03_mab_v03.ui.captured_pokemon;

// Importación de clases necesarias para la implementación del ViewModel.
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * ViewModel para gestionar los datos relacionados con el fragmento de Pokémon capturados.
 * Proporciona un enfoque reactivo para actualizar la interfaz de usuario cuando los datos cambian.
 */
public class CapturedPokemonViewModel extends ViewModel {

    // MutableLiveData que contiene el texto observable para el fragmento.
    private final MutableLiveData<String> mText;

    /**
     * Constructor de la clase. Inicializa el MutableLiveData con un mensaje predeterminado.
     */
    public CapturedPokemonViewModel() {
        // Se crea una instancia de MutableLiveData y se establece un valor inicial.
        mText = new MutableLiveData<>();
        mText.setValue("Captured Pokemon Fragment Loaded");
    }

    /**
     * Devuelve un LiveData que permite observar cambios en el texto.
     *
     * @return Un LiveData que contiene el texto observable.
     */
    public LiveData<String> getText() {
        return mText;
    }
}
