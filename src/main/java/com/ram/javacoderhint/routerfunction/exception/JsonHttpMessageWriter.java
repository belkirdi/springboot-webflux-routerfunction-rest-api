package com.ram.javacoderhint.routerfunction.exception;

import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
 
import java.util.Collections;
import java.util.List;
import java.util.Map;
 
/**
 * @author Ram Brij Bais
 * @date 2020/4/02 19:07
 */
@Component
public class JsonHttpMessageWriter implements HttpMessageWriter<Map<String, Object>> {
    @NonNull
    @Override
    public List<MediaType> getWritableMediaTypes() {
        return Collections.singletonList(MediaType.APPLICATION_JSON);
    }
 
    @Override
    public boolean canWrite(@NonNull ResolvableType elementType, MediaType mediaType) {
        return MediaType.APPLICATION_JSON.includes(mediaType);
    }
 
    @NonNull
    @Override
    public Mono<Void> write(@NonNull Publisher<? extends Map<String, Object>> inputStream,
                            @NonNull ResolvableType elementType,
                            MediaType mediaType,
                            @NonNull ReactiveHttpOutputMessage message,
                            @NonNull Map<String, Object> hints) {
        return Mono.from(inputStream).flatMap(m -> message.writeWith(Mono.just(message.bufferFactory().wrap(transform2Json(m).getBytes()))));
    }
 
    private String transform2Json(Map<String, Object> sourceMap) {
        return "{" +
                "\"code\":" +
                sourceMap.getOrDefault("code", 500) +
                "," +
                "\"msg\":\"" +
                sourceMap.getOrDefault("msg", "") +
                "\"," +
                "\"data\":\"" +
                sourceMap.getOrDefault("data", "") +
                "\"," +
                "\"exception\":\"" +
                sourceMap.getOrDefault("exception", "") +                
                "\"}";
    }
}