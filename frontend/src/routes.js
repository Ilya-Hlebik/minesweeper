import Vue from 'vue';
import VueRouter from 'vue-router';
import {store} from './store';
import Field from './components/Field';

Vue.use(VueRouter);

const routes = [

  {
    path: '/game',
    async beforeEnter(to, from, next) {
      let gameId = await store.dispatch('game/initiateBoard');
      if (gameId !== null) {
        next('/game/' + gameId);
      } else {
        next();
      }
    }
  },
  {
    path: '/game/:id',
    async  beforeEnter  (to, from, next)  {
      await store.dispatch('game/getGameById', to.params.id);
      next();
    },
    component: Field,
    props: {boardInitiated: true}
  },
  {
    name: 'menu',
    path: '/menu',
    component: Field,
  },
  {
    path: '*',
    redirect: {name: 'menu'}

  },
];

export const router = new VueRouter({
  routes,
  mode: 'history'
})
