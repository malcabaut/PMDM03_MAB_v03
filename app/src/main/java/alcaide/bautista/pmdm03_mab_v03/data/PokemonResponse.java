package alcaide.bautista.pmdm03_mab_v03.data;

import java.util.List;

public class PokemonResponse {
    private List<Result> results;

    public List<Result> getResults() {
        return results;
    }

    public static class Result {
        private String name;
        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        // Método para obtener el ID del Pokémon a partir de la URL
        public int getId() {
            String[] urlParts = url.split("/");
            return Integer.parseInt(urlParts[urlParts.length - 1]);
        }

        // Método para obtener la URL de la imagen del Pokémon
        public String getImageUrl() {
            int id = getId();
            return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + id + ".png";
        }
    }

}
