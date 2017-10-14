package com.besafx.app.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "employeeSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "EMPLOYEE_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "employeeSequenceGenerator")
    private Long id;

    private Integer code;

    private Double salary;

    @JoinColumn(name = "person")
    @OneToOne
    private Person person;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    private List<Salary> salaries = new ArrayList<>();

    public Double getPaid(){
        try{
            return this.salaries.stream().mapToDouble(salary -> salary.getReceipt().getAmountNumber()).sum();
        }catch (Exception ex){
            return null;
        }
    }

    @JsonCreator
    public static Employee Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Employee employee = mapper.readValue(jsonString, Employee.class);
        return employee;
    }
}
