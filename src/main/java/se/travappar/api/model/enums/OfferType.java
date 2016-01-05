package se.travappar.api.model.enums;

public enum OfferType {
    FREE_ENTRANCE("0"),
    FREE_FOOD("1"),
    OTHER("2");

    private final String code;

    OfferType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static OfferType getByCode(String code) {
        for (OfferType offerType : OfferType.values()) {
            if (offerType.getCode().equals(code)) {
                return offerType;
            }
        }
        return null;
    }
}
