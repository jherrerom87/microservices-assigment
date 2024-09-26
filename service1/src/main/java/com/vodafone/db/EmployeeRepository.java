package com.vodafone.db;

import com.vodafone.entity.Department;
import com.vodafone.entity.Employee;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.bson.types.ObjectId;

import java.util.Optional;

@ApplicationScoped
public class EmployeeRepository implements ReactivePanacheMongoRepository<Employee> {

    public Uni<Employee> updateEmployee(
            String id,
            Employee updatedEmployee,
            DepartmentRepository departmentRepository
    ) {
        Uni<Employee> employeeUni = findById(new ObjectId(id));

        return employeeUni.call(employee -> {
            employee.setName(updatedEmployee.getName());
            Uni<Department> departmentUni = departmentRepository.findById(employee.getDepartmentId());
            return departmentUni.call(department -> {
                if (department != null) {
                    Optional<Employee> emp = department.getEmployees().stream()
                            .filter(comment1 -> comment1.equals(employee)).findFirst();
                    emp.ifPresent(value -> value.setName(updatedEmployee.getName()));
                }
                return Uni.createFrom().item(employee);
            }).chain(departmentRepository::persistOrUpdate);
        }).chain(employee -> {
            if (employee == null) {
                throw new NotFoundException();
            }
            return persistOrUpdate(employee);
        });
    }

    public Uni<Void> deleteEmployee(
            String employeeId,
            DepartmentRepository departmentRepository
    ) {
        Uni<Employee> employeeUni = findById(new ObjectId(employeeId));

        return employeeUni.call(employee -> {

            Uni<Department> uni = departmentRepository.findById(employee.getDepartmentId());
            return uni.call(department -> {
                if (department != null) {
                    department.getEmployees().remove(employee);
                }
                return Uni.createFrom().item(employee);
            }).chain(departmentRepository::persistOrUpdate);
        }).chain(employee -> {
            if (employee == null) {
                throw new NotFoundException();
            }
            return delete(employee);
        });
    }

    public Multi<Employee> getAllEmployees() {
        return streamAll();
    }

    public Multi<Employee> getAllEmployeesByDepartmentId(String departmentId) {
        return stream("departmentId", new ObjectId(departmentId));
    }
}
