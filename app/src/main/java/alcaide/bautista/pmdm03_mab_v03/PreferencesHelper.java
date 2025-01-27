package alcaide.bautista.pmdm03_mab_v03;

import android.content.Context;

/**
 * Clase para gestionar las preferencias de idioma de la aplicación.
 */
public class PreferencesHelper {

    private static final String PREF_NAME = "language_pref";
    private static final String KEY_LANGUAGE = "language";

    /**
     * Guarda el idioma seleccionado en las preferencias.
     *
     * @param context   Contexto para acceder a SharedPreferences.
     * @param language  Código del idioma ("en" o "es").
     */
    public static void setLanguage(Context context, String language) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_LANGUAGE, language)
                .commit(); // Asegura que los cambios se escriban inmediatamente
    }

    /**
     * Obtiene el idioma guardado, por defecto "es" (español).
     *
     * @param context Contexto para acceder a SharedPreferences.
     * @return Código de idioma ("en" o "es").
     */
    public static String getLanguage(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getString(KEY_LANGUAGE, "es");
    }
}
