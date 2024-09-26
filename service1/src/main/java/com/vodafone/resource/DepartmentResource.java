package com.vodafone.resource;

import com.vodafone.db.DepartmentRepository;
import com.vodafone.db.EmployeeRepository;
import com.vodafone.entity.Department;
import com.vodafone.entity.Employee;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.net.URI;

@Path("/department")
public class DepartmentResource {

    @Inject
    DepartmentRepository departmentRepository;

    @Inject
    EmployeeRepository employeeRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Return a list of departments",
            description = "It returns the full list of departments")
    @APIResponses({@APIResponse(
            name = "Success Response",
            responseCode = "200",
            description = "json with the departments list")})
    public Multi<Department> getDepartmentList() {
        return departmentRepository.getAllDepartments();
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
    public Uni<Department> getDepartment(@PathParam("id") String id) {
        return departmentRepository.findById(new ObjectId(id));
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a department",
            description = "It delete a department and his employees given an id")
    @APIResponses({@APIResponse(
            name = "Success Response",
            responseCode = "200",
            description = "empty response")})
    public Uni<Void> deleteDepartment(@PathParam("id") String id) {
        return departmentRepository.deleteDepartment(id, employeeRepository);
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
    public Uni<Department> updateDepartment(@PathParam("id") String id, Department updatedDepartment) {
        return departmentRepository.updateDepartment(id, updatedDepartment);
    }

    @POST
    @Operation(summary = "Create a department",
            description = "It creates a department")
    @APIResponses({@APIResponse(
            name = "Success Response",
            responseCode = "200",
            description = "json with the created department")})
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> createDepartment(Department newDepartment) {
        return departmentRepository.persist(newDepartment).map(v ->
                Response.created(URI.create("/department/" + v.getId().toString()))
                        .entity(v).build());
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
    public Multi<Employee> getEmployeesByDepartment(@PathParam("id") String id) {
        return employeeRepository.getAllEmployeesByDepartmentId(id);
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
    public Uni<Response> addEmployeeToDepartment(@PathParam("id") String id, Employee employee) {
        return departmentRepository.addEmployeeToDepartment(employee, id, employeeRepository)
                .map(v -> Response.created(URI.create("/department/" + v.getId().toString() + "/employee"))
                        .entity(v).build());
    }
}
