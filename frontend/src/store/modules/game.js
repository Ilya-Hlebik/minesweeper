import axios from 'axios';

export default {
  namespaced: true,
  state: {
    minesCountFrom: 0,
    minesCountTo: 0,
    board: null
  },
  getters: {
    minesCountFrom(state) {
      return state.minesCountFrom;
    },
    minesCountTo(state) {
      return state.minesCountTo;
    },
    board(state){
      return state.board;
    }
  },
  mutations: {
    setMinesCountFrom(state, data) {
      state.minesCountFrom = data
    },
    setMinesCountTo(state, data) {
      state.minesCountTo = data
    },
    setBoard(state, data) {
      state.board = data
    },
    toggleBoardFlag(state, data){
      let cell = state.board[data.row][data.column];
      if (!cell.revealed){
        cell.flagged = !cell.flagged
      }
    }
  },
  actions: {
    async calculateMinesCount(store, data) {
      try {
        const response = await axios.post('/backend/game/minesCount', data);
        if (response.status === 200) {
          store.commit('setMinesCountFrom', response.data.minesFrom);
          store.commit('setMinesCountTo', response.data.minesTo);
        }
      } catch (e) {
        console.log(e);
      }
    },
    async initiateBoard(store, data) {
      try {
        const response = await axios.post('/backend/game/initiate', data);
        if (response.status === 200) {
          store.commit('setBoard', response.data);
        }
      } catch (e) {
        console.log(e);
      }
    },
  }
}
