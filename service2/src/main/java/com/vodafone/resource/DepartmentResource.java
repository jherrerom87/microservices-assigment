package com.vodafone.resource;

import com.vodafone.client.DepartmentClient;
import com.vodafone.entity.Department;
import com.vodafone.entity.Employee;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@Path("/department")
public class DepartmentResource {

    @Inject
    @RestClient
    DepartmentClient departmentClient;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Return a list of departments",
            description = "It returns the full list of departments")
    @APIResponses({@APIResponse(
            name = "Success Response",
            responseCode = "200",
            description = "json with the departments list")})
    public List<Department> getEmployeeList() {
        return departmentClient.getAllDepartments();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Return a department",
            description = "It returns a department given an id")
    @APIResponses({@APIResponse(
            name = "Success Response",
            responseCode = "200",
            description = "json with the department")})
    @Produces(MediaType.APPLICATION_JSON)
    public Department getDepartment(@PathParam("id") String id) {
        return departmentClient.getDepartment(id);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a department",
            description = "It delete a department and his employees given an id")
    @APIResponses({@APIResponse(
            name = "Success Response",
            responseCode = "200",
            description = "empty response")})
    public Void deleteDepartment(@PathParam("id") String id) {
        return departmentClient.deleteDepartment(id);
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update a department",
            description = "It update a department given an id and the updated department data")
    @APIResponses({@APIResponse(
            name = "Success Response",
            responseCode = "200",
            description = "json with the updated employee")})
    @Consumes(MediaType.APPLICATION_JSON)
    public Department updateDepartment(@PathParam("id") String id, Department updatedDepartment) {
        return departmentClient.updateDepartment(id, updatedDepartment);
    }

    @POST
    @Operation(summary = "Create a department",
            description = "It creates a department")
    @APIResponses({@APIResponse(
            name = "Success Response",
            responseCode = "200",
            description = "json with the created department")})
    @Consumes(MediaType.APPLICATION_JSON)
    public Department createDepartment(Department newDepartment) {
        return departmentClient.createDepartment(newDepartment);
    }

    @GET
    @Path("/{id}/employee")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Return a list of employees",
            description = "It returns a list of employees given a department ID")
    @APIResponses({@APIResponse(
            name = "Success Response",
            responseCode = "200",
            description = "json with the employees list")})
    public List<Employee> getEmployeesByDepartment(@PathParam("id") String id) {
        return departmentClient.getEmployeesByDepartment(id);
    }

    @POST
    @Path("/{id}/employee")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create a employee",
            description = "It creates a employee assigned to a department")
    @APIResponses({@APIResponse(
            name = "Success Response",
            responseCode = "200",
            description = "json with the created employee")})
    public Department addEmployeeToDepartment(@PathParam("id") String id, Employee employee) {
        return departmentClient.addEmployeeToDepartment(id, employee);
    }
}
