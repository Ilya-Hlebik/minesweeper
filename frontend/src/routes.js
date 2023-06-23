import Vue from 'vue';
import VueRouter from 'vue-router';
import {store} from './store';
import Field from './components/Field';

Vue.use(VueRouter);

const routes = [

  {
    name: 'game',
    path: '/game',
    async beforeEnter(to, from, next) {
      await store.commit('game/resetGame');
      await store.dispatch('game/resetStatistic');
      let gameId = await store.dispatch('game/initiateBoard');
      if (gameId !== null && to.path === '/game') {
        next('/game/' + gameId);
      } else {
        next();
      }
    }
  },
  {
    name: 'gameID',
    path: '/game/:id',
    async beforeEnter(to, from, next) {
      await store.dispatch('game/getGameById', to.params.id);
      next();
    },
    component: Field,
    props: {boardInitiated: true}
  },
  {
    name: 'menu',
    path: '/menu',
    async beforeEnter(to, from, next) {
      await store.commit('game/resetGame');
      await store.dispatch('game/resetStatistic');
      next();
    },
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
