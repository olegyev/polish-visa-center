package app.entities.enums;

public enum VisaStatus {
    DOCS_RECEIVED ("Documents received from client"),
    PENDING ("Documents are pending at the embassy"),
    CONFIRMED ("Visa confirmed"),
    DENIED ("Visa denied"),
    ISSUED ("Passport with visa issued to the client");

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