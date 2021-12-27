# Employee Storage Service 

The project is consist of REST API and sample UI for CURD operations on employee resource.


## Available Endpoints

For details on body of endpoints please start the backend app and go through swagger docs available in below URI

```bash
/swagger-ui/index.html
```


**1. Add Employee**

Api to add a new employee for a company
```bash
Method : POST
URI : /api/v1/employees
```

**2. Get all Employees**

Api to get all employees of a company
```bash
Method : GET
URI : /api/v1/employees?companyId=<company-id> 
```

**3. Get Employee details by Id**

Api to get a particular employee by id
```bash
Method : GET
URI : /api/v1/employees/{id} 
```

**4. Update details Employee by Id**

Api to update particular employee details by id
```bash
Method : PUT
URI : /api/v1/employees/{id} 
```

**5. Delete a employee**

Api to remove an employee
```bash
Method : DELETE
URI : /api/v1/employees/{id}
```

**6. Average Salary for a Company**

Api to find average salary of a company
```bash
Method : GET
URI : /api/v1/companies/{companyId}/average-salary
```

## Steps to setup

**1. Clone the repository**

```bash
git clone https://github.com/balachandrens/employee-storage-service.git
```

**2. Run the backend app**

Type the following command from the root directory of the project to run the app

```bash
cd employeeservice
mvn spring-boot:run
```

**3. Run the frontend app**

Type the following command from the root directory of the project to run the app

```bash
cd employee_frontend
npm install
ember s
```

**4. Run only backend tests**

Type the following command from the root directory of the project to run tests

```bash
cd employeeservice
mvn clean test -DskipTests=false
```

**5. Package the backend app**

Type the following command from the root directory of the project to package and run the app

```bash
cd employeeservice
mvn clean package
java -jar target/employeeservice-0.0.1-SNAPSHOT.jar
```

