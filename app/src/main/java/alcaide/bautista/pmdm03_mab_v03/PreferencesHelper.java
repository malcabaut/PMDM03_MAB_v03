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
     * @param isEnglish True si el idioma es inglés, false si es español.
     */
    public static void setLanguage(Context context, boolean isEnglish) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_LANGUAGE, isEnglish ? "en" : "es")
                .apply();
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
