package org.server.remoteclass.jwt;

import org.server.remoteclass.dto.auth.RequestTokenDto;
import org.server.remoteclass.dto.auth.ResponseTokenDto;
import org.server.remoteclass.exception.BadRequestArgumentException;
import org.server.remoteclass.exception.ErrorCode;
import org.server.remoteclass.exception.ForbiddenException;
import org.server.remoteclass.exception.IdNotExistException;
import org.server.remoteclass.service.auth.AuthService;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends GenericFilterBean {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    // 요청 시에 들어올 Refresh Token의 Key값
    public static final String REFRESH_TOKEN_HEADER = "Refresh";

    private final TokenProvider tokenProvider;
    private final AuthService authService;

    public JwtFilter(TokenProvider tokenProvider, AuthService authService) {
        this.tokenProvider = tokenProvider;
        this.authService = authService;
    }

    // 실제 필터링 로직은 doFilter에서 함
    // JWT 토큰의 인증 정보를 현재 쓰레드의 SecurityContext에 저장하는 역할 수행
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);
        String jwt2 = resolveRefreshToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        // 리프레쉬 토큰이 null 인경우(요청에 들어오지 않은 경우)
        if(jwt2 == null){
            if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt) == 1){
                Authentication authentication = tokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.debug("Security Context에 `{}` 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
                filterChain.doFilter(servletRequest, servletResponse);
            }
            else{
                logger.debug("유효한 JWT 토큰이 없습니다. uri {}", requestURI);
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }

        // 리프레쉬 토큰이 들어온 경우, 리프레쉬 토큰이 유효하다면 토큰 재발급을 실행한다.
        // 리프레쉬 토큰이 유효하지 않은 경우, 엑세스 토큰이 유효해도 예외가 발생한다.
        else{
            if(StringUtils.hasText(jwt) && StringUtils.hasText(jwt2) && (
                    tokenProvider.validateToken(jwt) == -1 || tokenProvider.validateToken(jwt) == 1)
                    && tokenProvider.validateToken(jwt2) == 1) {

                HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
                // DB에 있는 refresh 토큰과 일치하지 않으면 이 부분에서 에러가 발생합니다.
                ResponseTokenDto responseTokenDto;
                try {
                    responseTokenDto = authService.reissue(new RequestTokenDto(jwt, jwt2));
                    httpServletResponse.addHeader("grantType", "bearer");
                    httpServletResponse.addHeader("accessToken", responseTokenDto.getAccessToken());
                    httpServletResponse.addHeader("refreshToken", responseTokenDto.getRefreshToken());
                    //헤더에는 Long 타입을 담을 수 없어 String으로 변환하였습니다. -> 굳이 반환이 필요하지 않을 것 같아 반환하지 않도록 하였습니다.
//                httpServletResponse.addHeader("accessTokenExpireDate", responseTokenDto.getAccessTokenExpireDate().toString());

                    Authentication authentication = tokenProvider.getAuthentication(jwt);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.debug("Security Context에 `{}` 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
                    // 토큰이 업데이트 후 DB에 저장하게 되면 servletRequest에 있는 토큰과 달라지게 되어 인증에 실패함.
                    // 그래서 토큰을 업데이트만 해놓고, 인증 처리 후 응답답 반환 시에 토큰을 B에 반영함.
                    filterChain.doFilter(servletRequest, servletResponse);
                    RequestTokenDto requestTokenDto = new RequestTokenDto();
                    requestTokenDto.setAccessToken(responseTokenDto.getAccessToken());
                    requestTokenDto.setRefreshToken(responseTokenDto.getRefreshToken());
                    authService.updateToken(requestTokenDto);
                }

                catch (BadRequestArgumentException e){
                    logger.info("e.printStackTrace");
                    e.printStackTrace();
                    throw new BadRequestArgumentException("예외 처리 부분", ErrorCode.BAD_REQUEST_ARGUMENT);
                }
            }

            else{
                logger.debug("유효한 JWT 토큰이 없습니다. uri {}", requestURI);
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }
    }

    private String resolveToken(HttpServletRequest request){
        String bearToken = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(bearToken) && bearToken.startsWith(BEARER_PREFIX)){
            return bearToken.substring(7);
        }
        return null;
    }

    private String resolveRefreshToken(HttpServletRequest request){
        String bearToken = request.getHeader(REFRESH_TOKEN_HEADER);
        if(StringUtils.hasText(bearToken) && bearToken.startsWith(BEARER_PREFIX)){
            return bearToken.substring(7);
        }
        return null;
    }
}
