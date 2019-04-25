package track_ninja.playlist_generator.models.commons;

public enum UserRole {

    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");


    @Override
    public String toString() {
        switch (this){
            case ROLE_USER:{
                return "ROLE_USER";
            }
            case ROLE_ADMIN:{
                return "ROLE_ADMIN";
            }
            default:{
                throw  new IllegalArgumentException("Incorrect User Role");
            }
        }
    }

    private String name;

    UserRole(String name) {
        this.name = name;
    }

    public String getUserRole(){
        return name;
    }

}


