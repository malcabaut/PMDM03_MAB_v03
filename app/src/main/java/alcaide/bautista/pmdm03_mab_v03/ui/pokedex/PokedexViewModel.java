package alcaide.bautista.pmdm03_mab_v03.ui.pokedex;

// Importación de clases necesarias para la implementación del ViewModel.
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * ViewModel para gestionar los datos relacionados con el fragmento de la Pokédex.
 * Proporciona datos reactivos que pueden ser observados por la interfaz de usuario.
 */
public class PokedexViewModel extends ViewModel {

    // MutableLiveData que almacena un texto observable para el fragmento de la Pokédex.
    private final MutableLiveData<String> mText;

    /**
     * Constructor de la clase. Inicializa el MutableLiveData con un mensaje predeterminado.
     */
    public PokedexViewModel() {
        // Se crea una instancia de MutableLiveData y se establece un valor inicial.
        mText = new MutableLiveData<>();
        mText.setValue("Pokedex Fragment Loaded");
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
