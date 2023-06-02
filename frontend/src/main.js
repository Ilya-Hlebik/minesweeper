import Vue from 'vue';
import {BootstrapVue, IconsPlugin} from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import {store} from './store';
import {router} from './routes.js';
import App from './App.vue';
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';

Vue.component('app', require('./App.vue').default)
Vue.use(BootstrapVue);
Vue.use(IconsPlugin);
Vue.use(Antd);




const vm = new Vue({
  el: '#app',
  store,
  router,
  render: (h) => h(App)
});
export {vm};
