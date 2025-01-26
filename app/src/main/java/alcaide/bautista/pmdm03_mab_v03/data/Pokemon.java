package alcaide.bautista.pmdm03_mab_v03.data;

import java.util.List;

/**
 * Clase que representa un Pokémon.
 * Contiene información básica como nombre, ID, tipos, peso y altura.
 */
public class Pokemon {
    private String name;  // Nombre del Pokémon
    private int id;       // ID único del Pokémon
    private List<Type> types;  // Lista de tipos del Pokémon (se usa en información detallada)
    private int weight;   // Peso del Pokémon en hectogramos
    private int height;   // Altura del Pokémon en decímetros

    /**
     * Constructor vacío necesario para Firestore.
     * Firestore requiere un constructor sin argumentos para poder mapear los datos.
     */
    public Pokemon() {
    }

    /**
     * Constructor básico para inicializar un Pokémon con nombre, ID y URL.
     * @param name Nombre del Pokémon.
     * @param id ID único del Pokémon.
     * @param url URL base asociada al Pokémon.
     */
    public Pokemon(String name, int id, String url) {
        this.name = name;
        this.id = id;
        // URL base asociada al Pokémon (opcional)
    }

    // Métodos Getter y Setter para los atributos principales.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Type> getTypes() {
        return types;
    }


    public int getWeight() {
        return weight;
    }


    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Método para obtener la URL de la imagen del Pokémon.
     * La imagen se obtiene del repositorio oficial de PokeAPI utilizando el ID del Pokémon.
     * @return URL del sprite oficial del Pokémon.
     */
    public String getImageUrl() {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + id + ".png";
    }

    /**
     * Clase interna que representa un tipo de Pokémon.
     * Cada Pokémon puede tener uno o más tipos asociados.
     */
    public static class Type {
        private TypeDetails type;  // Detalles del tipo

        public TypeDetails getType() {
            return type;
        }


        /**
         * Clase interna que representa los detalles de un tipo.
         * Contiene información como el nombre del tipo (por ejemplo: "fire", "water").
         */
        public static class TypeDetails {
            private String name;  // Nombre del tipo

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}

