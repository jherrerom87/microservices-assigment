package com.vodafone;

import com.vodafone.client.EmployeeClient;
import com.vodafone.entity.Employee;
import com.vodafone.resource.EmployeeResource;
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
@TestHTTPEndpoint(EmployeeResource.class)
public class EmployeeResourceTest {

    @InjectMock
    @RestClient
    EmployeeClient employeeClient;

    private static Employee employee1;
    private static Employee employee2;

    private static final String EMPLOYEE_1 = "employee1";
    private static final String EMPLOYEE_2 = "employee2";

    @BeforeAll
    public static void setup() {
        employee1 = new Employee();
        employee1.setId(EMPLOYEE_1);
        employee1.setName(EMPLOYEE_1);

        employee2 = new Employee();
        employee2.setId(EMPLOYEE_2);
        employee2.setName(EMPLOYEE_2);
    }

    @Test
    public void testGetEmployeeList() {
        Mockito.when(employeeClient.getAllEmployees()).thenReturn(List.of(employee1, employee2));
        given()
                .when().get()
                .then()
                .statusCode(200)
                .body(containsString(EMPLOYEE_1), containsString(EMPLOYEE_2));
    }

    @Test
    public void testGetEmployee() {
        String employeeId = employee1.getId();
        Mockito.when(employeeClient.getEmployee(employeeId)).thenReturn(employee1);
        given()
                .pathParam("employeeId", employeeId)
                .when().get("/{employeeId}")
                .then()
                .statusCode(200)
                .body(containsString(EMPLOYEE_1), not(containsString(EMPLOYEE_2)));
    }


    @Test
    public void testUpdateEmployee() {
        String employeeId = employee1.getId();
        Mockito.when(employeeClient.updateEmployee(Mockito.any(), Mockito.any())).thenReturn(employee2);
        given()
                .pathParam("employeeId", employeeId)
                .contentType(ContentType.JSON)
                .body(employee1)
                .when().put("/{employeeId}")
                .then()
                .statusCode(200)
                .body(containsString(EMPLOYEE_2), not(containsString(EMPLOYEE_1)));
    }

    @Test
    public void testDeleteDepartment() {
        String employeeId = employee1.getId();
        Mockito.doNothing().when(employeeClient).deleteEmployee(employeeId);
        given()
                .pathParam("employeeId", employeeId)
                .when().delete("/{employeeId}")
                .then()
                .statusCode(204)
                .body(emptyString());
    }

}
