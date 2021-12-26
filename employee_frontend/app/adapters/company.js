import RESTAdapter from '@ember-data/adapter/rest';

export default class CompanyAdapter extends RESTAdapter {
  host = 'http://localhost:8080/api/v1/companies';
  findAverageSalary(companyId) {
    const url = `${this.host}/${companyId}/average-salary`;
    return this.ajax(url, 'GET');
  }
}