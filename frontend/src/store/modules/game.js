import axios from 'axios';

export default {
  namespaced: true,
  state: {
    minesCountFrom: 0,
    minesCountTo: 0,
    board: null,
    gameStatus: '',
    statistics: {
      totalCountOfMines: 0,
      currentCountOfFlags: 0,
      maximumCountOfFlags: 0,
      unrevealedAmount: 0
    },
    rows: '',
    columns: '',
    mines: '',
    gameId: ''
  },
  getters: {
    minesCountFrom(state) {
      return state.minesCountFrom;
    },
    minesCountTo(state) {
      return state.minesCountTo;
    },
    board(state) {
      return state.board;
    },
    getGameStatus(state) {
      return state.gameStatus;
    },
    getStatistic(state) {
      return state.statistics;
    },
    getRows(state) {
      return state.rows;
    },
    getColumns(state) {
      return state.columns;
    },
    getMines(state) {
      return state.mines;
    },
    getGameId(state) {
      return state.gameId;
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
    toggleBoardFlag(state, data) {
      let cell = state.board[data.row][data.column];
      if (!cell.revealed) {
        cell.flagged = !cell.flagged
      }
    },
    setGameStatus(state, data) {
      state.gameStatus = data
    },
    setStatistics(state, data) {
      state.statistics.totalCountOfMines = data.totalCountOfMines
      state.statistics.currentCountOfFlags = data.currentCountOfFlags
      state.statistics.maximumCountOfFlags = data.maximumCountOfFlags
      state.statistics.unrevealedAmount = data.unrevealedAmount
    },
    setCurrentCountOfFlags(state, data) {
      state.statistics.currentCountOfFlags = data
    }
    ,
    setMaximumCountOfFlags(state, data) {
      state.statistics.maximumCountOfFlags = data
    }
    ,
    setTotalCountOfMines(state, data) {
      state.statistics.totalCountOfMines = data
    },
    hideCellsOptions(state) {
      for (let i = 0; i < state.board.length; i++) {
        for (let j = 0; j < state.board[i].length; j++) {
          state.board[i][j].highLighted = false;
        }
      }

    },
    setGameSettings(state, data) {
      state.rows = data.rows;
      state.columns = data.columns;
      state.mines = data.mines;
    },
    setGameId(state, data) {
      state.gameId = data;
    }
  },
  actions: {
    resetStatistic(store) {
      store.commit('setStatistics', {
        totalCountOfMines: 0,
        currentCountOfFlags: 0,
        maximumCountOfFlags: 0,
        unrevealedAmount: 0
      });
    },
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
    async initiateBoard(store) {
      try {
        //store.commit('setGameSettings', data);
        const response = await axios.post('/backend/game/initiate', {rows: store.state.rows, columns: store.state.columns, mines: store.state.mines});
        if (response.status === 200) {
          store.commit('setBoard', response.data.cellResponse);
          store.commit('setGameId', response.data.gameId);
          store.dispatch('resetStatistic');
          store.commit('setMaximumCountOfFlags', store.state.mines);
          store.commit('setTotalCountOfMines', store.state.mines);
        }
        return response.data.gameId;
      } catch (e) {
        console.log(e);
      }
    },
    async getGameById(store, data) {
      const response = await axios.get('/backend/game/' + data);
      store.commit('setGameId', response.data.gameId);
      if (response.status === 200) {
        store.commit('setBoard', response.data.cellResponse);
        store.commit('setGameId', response.data.gameId);
        store.dispatch('resetStatistic');
        store.commit('setMaximumCountOfFlags', store.state.mines);
        store.commit('setTotalCountOfMines', store.state.mines);
      }
    },
    async revealCell(store, data) {
      try {
        const response = await axios.post('/backend/game/revealCell', {...data, gameId: store.state.gameId});
        if (response.status === 200) {
          store.commit('setBoard', response.data.cellResponse);
          store.commit('setGameStatus', response.data.gameStatus);
          store.commit('setStatistics', response.data);
        }
      } catch (e) {
        console.log(e);
      }
    },
    async setFlagged(store, data) {
      try {
        const response = await axios.patch('/backend/game/setFlagged', {...data, gameId: store.state.gameId});
        store.commit('toggleBoardFlag', data);
        store.commit('setCurrentCountOfFlags', response.data);
      } catch (e) {
        console.log(e);
      }
    },
    async showAll(store) {
      try {
        const response = await axios.post('/backend/game/showAll', {gameId: store.state.gameId});
        if (response.status === 200) {
          store.commit('setBoard', response.data.cellResponse);
          store.commit('setGameStatus', response.data.gameStatus);
          store.commit('setStatistics', response.data);
        }
      } catch (e) {
        console.log(e);
      }
    },
    async showCellsOptions(store, data) {
      try {
        const response = await axios.post('/backend/game/showCellsOptions', {...data, gameId: store.state.gameId});
        if (response.status === 200) {
          debugger
          store.commit('setBoard', response.data.cellResponse);
          store.commit('setGameStatus', response.data.gameStatus);
          store.commit('setStatistics', response.data);
        }
      } catch (e) {
        console.log(e);
      }
    }
  }
}
