package alcaide.bautista.pmdm03_mab_v03.data;

import java.util.List;

public class Pokemon {
    private String name;  // Nombre del Pokémon
    private int id;       // ID del Pokémon
    private String url;   // URL base del Pokémon (opcional)
    private List<Type> types;  // Lista de tipos del Pokémon (para la versión detallada)
    private int weight;  // Peso del Pokémon (en hectogramos)
    private int height;  // Altura del Pokémon (en decímetros)
    private Sprites sprites;  // Información de sprites (para la versión detallada)

    // Constructor vacío necesario para Firestore
    public Pokemon() {
    }

    // Constructor para la versión básica con solo nombre, ID y URL
    public Pokemon(String name, int id, String url) {
        this.name = name;
        this.id = id;
        this.url = url;
    }

    // Constructor para la versión detallada (con tipos, peso, altura y sprites)
    public Pokemon(String name, int id, List<Type> types, int weight, int height) {
        this.name = name;
        this.id = id;
        this.types = types;
        this.weight = weight;
        this.height = height;
    }

    // Getters y setters

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


    // Método para obtener la URL de la imagen del Pokémon (usando el sprite oficial)
    public String getImageUrl() {
            return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + id + ".png";
    }

    // Método para extraer el ID desde la URL si es necesario
    public void extractIdFromUrl() {
        if (url != null && !url.isEmpty()) {
            String[] urlParts = url.split("/");
            this.id = Integer.parseInt(urlParts[urlParts.length - 1]);
        }
    }

    // Clase interna para representar los tipos del Pokémon
    public static class Type {
        private TypeDetails type;

        public TypeDetails getType() {
            return type;
        }

        public void setType(TypeDetails type) {
            this.type = type;
        }

        // Clase interna para detalles del tipo
        public static class TypeDetails {
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }

    // Clase interna para representar los sprites del Pokémon
    public static class Sprites {
        private String officialArtwork;

        public String getOfficialArtwork() {
            return officialArtwork;
        }

        public void setOfficialArtwork(String officialArtwork) {
            this.officialArtwork = officialArtwork;
        }
    }
}
