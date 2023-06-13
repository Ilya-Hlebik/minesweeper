<template>
  <div>
    <a-layout class="minesweeper-field">
      <a-layout-header class="header">
        <h1>Minesweeper</h1>
      </a-layout-header>
      <a-layout-content class="content">
        <game-settings v-if="!boardInitiated" @showBoard="onShowBoard">
        </game-settings>
        <div class="board" v-if="boardInitiated">
          <a-layout-content>
            <div class="big-blue-font">
              Total Count Of Mines: {{getStatistic.totalCountOfMines}}
            </div>
            <div class="big-blue-font">
              Current Count Of Flags {{getStatistic.currentCountOfFlags}}
            </div>
            <div class="big-blue-font">
              Maximum Count Of Flags {{getStatistic.maximumCountOfFlags}}
            </div>
            <div class="big-blue-font">
              Unrevealed Cells Amount {{getStatistic.unrevealedAmount}}
            </div>
          </a-layout-content>
          <div
            class="cell"
            v-for="(row, rowIndex) in this.getBoard"
            :key="rowIndex"
          >
            <div
              class="inner-cell"
              v-for="(col, colIndex) in row"
              :key="colIndex"
              :class="{
                'mine': col.mine,
                'revealed': col.revealed,
                'flagged': col.flagged,
                'highLighted': col.highLighted,
              }"
              @click="revealCell($event,rowIndex, colIndex, col.revealed, col.flagged)"
              @mousedown="mouseDownAction($event,rowIndex, colIndex, col.revealed, col.content)"
              @mouseup="mouseUpAction"
              @contextmenu.prevent="toggleFlag(rowIndex, colIndex, col.revealed, col.flagged)"
            >
              {{ col.content }}
            </div>
          </div>
        </div>
      </a-layout-content>
      <a-layout-footer class="footer" v-if="boardInitiated && updateGameStatus">
       <span :class="{
                'big-blue-font': this.getGameStatus === 'WIN',
                'big-red-font': this.getGameStatus === 'LOSE',
              }">{{getGameStatus}}</span>
      </a-layout-footer>

      <a-layout-footer class="footer" v-if="boardInitiated">
        <button @click="resetGame">Reset</button>
      </a-layout-footer>
    </a-layout>
  </div>
</template>

<script>
  import {mapActions, mapGetters, mapMutations} from 'vuex';
  import GameSettings from "./GameSettings";

  export default {
    data() {
      return {
        boardInitiated: false
      }
    },
    methods: {
      mouseDownAction(event, rowIndex, colIndex, revealed, content) {
        if (event.buttons === 3 && revealed && content !== '') {
          this.showCellsOptions({row: rowIndex, column: colIndex})
        }
      },
      mouseUpAction(event) {
        if (event.buttons === 2) {
        this.hideCellsOptions();
        }
      },
      revealCell(event, rowIndex, colIndex, revealed, flagged) {
        if (!revealed && !flagged) {
          this.revealCellOnBoard({row: rowIndex, column: colIndex});
        }
        // Logic to reveal the clicked cell goes here
      },
      toggleFlag(rowIndex, colIndex, revealed, flagged) {
        if (!revealed && (this.getStatistic.maximumCountOfFlags > this.getStatistic.currentCountOfFlags || flagged)) {
          this.setFlagged({row: rowIndex, column: colIndex, flagged: !flagged})
        }
        // Logic to toggle flag on the cell goes here
      },
      resetGame() {
        this.onShowBoard(false);
        this.setGameStatus('');
        this.setBoard(null);
        this.resetStatistic();
        // Logic to reset the game goes here
      },
      onShowBoard(value) {
        this.boardInitiated = value
      },
      ...mapActions('game', {
        revealCellOnBoard: 'revealCell',
        setFlagged: 'setFlagged',
        showAll: 'showAll',
        resetStatistic: 'resetStatistic',
        showCellsOptions: 'showCellsOptions'
      }),
      ...mapMutations('game', {
        toggleBoardFlag: 'toggleBoardFlag',
        setGameStatus: 'setGameStatus',
        setBoard: 'setBoard',
        hideCellsOptions: 'hideCellsOptions',
      }),
    },
    computed: {
      ...mapGetters('game', {
        getBoard: 'board',
        getGameStatus: 'getGameStatus',
        getStatistic: 'getStatistic'
      }),
      updateGameStatus() {
        if (this.getGameStatus === 'WIN' || this.getGameStatus === 'LOSE') {
          this.showAll();
          return true;
        }
        return false;
      }
    },
    components: {
      GameSettings
    }
  }
</script>
<style scoped>
  .minesweeper-field {
    height: 100vh;
    display: flex;
    flex-direction: column;
  }

  .header {
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .content {
    flex-grow: 1;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .board {
    grid-template-columns: repeat(auto-fill, minmax(30px, 1fr));
    grid-gap: 2px;
  }

  .cell {
    background-color: #ddd;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .inner-cell {
    border-style: solid;
    width: 30px;
    height: 30px;
    background-color: #bbb;
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: pointer;
  }

  .inner-cell.highLighted {
    border-style: solid;
    width: 30px;
    height: 30px;
    background-color: #888;
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: pointer;
  }

  .inner-cell.revealed {
    background-color: #eee;
    cursor: default;
  }

  .inner-cell.mine {
    background-color: red;
  }

  .inner-cell.flagged::after {
    content: "🚩";
  }

  .footer {
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .big-red-font {
    font-size: 24px; /* Adjust the font size as needed */
    color: red;
  }

  .big-blue-font {
    font-size: 24px; /* Adjust the font size as needed */
    color: #0004ff;
  }
</style>
