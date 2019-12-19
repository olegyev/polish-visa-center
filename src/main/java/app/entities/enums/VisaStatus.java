package app.entities.enums;

public enum VisaStatus {
    INCOMPLETE_DOCS ("Incomplete set of documents"),
    PENDING ("Documents are pending at the embassy"),
    CONFIRMED ("Visa confirmed"),
    ISSUED ("Passport with visa issued to the client"),
    DENIED ("Visa denied");

    private String title;

    VisaStatus(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static VisaStatus findByTitle(String title){
        for (VisaStatus constant : values()){
            if(title.equals(constant.getTitle())){
                return constant;
            }
        }

        return null;
    }
}