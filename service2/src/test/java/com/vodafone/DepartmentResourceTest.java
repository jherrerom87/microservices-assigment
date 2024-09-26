package com.vodafone;

import com.vodafone.client.DepartmentClient;
import com.vodafone.entity.Department;
import com.vodafone.entity.Employee;
import com.vodafone.resource.DepartmentResource;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestHTTPEndpoint(DepartmentResource.class)
public class DepartmentResourceTest {

    @InjectMock
    @RestClient
    DepartmentClient departmentClient;

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
        employee1 = new Employee();
        employee1.setId(EMPLOYEE_1);
        employee1.setName(EMPLOYEE_1);
        employee1.setDepartmentId(DEPARTMENT_1);

        employee2 = new Employee();
        employee2.setId(EMPLOYEE_2);
        employee2.setName(EMPLOYEE_2);
        employee2.setDepartmentId(DEPARTMENT_2);

        department1 = new Department();
        department1.setId(DEPARTMENT_1);
        department1.setName(DEPARTMENT_1);
        department1.setEmployees(List.of(employee1));

        department2 = new Department();
        department2.setId(DEPARTMENT_2);
        department2.setName(DEPARTMENT_2);
        department2.setEmployees(List.of(employee2));
    }

    @Test
    public void testGetDepartmentList() {
        Mockito.when(departmentClient.getAllDepartments()).thenReturn(List.of(department1, department2));
        given()
                .when().get()
                .then()
                .statusCode(200)
                .body(containsString(DEPARTMENT_1), containsString(DEPARTMENT_2));
    }

    @Test
    public void testGetDepartment() {
        String department1Id = department1.getId();
        Mockito.when(departmentClient.getDepartment(department1Id)).thenReturn(department1);
        given()
                .pathParam("departmentId", department1Id)
                .when().get("/{departmentId}")
                .then()
                .statusCode(200)
                .body(containsString(DEPARTMENT_1), not(containsString(DEPARTMENT_2)));
    }

    @Test
    public void testGetEmployeeByDepartment() {
        String department1Id = department1.getId();
        Mockito.when(departmentClient.getEmployeesByDepartment(department1Id)).thenReturn(List.of(employee1));
        given()
                .pathParam("departmentId", department1Id)
                .when().get("/{departmentId}/employee")
                .then()
                .statusCode(200)
                .body(containsString(EMPLOYEE_1), not(containsString(EMPLOYEE_2)));
    }

    @Test
    public void testUpdateDepartment() {
        String department1Id = department1.getId();
        Mockito.when(departmentClient.updateDepartment(Mockito.any(), Mockito.any())).thenReturn(department2);
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
        Mockito.when(departmentClient.createDepartment(Mockito.any(Department.class))).thenReturn(department2);
        given()
                .contentType(ContentType.JSON)
                .body(department2)
                .when().post()
                .then()
                .statusCode(200)
                .body(containsString(DEPARTMENT_2), not(containsString(DEPARTMENT_1)));
    }

    @Test
    public void testAddEmployeeToDepartment() {
        String department1Id = department1.getId();
        Mockito.when(departmentClient.addEmployeeToDepartment(Mockito.any(), Mockito.any())).thenAnswer(val -> {
            department1.setEmployees(List.of(employee1, employee2));
            return department1;
        });
        given()
                .pathParam("departmentId", department1Id)
                .contentType(ContentType.JSON)
                .body(employee2)
                .when().post("/{departmentId}/employee")
                .then()
                .statusCode(200)
                .body(containsString(EMPLOYEE_1), containsString(EMPLOYEE_2));
    }

    @Test
    public void testDeleteDepartment() {
        String department1Id = department1.getId();
        Mockito.doNothing().when(departmentClient).deleteDepartment(Mockito.any());
        given()
                .pathParam("departmentId", department1Id)
                .when().delete("/{departmentId}")
                .then()
                .statusCode(204)
                .body(emptyString());
    }

}
