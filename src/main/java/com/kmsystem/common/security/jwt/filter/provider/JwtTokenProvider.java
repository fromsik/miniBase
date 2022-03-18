package com.kmsystem.common.security.jwt.filter.provider;

import com.kmsystem.common.exception.ErrorExceptionHandler;
import com.kmsystem.common.security.domain.CustomUser;
import com.kmsystem.common.security.jwt.constants.SecurityConstants;
import com.kmsystem.config.Properties;
import com.kmsystem.member.domain.ResRegister;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private final Properties properties;

    //3-2. 토큰 생성
    public String createToken(long memberId, String memId, List<String> roles) {
        byte[] signingKey = getSigningKey();

        String token = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
                .setExpiration(new Date(System.currentTimeMillis() + 1000L*60*60*12))
                .claim("memberId", "" + memberId)
                .claim("memId", memId)
                .claim("role", roles)
                .compact();
        return token;
    }


    // 유효한 토큰인지 검사
    public UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader) {
        if (isNotEmpty(tokenHeader)) {
            try {
                byte[] signingKey = getSigningKey();

                Jws<Claims> parsedToken = Jwts.parser()
                        .setSigningKey(signingKey)
                        .parseClaimsJws(tokenHeader.replace("Bearer ", ""));

                Claims claims = parsedToken.getBody();

                String memberId = (String)claims.get("memberId");
                String memId = (String)claims.get("memId");
                

                List<SimpleGrantedAuthority> authorities = ((List<?>) claims.get("role"))
                        .stream()
                        .map(authority -> new SimpleGrantedAuthority((String) authority))
                        .collect(Collectors.toList());
                
                if (isNotEmpty(memId)) {
                    ResRegister resRegister = new ResRegister();
                    resRegister.setMemberId(Integer.parseInt(memberId));
                    resRegister.setMemId(memId);
                    resRegister.setMemPw("");

                    UserDetails userDetails = new CustomUser(resRegister, authorities);

                    return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                }
            } catch (ExpiredJwtException exception) {
                log.warn("Request to parse expired JWT : {} failed : {}", tokenHeader, exception.getMessage());
            } catch (UnsupportedJwtException exception) {
                log.warn("Request to parse unsupported JWT : {} failed : {}", tokenHeader, exception.getMessage());
            } catch (MalformedJwtException exception) {
                log.warn("Request to parse invalid JWT : {} failed : {}", tokenHeader, exception.getMessage());
            } catch (IllegalArgumentException exception) {
                log.warn("Request to parse empty or null JWT : {} failed : {}", tokenHeader, exception.getMessage());
            }
        }

        return null;
    }

    private byte[] getSigningKey() {
        return properties.getSecretKey().getBytes();
    }

    private boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }

    private boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(properties.getSecretKey()).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException exception) {
            log.error("Token Expired");
            return false;
        } catch (JwtException exception) {
            log.error("Token Tampered");
            return false;
        } catch (NullPointerException exception) {
            log.error("Token is null");
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
