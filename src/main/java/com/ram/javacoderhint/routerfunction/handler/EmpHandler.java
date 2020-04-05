package com.ram.javacoderhint.routerfunction.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ram.javacoderhint.routerfunction.exception.ResourceNotFoundException;
import com.ram.javacoderhint.routerfunction.model.Employee;
import com.ram.javacoderhint.routerfunction.repository.EmpRepository;
import com.ram.javacoderhint.routerfunction.validator.AbstractValidationHandler;

import org.springframework.validation.Validator;


import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//Comment is used for request body validation

@Slf4j
//@Log4j2
@Component
//public class EmpHandler extends AbstractValidationHandler<Employee, Validator>{
public class EmpHandler {

	@Autowired
	private EmpRepository empRepository;
	
	/*
	 * private EmpHandler(@Autowired Validator validator) { super(Employee.class,
	 * validator); }
	 */
	
	public Mono<ServerResponse> createEmp(ServerRequest request) {
		Mono<Employee> empMono = request.bodyToMono(Employee.class).flatMap(user -> empRepository.save(user));
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(empMono, Employee.class);
	}
    
	/*
	 * @Override protected Mono<ServerResponse> processBody(Employee validBody,
	 * ServerRequest request) { Mono<Employee> empMono =
	 * request.bodyToMono(Employee.class).flatMap(user -> empRepository.save(user));
	 * return
	 * ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(empMono,
	 * Employee.class); }
	 */
    

	public Mono<ServerResponse> listEmp(ServerRequest request) {
		Flux<Employee> empList = empRepository.findAll();
		log.info("inside listEmp method");	
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(empList, Employee.class);
	}

	public Mono<ServerResponse> getEmpById(ServerRequest request) {
		int id = Integer.valueOf(request.pathVariable("id"));
		log.info("inside getEmpById method");
		return empRepository.findById(id)
				.flatMap(emp -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(emp))
				.switchIfEmpty(Mono.error(new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Given id not found:  "+ id)));
	}

	public Mono<ServerResponse> deleteEmp(ServerRequest serverRequest) {
		Mono<Employee> emp = serverRequest.bodyToMono(Employee.class);
		return emp.flatMap(e -> empRepository.findById(e.getId())).flatMap(e -> empRepository.delete(e))
				.then(ServerResponse.ok().build(Mono.empty()));
	}

	public Mono<ServerResponse> streamEmp(ServerRequest serverRequest) {
		Flux<Employee> empList = empRepository.findAll();
		return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(empList, Employee.class);
	}

}