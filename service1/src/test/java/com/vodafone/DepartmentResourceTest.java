package com.vodafone;

import com.vodafone.db.DepartmentRepository;
import com.vodafone.db.EmployeeRepository;
import com.vodafone.entity.Department;
import com.vodafone.entity.Employee;
import com.vodafone.resource.DepartmentResource;
import io.quarkus.builder.json.JsonObject;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.json.JsonValue;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestHTTPEndpoint(DepartmentResource.class)
public class DepartmentResourceTest {

    @InjectMock
    DepartmentRepository departmentRepository;

    @InjectMock
    EmployeeRepository employeeRepository;

    private static Department department1;
    private static Department department2;
    private static Employee employee1;
    private static Employee employee2;

    private static final String EMPLOYEE_1 = "employee1";
    private static final String EMPLOYEE_2 = "employee2";
    private static final String DEPARTMENT_1 = "department1";
    private static final String DEPARTMENT_2 = "department2";

    @BeforeAll
    public static void setup() {
        ObjectId department1Id = new ObjectId();
        ObjectId department2Id = new ObjectId();
        employee1 = new Employee();
        employee1.setId(new ObjectId());
        employee1.setName(EMPLOYEE_1);
        employee1.setDepartmentId(department1Id);

        employee2 = new Employee();
        employee2.setId(new ObjectId());
        employee2.setName(EMPLOYEE_2);
        employee2.setDepartmentId(department2Id);

        department1 = new Department();
        department1.setId(department1Id);
        department1.setName(DEPARTMENT_1);
        department1.setEmployees(List.of(employee1));

        department2 = new Department();
        department2.setId(department2Id);
        department2.setName(DEPARTMENT_2);
        department2.setEmployees(List.of(employee2));
    }

    @Test
    public void testGetDepartmentList() {
        Multi<Department> multi = Multi.createFrom().items(Stream.of(department1, department2));
        Mockito.when(departmentRepository.getAllDepartments()).thenReturn(multi);
        given()
                .when().get()
                .then()
                .statusCode(200)
                .body(containsString(DEPARTMENT_1), containsString(DEPARTMENT_2));
    }

    @Test
    public void testGetDepartment() {
        String department1Id = department1.getId().toString();
        Uni<Department> uni = Uni.createFrom().item(department1);
        Mockito.when(departmentRepository.findById(Mockito.any())).thenReturn(uni);
        given()
                .pathParam("departmentId", department1Id)
                .when().get("/{departmentId}")
                .then()
                .statusCode(200)
                .body(containsString(DEPARTMENT_1), not(containsString(DEPARTMENT_2)));
    }

    @Test
    public void testGetEmployeeByDepartment() {
        String department1Id = department1.getId().toString();
        Multi<Employee> multi = Multi.createFrom().items(Stream.of(employee1));
        Mockito.when(employeeRepository.getAllEmployeesByDepartmentId(department1Id)).thenReturn(multi);
        given()
                .pathParam("departmentId", department1Id)
                .when().get("/{departmentId}/employee")
                .then()
                .statusCode(200)
                .body(containsString(EMPLOYEE_1), not(containsString(EMPLOYEE_2)));
    }

    @Test
    public void testUpdateDepartment() {
        String department1Id = department1.getId().toString();
        Uni<Department> uni = Uni.createFrom().item(department2);
        Mockito.when(departmentRepository.updateDepartment(Mockito.any(), Mockito.any())).thenReturn(uni);
        given()
                .pathParam("departmentId", department1Id)
                .contentType(ContentType.JSON)
                .body(department1)
                .when().put("/{departmentId}")
                .then()
                .statusCode(200)
                .body(containsString(DEPARTMENT_2), not(containsString(DEPARTMENT_1)));
    }


    @Test
    public void testCreateDepartment() {
        Uni<Department> uni = Uni.createFrom().item(department2);
        Mockito.when(departmentRepository.persist(Mockito.any(Department.class))).thenReturn(uni);
        given()
                .contentType(ContentType.JSON)
                .body(department2)
                .when().post()
                .then()
                .statusCode(201)
                .body(containsString(DEPARTMENT_2), not(containsString(DEPARTMENT_1)));
    }

    @Test
    public void testAddEmployeeToDepartment() {
        String department1Id = department1.getId().toString();
        Mockito.when(departmentRepository.addEmployeeToDepartment(Mockito.any(), Mockito.any(), Mockito.any())).thenAnswer(val -> {
            department1.setEmployees(List.of(employee1, employee2));
            return Uni.createFrom().item(department1);
        });
        given()
                .pathParam("departmentId", department1Id)
                .contentType(ContentType.JSON)
                .body(employee2)
                .when().post("/{departmentId}/employee")
                .then()
                .statusCode(201)
                .body(containsString(EMPLOYEE_1), containsString(EMPLOYEE_2));
    }

    @Test
    public void testDeleteDepartment() {
        String department1Id = department1.getId().toString();
        Uni<Void> uni = Uni.createFrom().voidItem();
        Mockito.when(departmentRepository.deleteDepartment(Mockito.any(), Mockito.any())).thenReturn(uni);
        given()
                .pathParam("departmentId", department1Id)
                .when().delete("/{departmentId}")
                .then()
                .statusCode(204)
                .body(emptyString());
    }

}
