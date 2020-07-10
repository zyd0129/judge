package com.ps.judge.web.auth.filter;

import com.ps.judge.web.auth.objects.AuthUserBO;
import com.ps.judge.web.auth.utils.JWTHelper;
import com.ps.judge.web.models.ConfigProductBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyPair;

@Service //这种写法不是很合适，secuirty链中和 servlet链中各有一个，重复了
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Autowired
    KeyPair keyPair;

    private String tokenHead = "Bearer ";
    private String tokenHeader = "Authorization";

    private RequestMatcher ignoreAuthenticationRequestMatcher = new AntPathRequestMatcher("/auth/login", "POST");

    private boolean ignoreAuthentication(HttpServletRequest request,
                                         HttpServletResponse response) {
        return ignoreAuthenticationRequestMatcher.matches(request);
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(this.tokenHeader);
        if (!ignoreAuthentication(request, response) && authHeader != null && authHeader.startsWith(tokenHead)) {
            final String authToken = authHeader.substring(tokenHead.length()); // The part after "Bearer "
            if (!StringUtils.isEmpty(authToken)) {
                try {
                    AuthUserBO authUserBO = JWTHelper.parseToken(authToken, keyPair.getPublic());
                    if (authUserBO != null) {
                        Authentication authentication = new UsernamePasswordAuthenticationToken(authUserBO, null, authUserBO.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (Exception e) {
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
