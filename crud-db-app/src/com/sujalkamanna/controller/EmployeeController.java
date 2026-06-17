package com.sujalkamanna.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sujalkamanna.dao.EmployeeDAO;
import com.sujalkamanna.dao.EmployeeDAOImpl;
import com.sujalkamanna.model.Employee;

public class EmployeeController extends HttpServlet {

```
private static final long serialVersionUID = 1L;

private RequestDispatcher dispatcher;
private EmployeeDAO employeeDAO;

public EmployeeController() {
    employeeDAO = new EmployeeDAOImpl();
}

@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    String action = request.getParameter("action");

    if (action == null) {
        action = "LIST";
    }

    switch (action) {

        case "LIST":
            listEmployee(request, response);
            break;

        case "EDIT":
            getSingleEmployee(request, response);
            break;

        case "DELETE":
            deleteEmployee(request, response);
            break;

        default:
            listEmployee(request, response);
            break;
    }
}

private void deleteEmployee(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    String id = request.getParameter("id");

    if (employeeDAO.delete(Integer.parseInt(id))) {
        request.setAttribute("NOTIFICATION", "Employee Deleted Successfully!");
    }

    listEmployee(request, response);
}

private void getSingleEmployee(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    String id = request.getParameter("id");

    Employee employee = employeeDAO.get(Integer.parseInt(id));

    request.setAttribute("employee", employee);

    dispatcher = request.getRequestDispatcher("/views/employee-form.jsp");

    dispatcher.forward(request, response);
}

private void listEmployee(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    List<Employee> employeeList = employeeDAO.get();

    request.setAttribute("list", employeeList);

    dispatcher = request.getRequestDispatcher("/views/employee-list.jsp");

    dispatcher.forward(request, response);
}

@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    String id = request.getParameter("id");

    Employee employee = new Employee();

    employee.setName(request.getParameter("name"));
    employee.setDepartment(request.getParameter("department"));
    employee.setDob(request.getParameter("dob"));

    if (id == null || id.trim().isEmpty()) {

        if (employeeDAO.save(employee)) {
            request.setAttribute("NOTIFICATION", "Employee Saved Successfully!");
        }

    } else {

        employee.setId(Integer.parseInt(id));

        if (employeeDAO.update(employee)) {
            request.setAttribute("NOTIFICATION", "Employee Updated Successfully!");
        }
    }

    listEmployee(request, response);
}
```

}
