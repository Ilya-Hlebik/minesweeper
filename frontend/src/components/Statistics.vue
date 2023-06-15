<template>
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
</template>

<script>
  import {mapGetters} from "vuex";

  export default {
    name: "Statistics",
    computed: {
      ...mapGetters('game', {
        getStatistic: 'getStatistic',
      }),
    },
  }
</script>
<style scoped>
  .big-blue-font {
    font-size: 24px; /* Adjust the font size as needed */
    color: #0004ff;
  }
</style>
