package com.vodafone.db;

import com.vodafone.entity.Department;
import com.vodafone.entity.Employee;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import org.bson.types.ObjectId;

import java.util.List;

@ApplicationScoped
public class DepartmentRepository implements ReactivePanacheMongoRepository<Department> {

    public Uni<Department> updateDepartment(String id, Department updatedDepartment) {
        Uni<Department> departmentUni = findById(new ObjectId(id));
        return departmentUni
                .onItem().transform(department -> {
                    department.setName(updatedDepartment.getName());
                    return department;
                }).call(this::persistOrUpdate);
    }


    public Uni<Department> addEmployeeToDepartment(
            Employee employee,
            String departmentId,
            EmployeeRepository employeeRepository
    ) {
        Uni<Department> departmentUni = findById(new ObjectId(departmentId));
        return departmentUni.onItem().transform(department -> {
            if (department.getEmployees() == null) {
                department.setEmployees(List.of(employee));
            } else {
                department.getEmployees().add(employee);
            }
            employee.setDepartmentId(new ObjectId(departmentId));
            return department;
        }).call(department -> employeeRepository.persist(employee).chain(() -> persistOrUpdate(department)));
    }

    public Uni<Void> deleteDepartment(
            String departmentId,
            EmployeeRepository employeeRepository
    ) {
        Uni<Department> departmentUni = findById(new ObjectId(departmentId));
        Multi<Employee> employeeMulti = employeeRepository.getAllEmployeesByDepartmentId(departmentId);

        return departmentUni.call(department -> employeeMulti.onItem().call(employeeRepository::delete)
                .collect().asList()).chain(department -> {
            if (department == null) {
                throw new NotFoundException();
            }
            return delete(department);
        });
    }

    public Multi<Department> getAllDepartments() {
        return streamAll();
    }
}
