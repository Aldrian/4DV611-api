package se.travappar.api.model;

public enum UserRole {
    ROLE_SUPER_ADMIN("0"),
    ROLE_ADMIN("1"),
    ROLE_USER("2");

    private final String code;

    UserRole(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static UserRole getByCode(String code) {
        for (UserRole userRole : UserRole.values()) {
            if (userRole.getCode().equals(code)) {
                return userRole;
            }
        }
        return null;
    }
}
