import Vue from 'vue';
import VueRouter from 'vue-router';
import {store} from './store';
import Field from './components/Field.vue';

Vue.use(VueRouter);

const routes = [

  {
    name: 'field',
    path: '/field',
    component: Field,
  },
/*  {
    path: '*',
    async beforeEnter(from, to, next) {
      console.log(1)
      const isLogged = await store.dispatch('login/checkAuthorization')
      if (isLogged === true) {
        next();
      }
      else {
        next('field')
      }
    }
  },*/
];

export const router = new VueRouter({
  routes,
  mode: 'history'
})
