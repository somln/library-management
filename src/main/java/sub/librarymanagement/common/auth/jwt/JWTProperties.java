package sub.librarymanagement.common.auth.jwt;

public class JWTProperties {

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING_TOKEN = "Authorization";
    public static final long TOKEN_EXPIRATION_TIME = 60*60*1000L; //1시간
}
