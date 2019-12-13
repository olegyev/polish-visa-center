package app.entities.enums;

public enum ClientOccupation {
    EMPLOYED ("Employed"),
    ENTREPRENEUR ("Entrepreneur"),
    UNEMPLOYED ("Unemployed"),
    FOREIGN_CITIZEN ("Foreign citizen"),
    MINOR ("Minor");

    private String title;

    ClientOccupation(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static ClientOccupation findByTitle(String title){
        for (ClientOccupation constant : values()){
            if(title.equals(constant.getTitle())){
                return constant;
            }
        }
        return null;
    }
}