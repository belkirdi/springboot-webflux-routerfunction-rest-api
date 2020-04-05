package com.ram.javacoderhint.routerfunction.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;
 
/**
 * @author Ram Brij Bais
 * @date 2020/4/02 18:11
 */
@Component
@Slf4j
//@Log4j2
public class GlobalErrorAttributes extends DefaultErrorAttributes {
	
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        Throwable error = getError(request);
        
        if (error instanceof ResourceNotFoundException) {
			ResourceNotFoundException responseStatusException = (ResourceNotFoundException) error;
			Map<String, Object> errorAttributes = new LinkedHashMap<>();
			errorAttributes.put("code", responseStatusException.getStatus().value());
			errorAttributes.put("msg", responseStatusException.getStatus().getReasonPhrase());
			errorAttributes.put("data", "");
			errorAttributes.put("exception", responseStatusException.getMessage());

			log.error("Exception :  {}", errorAttributes);

			return errorAttributes;
        } else if (error instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) error;
            Map<String, Object> errorAttributes = new LinkedHashMap<>();
            errorAttributes.put("code", responseStatusException.getStatus().value());
            errorAttributes.put("msg", responseStatusException.getStatus().getReasonPhrase());
            errorAttributes.put("data", "");
			errorAttributes.put("exception", responseStatusException.getMessage());
            return errorAttributes;
        } else {
            Map<String, Object> errorAttributes = super.getErrorAttributes(request, includeStackTrace);
            errorAttributes.put("code", errorAttributes.getOrDefault("status", 404));
            errorAttributes.put("msg", error.getMessage());
            errorAttributes.put("data", "");
			errorAttributes.put("exception", "INTERNAL SERVER ERROR");

            return errorAttributes;
        }
    }
}
