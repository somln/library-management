package sub.librarymanagement.persistence.user.enums;

import sub.librarymanagement.common.exception.ApplicationException;
import sub.librarymanagement.common.exception.ErrorCode;

public enum Role {
    ADMIN, USER;

    public static Role from(String role) {
        try {
            return Role.valueOf(role);
        } catch (IllegalArgumentException e) {
            throw new ApplicationException(ErrorCode.INVALID_ROLE);
        }
    }
}