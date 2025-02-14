package sub.librarymanagement.common.auth.jwt;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import sub.librarymanagement.common.auth.userDetail.CustomUserDetails;
import sub.librarymanagement.common.exception.ErrorCode;
import sub.librarymanagement.persistence.user.entity.User;

import java.io.IOException;

import static sub.librarymanagement.common.auth.jwt.JWTProperties.*;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // request에서 Authorization 헤더를 찾음
            String authorization = request.getHeader(HEADER_STRING_TOKEN);

            // Authorization 헤더 검증
            if (authorization == null || !authorization.startsWith(TOKEN_PREFIX)) {
                throw new JwtException(ErrorCode.AUTHORIZATION_HEADER_NOT_FOUND.getMessage());
            }

            // "Bearer " 부분 제거 후 토큰만 추출
            String[] parts = authorization.split(" ");
            if (parts.length != 2) {
                throw new JwtException(ErrorCode.INVALID_TOKEN.getMessage());
            }

            String token = parts[1];

            // 토큰 소멸 시간 검증
            jwtUtil.isExpired(token);

            // 토큰에서 username과 role 획득
            String username = jwtUtil.getUsername(token);
            String role = jwtUtil.getRole(token);

            User user = User.of(username, "temp@example.com", "tempPW", role);
            CustomUserDetails customUserDetails = new CustomUserDetails(user);

            Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
            filterChain.doFilter(request, response);

        } catch (JwtException e) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String jsonResponse = String.format("{\"code\": %d, \"errorMessage\": \"%s\"}",
                    HttpServletResponse.SC_UNAUTHORIZED,
                    e.getMessage());
            response.getWriter().write(jsonResponse);
            return;
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 로그인 경로를 필터링에서 제외
        String path = request.getServletPath();
        return path.equals("/login");
    }


}

