package com.vodafone;

import com.vodafone.db.DepartmentRepository;
import com.vodafone.db.EmployeeRepository;
import com.vodafone.entity.Department;
import com.vodafone.entity.Employee;
import com.vodafone.resource.EmployeeResource;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestHTTPEndpoint(EmployeeResource.class)
public class EmployeeResourceTest {

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
    public void testGetEmployeeList() {
        Multi<Employee> multi = Multi.createFrom().items(Stream.of(employee1, employee2));
        Mockito.when(employeeRepository.getAllEmployees()).thenReturn(multi);
        given()
                .when().get()
                .then()
                .statusCode(200)
                .body(containsString(EMPLOYEE_1), containsString(EMPLOYEE_2));
    }

    @Test
    public void testGetEmployee() {
        String employeeId = employee1.getId().toString();
        Uni<Employee> uni = Uni.createFrom().item(employee1);
        Mockito.when(employeeRepository.findById(Mockito.any())).thenReturn(uni);
        given()
                .pathParam("employeeId", employeeId)
                .when().get("/{employeeId}")
                .then()
                .statusCode(200)
                .body(containsString(EMPLOYEE_1), not(containsString(EMPLOYEE_2)));
    }



    @Test
    public void testUpdateEmployee() {
        String employeeId = employee1.getId().toString();
        Uni<Employee> uni = Uni.createFrom().item(employee2);
        Mockito.when(employeeRepository.updateEmployee(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(uni);
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
        String employeeId = employee1.getId().toString();
        Uni<Void> uni = Uni.createFrom().voidItem();
        Mockito.when(departmentRepository.deleteDepartment(Mockito.any(), Mockito.any())).thenReturn(uni);
        given()
                .pathParam("employeeId", employeeId)
                .when().delete("/{employeeId}")
                .then()
                .statusCode(204)
                .body(emptyString());
    }

}
