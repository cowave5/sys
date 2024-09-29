package com.cowave.sys.gateway.filter;

import com.cowave.commons.client.http.asserts.I18Messages;
import com.cowave.commons.client.http.response.Response;
import com.cowave.commons.client.http.response.ResponseCode;
import com.cowave.commons.framework.access.Access;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

import static com.cowave.commons.client.http.constants.HttpCode.*;
import static com.cowave.commons.client.http.constants.HttpHeader.Authorization;
import static com.cowave.commons.client.http.constants.HttpHeader.X_User_Payload;
import static com.cowave.commons.framework.access.security.BearerTokenService.*;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    private final ObjectMapper objectMapper;

    @Value("${spring.auth.secret}")
    private String authSecret;

    @Value("${spring.auth.ignoreUrls}")
    private List<String> authIgnoreUrls;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // url白名单
        boolean isIgnoreUrl = authIgnoreUrls != null && authIgnoreUrls.stream().anyMatch(p -> PATH_MATCHER.match(p, path));
        if (isIgnoreUrl) {
            return chain.filter(exchange);
        }

        // 获取token
        String token = exchange.getRequest().getHeaders().getFirst(Authorization);
        if (StringUtils.isBlank(token)) {
            return writeResponse(exchange.getResponse(), UNAUTHORIZED, "frame.auth.no");
        }
        if(token.startsWith("Bearer ")) {
            token = token.replace("Bearer ", "");
        }

        // 校验token
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(authSecret).parseClaimsJws(token).getBody();
        }catch(ExpiredJwtException e) {
            return writeResponse(exchange.getResponse(), INVALID_TOKEN, "frame.auth.expired");
        }catch(Exception e) {
            return writeResponse(exchange.getResponse(), UNAUTHORIZED, "frame.auth.invalid");
        }

        // IP变化要求重新刷一下accessToken
        String userIp = (String)claims.get(CLAIM_USER_IP);
        String tokenConflict = (String)claims.get(CLAIM_CONFLICT);
        if("Y".equals(tokenConflict) && !Objects.equals(Access.accessIp(), userIp)) {
            return writeResponse(exchange.getResponse(), INVALID_TOKEN, "frame.auth.ipchanged");
        }

        // 获取载荷部分
        String[] tokenParts = token.split("\\.");
        String tokenPayload = tokenParts[1];
        exchange.mutate().request(exchange.getRequest().mutate().header(X_User_Payload, tokenPayload).build()).build();

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private Mono<Void> writeResponse(ServerHttpResponse response, ResponseCode responseCode, String messageKey) {
        // int httpStatus = responseCode.getStatus();
        response.setStatusCode(HttpStatus.valueOf(SUCCESS.getStatus()));
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(Response.msg(responseCode, I18Messages.msg(messageKey)));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }
}
