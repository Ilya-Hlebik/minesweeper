import Vue from 'vue';
import Vuex from 'vuex';
import createPersistedState from "vuex-persistedstate";
import * as Cookies from "js-cookie";
import game from './modules/game';

Vue.use(Vuex);

export const store = new Vuex.Store({
  plugins: [
    createPersistedState({
      getState: (key) =>    Cookies.getJSON(key),
      setState: (key, state) =>  Cookies.set(key, state, {expires: 3, secure: false})
    })
  ],
  modules: {
    devtool: 'source-map',
    game,
  },

  strict: process.env.NODE_ENV !== 'production'
});
