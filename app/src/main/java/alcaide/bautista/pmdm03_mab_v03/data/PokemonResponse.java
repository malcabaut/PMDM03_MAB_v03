package alcaide.bautista.pmdm03_mab_v03.data;

import java.util.List;

/**
 * Clase que representa la respuesta de la API al solicitar una lista de Pokémon.
 * Contiene una lista de resultados, donde cada resultado representa un Pokémon básico con su nombre y URL.
 */
public class PokemonResponse {

    // Lista de resultados (cada uno representa un Pokémon con nombre y URL).
    private List<Result> results;

    /**
     * Obtener la lista de resultados de la respuesta.
     * @return Lista de objetos Result.
     */
    public List<Result> getResults() {
        return results;
    }

    /**
     * Clase interna que representa un resultado individual de la lista de Pokémon.
     * Contiene información básica como el nombre y la URL asociada al Pokémon.
     */
    public static class Result {
        private String name;  // Nombre del Pokémon
        private String url;   // URL que apunta al recurso del Pokémon

        /**
         * Obtener el nombre del Pokémon.
         * @return Nombre del Pokémon.
         */
        public String getName() {
            return name;
        }

        /**
         * Establecer el nombre del Pokémon.
         * @param name Nombre del Pokémon.
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Obtener la URL asociada al Pokémon.
         * @return URL del Pokémon.
         */
        public String getUrl() {
            return url;
        }

        /**
         * Establecer la URL asociada al Pokémon.
         * @param url URL del Pokémon.
         */
        public void setUrl(String url) {
            this.url = url;
        }

        /**
         * Obtener el ID del Pokémon a partir de su URL.
         * La URL tiene un formato estándar que incluye el ID al final, como:
         * " https://pokeapi.co/api/v2/pokemon/25/" -> El ID es 25.
         *
         * @return ID del Pokémon como entero.
         */
        public int getId() {
            try {
                // Dividir la URL por "/" y obtener el último fragmento que corresponde al ID.
                String[] urlParts = url.split("/");
                return Integer.parseInt(urlParts[urlParts.length - 1]);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                // Manejo de posibles errores al extraer el ID.
                return -1; // Retornar -1 si hay un error.
            }
        }

        /**
         * Obtener la URL del sprite oficial del Pokémon.
         * Utiliza el ID para construir la URL del sprite desde el repositorio oficial de PokeAPI.
         *
         * @return URL de la imagen del Pokémon.
         */
        public String getImageUrl() {
            int id = getId();
            if (id > 0) {
                return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + id + ".png";
            }
            return null; // Retorna null si no se puede obtener un ID válido.
        }
    }
}
