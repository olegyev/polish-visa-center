package app.first;

public enum City {
    MINSK ("Minsk"),
    GOMEL ("Gomel"),
    MOGILEV ("Mogilev"),
    BREST ("Brest"),
    GRODNO ("Grodno");

    private String title;

    City(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static City findByTitle(String title){
        for (City constant : values()){
            if (title.equals(constant.getTitle())){
                return constant;
            }
        }

        return null;
    }
}
