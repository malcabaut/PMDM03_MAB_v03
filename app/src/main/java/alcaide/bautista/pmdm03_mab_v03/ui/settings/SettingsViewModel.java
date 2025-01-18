package alcaide.bautista.pmdm03_mab_v03.ui.settings;

// Importación de clases necesarias para implementar el ViewModel.
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * ViewModel para gestionar los datos del fragmento de configuración.
 * Proporciona datos observables que se utilizan para actualizar dinámicamente la interfaz de usuario.
 */
public class SettingsViewModel extends ViewModel {

    // MutableLiveData que almacena un texto observable para el fragmento de configuración.
    private final MutableLiveData<String> mText;

    /**
     * Constructor de la clase. Inicializa el MutableLiveData con un mensaje predeterminado.
     */
    public SettingsViewModel() {
        // Se crea una instancia de MutableLiveData y se establece un valor inicial.
        mText = new MutableLiveData<>();
        mText.setValue("This is the settings fragment");
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
