import DS from 'ember-data';

export default DS.RESTAdapter.extend({
  host: 'http://localhost:8080/api/v1',
  headers: {
    'Accept': 'application/json',
    'Content-Type': 'application/json',
  }
});
