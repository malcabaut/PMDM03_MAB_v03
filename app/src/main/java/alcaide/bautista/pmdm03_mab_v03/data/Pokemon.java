package alcaide.bautista.pmdm03_mab_v03.data;

public class Pokemon {
    private String name;  // Nombre del Pokémon
    private int id;       // ID del Pokémon (necesario para formar la URL de la imagen)
    private String url;   // URL base del Pokémon (opcional, según los datos de origen)

    // Constructor vacío necesario para Firestore
    public Pokemon() {
    }

    // Constructor con todos los parámetros
    public Pokemon(String name, int id, String url) {
        this.name = name;
        this.id = id;
        this.url = url;
    }



    // Getter para el nombre del Pokémon
    public String getName() {
        return name;
    }

    // Setter para el nombre del Pokémon
    public void setName(String name) {
        this.name = name;
    }

    // Getter para el ID del Pokémon
    public int getId() {
        return id;
    }

    // Setter para el ID del Pokémon
    public void setId(int id) {
        this.id = id;
    }

    // Getter para la URL del Pokémon
    public String getUrl() {
        return url;
    }

    // Setter para la URL del Pokémon
    public void setUrl(String url) {
        this.url = url;
    }

    // Método para obtener el ID del Pokémon a partir de la URL (si no está definido explícitamente)
    public void extractIdFromUrl() {
        if (url != null && !url.isEmpty()) {
            String[] urlParts = url.split("/");
            this.id = Integer.parseInt(urlParts[urlParts.length - 1]);
        }
    }

    // Getter para la URL de la imagen del Pokémon
    public String getImageUrl() {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + id + ".png";
    }
}
