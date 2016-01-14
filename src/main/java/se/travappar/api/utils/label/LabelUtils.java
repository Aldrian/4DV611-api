package se.travappar.api.utils.label;

public enum LabelUtils {
    ANY_TRACK_FREE_ENTRANCE("Free entrance to any track.");

    private final String message;

    LabelUtils(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
