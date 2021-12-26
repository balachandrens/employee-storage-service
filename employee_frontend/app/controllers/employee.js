import Controller from '@ember/controller';
import { inject as service } from '@ember/service';
import { action } from '@ember/object';
import attr from 'ember-data/attr';

export default class EmployeeController extends Controller {
  @service store;
  @attr('employee') employeeObj;
  @action
  async insertEmployee() {
      
      var insertEmployee = this.model;
      let employee = await this.store.createRecord('employee', insertEmployee);
      let res = await employee.save().then(function(employee) {
            alert("Employee created id : " + employee.id)
            return 0;
        }).catch(function(error){
            alert("Employee creatation failed")
            return 1;
        });
        if(res == 0)
            this.model = null;
  }
  @action
  updateEmployee() {
      var updatedEmployee = this.model;
      let res = this.store.findRecord('employee', updatedEmployee.id).then(function(employee) {
        employee.name = updatedEmployee.name;
        employee.surname = updatedEmployee.surname
        employee.email = updatedEmployee.email
        employee.address = updatedEmployee.address
        employee.salary = updatedEmployee.salary        
        let res = employee.save().then(function(updatedEmployee) {
            alert("Employee info updated")
        }).catch(function(error){
            alert("Employee info update failed")
        });
      }); 

  }
  @action
  deleteEmployee() {
    var employee = this.model;
    let employeeToDelete = this.store.peekRecord('employee', employee.id);
    if(employeeToDelete) {
        let res = employeeToDelete.destroyRecord();
        this.model = null;
        alert("Employee deleted")
        return;
    }
    alert("Employe not found in system")
    this.model = null;
    return;
  }
}
