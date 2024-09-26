package com.vodafone.resource;

import com.vodafone.db.DepartmentRepository;
import com.vodafone.db.EmployeeRepository;
import com.vodafone.entity.Employee;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

@Path("/employee")
public class EmployeeResource {

    @Inject
    DepartmentRepository departmentRepository;

    @Inject
    EmployeeRepository employeeRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Return a list of employees",
            description = "It returns the full list of employees without any filters")
    @APIResponses({@APIResponse(
            name = "Success Response",
            responseCode = "200",
            description = "json with the employees list")})
    public Multi<Employee> getEmployeeList() {
        return employeeRepository.getAllEmployees();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Return a employees",
            description = "It returns a employee given an id")
    @APIResponses({@APIResponse(
            name = "Success Response",
            responseCode = "200",
            description = "json with the employee")})
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Employee> getEmployee(@PathParam("id") String id) {
        return employeeRepository.findById(new ObjectId(id));
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a employee",
            description = "It delete a employee given an id")
    @APIResponses({@APIResponse(
            name = "Success Response",
            responseCode = "200",
            description = "empty response")})
    public Uni<Void> deleteEmployee(@PathParam("id") String id) {
        return employeeRepository.deleteEmployee(id, departmentRepository);
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update a employee",
            description = "It update a employee given an id and the updated employee data")
    @APIResponses({@APIResponse(
            name = "Success Response",
            responseCode = "200",
            description = "json with the updated employee")})
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Employee> updateEmployee(@PathParam("id") String id, Employee updatedEmployee) {
        return employeeRepository.updateEmployee(id, updatedEmployee, departmentRepository);
    }
}
