import Route from '@ember/routing/route';
import { inject as service } from '@ember/service';


export default class CompanyRoute extends Route {
  @service store;
  async model(params) {
    let employeesList = await this.store.query('employee', { companyId: params.company_id })
    let res = await this.store.adapterFor('company').findAverageSalary(params.company_id);
    return {employees : employeesList, companyId : params.company_id, averageSalary : res.averageSalary};
  }
}

