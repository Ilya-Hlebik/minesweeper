<template>
  <div>
    <a-layout class="minesweeper-field">
      <a-layout-header class="header">
        <h1>Minesweeper</h1>
      </a-layout-header>
      <a-layout-content class="content">
        <div class="board">
          <div
            class="cell"
            v-for="(row, rowIndex) in board"
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
              @click="revealCell(rowIndex, colIndex)"
              @contextmenu.prevent="toggleFlag(rowIndex, colIndex)"
            >
              {{ col.content }}
            </div>
          </div>
        </div>
      </a-layout-content>
      <a-layout-footer class="footer">
        <game-settings></game-settings>
        <button @click="resetGame">Reset</button>
      </a-layout-footer>
    </a-layout>
  </div>
</template>

<script>
  import {mapActions} from 'vuex';
  import GameSettings from "./GameSettings";

  export default {
    data() {
      return {
        board: [
          [
            { isMine: false, revealed: false, flagged: false, content: '' },
            { isMine: true, revealed: false, flagged: false, content: '' },
            { isMine: false, revealed: false, flagged: false, content: ' 1 ' },
            { isMine: false, revealed: false, flagged: false, content: ' 1 ' },
            { isMine: false, revealed: false, flagged: false, content: '' },
            { isMine: false, revealed: false, flagged: false, content: ' 2 ' },
            { isMine: false, revealed: false, flagged: false, content: '' },
            { isMine: false, revealed: false, flagged: false, content: '' },
          ],
          [
            { isMine: false, revealed: false, flagged: false, content: ' 2 ' },
            { isMine: false, revealed: false, flagged: false, content: '' },
            { isMine: false, revealed: false, flagged: false, content: '' },
            { isMine: true, revealed: false, flagged: false, content: '' },
            { isMine: false, revealed: false, flagged: false, content: '' },
            { isMine: false, revealed: false, flagged: false, content: ' 3 ' },
            { isMine: false, revealed: false, flagged: false, content: ' 2 ' },
            { isMine: false, revealed: false, flagged: false, content: ' 1 ' },
          ]
          ,
          [
            { isMine: false, revealed: false, flagged: false, content: ' 2 ' },
            { isMine: false, revealed: false, flagged: false, content: '' },
            { isMine: false, revealed: true, flagged: false, content: '' },
            { isMine: true, revealed: false, flagged: false, content: '' },
            { isMine: false, revealed: false, flagged: false, content: '' },
            { isMine: false, revealed: false, flagged: false, content: ' 3 ' },
            { isMine: false, revealed: false, flagged: false, content: ' 2 ' },
            { isMine: false, revealed: false, flagged: false, content: ' 1 ' },
          ]
          ,
          [
            { isMine: false, revealed: false, flagged: false, content: ' 2 ' },
            { isMine: false, revealed: false, flagged: false, content: '' },
            { isMine: false, revealed: true, flagged: false, content: '' },
            { isMine: true, revealed: false, flagged: false, content: '' },
            { isMine: false, revealed: false, flagged: false, content: '' },
            { isMine: false, revealed: false, flagged: false, content: ' 3 ' },
            { isMine: false, revealed: false, flagged: false, content: ' 2 ' },
            { isMine: false, revealed: false, flagged: false, content: ' 1 ' },
          ]
        ]
      };
    },
    mounted() {
      this.initializeBoard();
    },
    methods: {
      initializeBoard() {
          this.checkAuthorization();
      },
      revealCell(rowIndex, colIndex) {
        // Logic to reveal the clicked cell goes here
      },
      toggleFlag(rowIndex, colIndex) {
        console.log(1)
        this.board[rowIndex][colIndex].flagged = true
        // Logic to toggle flag on the cell goes here
      },
      resetGame() {
        // Logic to reset the game goes here
      },
      ...mapActions('game', {
        checkAuthorization: 'checkAuthorization'
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
