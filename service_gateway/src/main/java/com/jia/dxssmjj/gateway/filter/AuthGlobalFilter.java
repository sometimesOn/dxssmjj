package com.jia.dxssmjj.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.jia.dxssmjj.common.helper.JwtHelper;
import com.jia.dxssmjj.common.result.Result;
import com.jia.dxssmjj.common.result.ResultCodeEnum;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        System.out.println("===="+path);

        if(matcher.match("/**/inner/**",path)){
            ServerHttpResponse response = exchange.getResponse();
            return out(response, ResultCodeEnum.PERMISSION);
        }
        Long userId = this.getUserId(request);

        if(matcher.match("/api/**/auth/**",path)){
           if(!StringUtils.hasText(String.valueOf(userId))){
               ServerHttpResponse response = exchange.getResponse();
               return out(response,ResultCodeEnum.LOGIN_AUTH);
           }
        }
        return chain.filter(exchange);

    }

    private Mono<Void> out(ServerHttpResponse response, ResultCodeEnum resultCodeEnum) {
        Result result = Result.build(null, resultCodeEnum);
        byte[] bytes = JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);

        response.getHeaders().add("Content-Type","application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    private Long getUserId(ServerHttpRequest request) {
        String token = "";
        List<String> tokenList = request.getHeaders().get("token");
        if(null != tokenList){
            token = tokenList.get(0);
        }
        if(StringUtils.hasText(token)){
            return JwtHelper.getUserId(token);
        }
        return null;
    }



    @Override
    public int getOrder() {
        return 0;
    }
}
