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
                'mine': col.isMine,
                'revealed': col.revealed,
                'flagged': col.flagged,
              }"
              @click="revealCell(rowIndex, colIndex, col.revealed)"
              @contextmenu.prevent="toggleFlag(rowIndex, colIndex, col.flagged)"
            >
              {{ col.content }}
            </div>
          </div>
        </div>
      </a-layout-content>
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
    mounted() {
      this.initializeBoard();
    },
    methods: {
      initializeBoard() {
      },
      revealCell(rowIndex, colIndex, revealed) {
        if (!revealed) {
          this.revealCellOnBoard({row: rowIndex, column: colIndex, revealed: revealed});
        }
        // Logic to reveal the clicked cell goes here
      },
      toggleFlag(rowIndex, colIndex, flagged) {
        this.setFlagged({row: rowIndex, column: colIndex, flagged: !flagged})
        // Logic to toggle flag on the cell goes here
      },
      resetGame() {
        this.onShowBoard(false);
        // Logic to reset the game goes here
      },
      onShowBoard(value) {
        this.boardInitiated = value
      },
      ...mapActions('game', {
        revealCellOnBoard: 'revealCell',
        setFlagged: 'setFlagged'
      }),
      ...mapMutations('game', {
        toggleBoardFlag: 'toggleBoardFlag',
      }),
    },
    computed: {
      ...mapGetters('game', {
        getBoard: 'board',
      }),
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
    max-width: 500px;
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
</style>
