package com.ps.judge.web.auth.filter;

import com.ps.common.exception.BizException;
import com.ps.judge.web.auth.objects.AuthUserBO;
import com.ps.judge.web.auth.utils.JWTHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyPair;

@Service
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Autowired
    KeyPair keyPair;

    private String tokenHead = "Bearer ";
    private String tokenHeader = "Authorization";


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(this.tokenHeader);
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
            final String authToken = authHeader.substring(tokenHead.length()); // The part after "Bearer "
            if (!StringUtils.isEmpty(authToken)) {
                try {
                    AuthUserBO authUserBO = JWTHelper.parseToken(authToken, keyPair.getPublic());
                    if (authUserBO != null) {
                        Authentication authentication = new UsernamePasswordAuthenticationToken(authUserBO, null, authUserBO.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (Exception e) {
                    /**
                     * 目前无效，无所传递过去
                     */
                    throw new AuthenticationServiceException(e.getMessage());
                }
            }
        }
        filterChain.doFilter(request,response);
    }
}
