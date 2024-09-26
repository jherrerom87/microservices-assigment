package com.vodafone.client;

import com.vodafone.entity.Department;
import com.vodafone.entity.Employee;
import com.vodafone.exceptions.ClientOneException;
import com.vodafone.request.Endpoint3Request;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.time.temporal.ChronoUnit;
import java.util.List;

@RegisterRestClient(baseUri = "http://localhost:8081/department")
public interface DepartmentClient {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(unit = ChronoUnit.SECONDS, value = 10)
    @Retry(delayUnit = ChronoUnit.SECONDS, maxRetries = 2, delay = 1)
    @Fallback(fallbackMethod = "departmentListFallback")
    @CircuitBreaker(requestVolumeThreshold = 4)
    List<Department> getAllDepartments();

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(unit = ChronoUnit.SECONDS, value = 10)
    @Retry(delayUnit = ChronoUnit.SECONDS, maxRetries = 2, delay = 1)
    @Fallback(fallbackMethod = "departmentFallback")
    @CircuitBreaker(requestVolumeThreshold = 4)
    Department getDepartment(@PathParam("id") String id);

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timeout(unit = ChronoUnit.SECONDS, value = 10)
    @Retry(delayUnit = ChronoUnit.SECONDS, maxRetries = 2, delay = 1)
    @Fallback(fallbackMethod = "departmentWithBodyAndIdFallback")
    @CircuitBreaker(requestVolumeThreshold = 4)
    Department updateDepartment(@PathParam("id") String id, Department department);

    @DELETE
    @Path("{id}")
    @Timeout(unit = ChronoUnit.SECONDS, value = 10)
    @Retry(delayUnit = ChronoUnit.SECONDS, maxRetries = 2, delay = 1)
    @Fallback(fallbackMethod = "deleteFallback")
    @CircuitBreaker(requestVolumeThreshold = 4)
    Void deleteDepartment(@PathParam("id") String id);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timeout(unit = ChronoUnit.SECONDS, value = 10)
    @Retry(delayUnit = ChronoUnit.SECONDS, maxRetries = 2, delay = 1)
    @Fallback(fallbackMethod = "departmentWithBodyFallback")
    @CircuitBreaker(requestVolumeThreshold = 4)
    Department createDepartment(Department department);

    @POST
    @Path("{id}/employee")
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(unit = ChronoUnit.SECONDS, value = 10)
    @Retry(delayUnit = ChronoUnit.SECONDS, maxRetries = 2, delay = 1)
    @Fallback(fallbackMethod = "departmentWithEmployeeBodyFallback")
    @CircuitBreaker(requestVolumeThreshold = 4)
    Department addEmployeeToDepartment(@PathParam("id") String id, Employee employee);

    @GET
    @Path("{id}/employee")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timeout(unit = ChronoUnit.SECONDS, value = 10)
    @Retry(delayUnit = ChronoUnit.SECONDS, maxRetries = 2, delay = 1)
    @Fallback(fallbackMethod = "employeeListFallback")
    @CircuitBreaker(requestVolumeThreshold = 4)
    List<Employee> getEmployeesByDepartment(@PathParam("id") String id);

    default Department departmentFallback(String id) throws ClientOneException {
        throw new ClientOneException();
    }
    default Department departmentWithBodyFallback(Department department) throws ClientOneException {
        throw new ClientOneException();
    }
    default Department departmentWithBodyAndIdFallback(String id, Department department) throws ClientOneException {
        throw new ClientOneException();
    }
    default Department departmentWithEmployeeBodyFallback(String id, Employee department) throws ClientOneException {
        throw new ClientOneException();
    }
    default List<Department> departmentListFallback() throws ClientOneException {
        throw new ClientOneException();
    }
    default List<Employee> employeeListFallback(String id) throws ClientOneException {
        throw new ClientOneException();
    }
    default Void deleteFallback(String id) throws ClientOneException {
        throw new ClientOneException();
    }
}