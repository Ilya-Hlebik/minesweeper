import axios from 'axios';
/*
import {router} from "../../routes";
*/

export default {
  namespaced: true,
  state: {
    isLogged: false,
    needShowLoginFrom: false,
    needShowRegistrationForm: false,
    roles: [],
    user:null
  },
  getters: {
    isLogged(state) {
      return state.isLogged;
    },
    needShowLoginFrom(state) {
      console.log(2);
      return state.needShowLoginFrom || !state.isLogged;
    },
    needShowRegistrationForm(state) {
      return state.needShowRegistrationForm;
    },
    roles(state){
      return state.roles;
    },
    user(state){
      return state.user;
    },
  },
  mutations: {
    updateLoginInfo(state, data) {
      state.isLogged = data;
    },
    needShowLoginFrom(state, data) {
      state.needShowLoginFrom = data;
    },
    needShowRegistrationForm(state, data) {
      state.needShowRegistrationForm = data;
    },
    updateRoles(state, data){
      state.roles = data;
    },
    setUser(state, data){
      state.user = data;
    },
    setUserName(state, e){
      state.user.userInfo.name = e.target.value;
    },
  },
  actions: {
    async signIn(store, data) {
      try {
        const response = await axios.post('/backend/users/signin', data);
        if (response.status === 200) {
          store.commit('updateLoginInfo', true);
          store.commit('updateRoles',response.data.roles);
          store.commit('setUser', response.data)
        } else if (response.status === 405) {
          alert('You are already logged in');
        } else {
          store.commit('updateLoginInfo', false);
        }
      } catch (error) {
        store.commit('updateLoginInfo', false);
      }
    },
    async logOut(store) {
      try {
        const response = await axios.get('/backend/users/logout');
        if (response.status === 200) {
          store.commit('updateLoginInfo', false);
          store.commit('updateRoles', []);
          router.push('/menu');
        }
      } catch (error) {
        store.commit('updateLoginInfo', false);
        store.commit('updateRoles', []);
      }
    }
    ,
    async checkAuthorization(store) {
      try {
      const response = await axios.get('/backend/users/me');
      const responseStatus = response.status === 200;
      store.commit('updateLoginInfo', responseStatus);
      store.commit('setUser', response.data)
      return responseStatus;
      }
      catch (e) {
        return false;
      }
    },
    async signUp(store, data) {
      try {
        data.formData.delete("user");
        data.formData.append('user', new Blob([JSON.stringify({
          username: data.username,
          password: data.password,
          roles: ['ROLE_CLIENT']
        })], {
          type: "application/json"
        }))
        const response = await axios.post('/backend/users/signup', data.formData, {
          headers: {
            "Content-Type": undefined
          }
        });
        return response.status === 200;
      } catch (error) {
        return false;
      }
    },
  }
}
