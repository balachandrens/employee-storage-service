import Route from '@ember/routing/route';
import { inject as service } from '@ember/service';
import { action } from '@ember/object';
import employee from '../adapters/employee';
import attr from 'ember-data/attr';

export default class EmployeeRoute extends Route {
  @service store;
  model(params) {
    if(params.emp_id != 'new') {
      var employeeObj = this.store.findRecord('employee', params.emp_id).then(function(employee){
        return employee;
      }).catch(function(error){
        alert("Employe not found")
      });
      this.controllerFor('employee').set('empoloyeeObj', employeeObj);
      employeeObj.companyId = params.company_id;
      return employeeObj;
    }
    var employeeObj = new employee();
    employeeObj.companyId = params.company_id;
    return employeeObj;
  }
}
