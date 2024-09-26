package com.vodafone.resource;

import com.vodafone.client.EmployeeClient;
import com.vodafone.entity.Employee;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@Path("/employee")
public class EmployeeResource {

    @Inject
    @RestClient
    EmployeeClient employeeClient;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Return a list of employees",
            description = "It returns the full list of employees without any filters")
    @APIResponses({@APIResponse(
            name = "Success Response",
            responseCode = "200",
            description = "json with the employees list")})
    public List<Employee> getEmployeeList() {
        return employeeClient.getAllEmployees();
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
    public Employee getEmployee(@PathParam("id") String id) {
        return employeeClient.getEmployee(id);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a employee",
            description = "It delete a employee given an id")
    @APIResponses({@APIResponse(
            name = "Success Response",
            responseCode = "200",
            description = "empty response")})
    public Void deleteEmployee(@PathParam("id") String id) {
        return employeeClient.deleteEmployee(id);
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
    public Employee updateEmployee(@PathParam("id") String id, Employee updatedEmployee) {
        return employeeClient.updateEmployee(id, updatedEmployee);
    }
}
