package com.vodafone.client;

import com.vodafone.entity.Department;
import com.vodafone.entity.Employee;
import com.vodafone.exceptions.ClientOneException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.time.temporal.ChronoUnit;
import java.util.List;

@RegisterRestClient(baseUri = "http://localhost:8081/employee")
public interface EmployeeClient {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(unit = ChronoUnit.SECONDS, value = 10)
    @Retry(delayUnit = ChronoUnit.SECONDS, maxRetries = 2, delay = 1)
    @Fallback(fallbackMethod = "employeeListFallback")
    @CircuitBreaker(requestVolumeThreshold = 4)
    List<Employee> getAllEmployees();

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(unit = ChronoUnit.SECONDS, value = 10)
    @Retry(delayUnit = ChronoUnit.SECONDS, maxRetries = 2, delay = 1)
    @Fallback(fallbackMethod = "employeeFallback")
    @CircuitBreaker(requestVolumeThreshold = 4)
    Employee getEmployee(@PathParam("id") String id);

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timeout(unit = ChronoUnit.SECONDS, value = 10)
    @Retry(delayUnit = ChronoUnit.SECONDS, maxRetries = 2, delay = 1)
    @Fallback(fallbackMethod = "employeeWithBodyFallback")
    @CircuitBreaker(requestVolumeThreshold = 4)
    Employee updateEmployee(@PathParam("id") String id, Employee department);

    @DELETE
    @Path("{id}")
    @Timeout(unit = ChronoUnit.SECONDS, value = 10)
    @Retry(delayUnit = ChronoUnit.SECONDS, maxRetries = 2, delay = 1)
    @Fallback(fallbackMethod = "deleteFallback")
    @CircuitBreaker(requestVolumeThreshold = 4)
    Void deleteEmployee(@PathParam("id") String id);

    default Employee employeeFallback(String id) throws ClientOneException {
        throw new ClientOneException();
    }
    default Employee employeeWithBodyFallback(String id, Employee employee) throws ClientOneException {
        throw new ClientOneException();
    }
    default List<Employee> employeeListFallback() throws ClientOneException {
        throw new ClientOneException();
    }
    default Void deleteFallback(String id) throws ClientOneException {
        throw new ClientOneException();
    }
}