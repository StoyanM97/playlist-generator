package track_ninja.playlist_generator.database_generator.common;

public enum GenreStyle {

    POP("Pop"),
    ROCK("Rock"),
    DANCE("Dance");


    private String style;

    GenreStyle(String style) {
        this.style = style;
    }

    public String getStyle(){
        return this.style;
    }

    @Override
    public String toString() {
        switch (this){
            case POP:{
                return "Pop";
            }
            case ROCK:{
                return "Rock";
            }
            case DANCE:{
                return "Dance";
            }
            default:{
                throw  new IllegalArgumentException("Incorrect GenerationGenre Style");
            }
        }
    }
}
