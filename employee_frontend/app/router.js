import EmberRouter from '@ember/routing/router';
import config from 'employee-frontend/config/environment';

export default class Router extends EmberRouter {
  location = config.locationType;
  rootURL = config.rootURL;
}

Router.map(function () {
  this.route('company', { path: '/company/:company_id' });
  this.route('not-found', { path: '/*path' });
  this.route('employee', { path: '/employee/:emp_id/:company_id' });
});
