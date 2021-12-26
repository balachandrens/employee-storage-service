import Model, { attr } from '@ember-data/model';

export default class EmployeeModel extends Model {
  @attr('string') name;
  @attr('string') surname;
  @attr('string') email;
  @attr('string') address;
  @attr('string') salary;
  @attr('number') companyId;
}
