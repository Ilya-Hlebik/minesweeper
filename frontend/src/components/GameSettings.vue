<template>
  <a-form
    name="basic"
    :label-col="{ span: 8 }"
    :wrapper-col="{ span: 16 }"
    autocomplete="off"
    @submit.prevent="onFinish"
    @finishFailed="onFinishFailed"
  >
    <a-form-item
      label="Rows"
      name="username"
      :rules="[{ required: true, message: 'Please Rows count' }]"
    >
      <a-input v-model:value="rows"/>
    </a-form-item>

    <a-form-item
      label="Columns"
      name="columns"
      type="number"
      :rules="[{ required: true, message: 'Please Columns count' }]"
    >
      <a-input v-model:value="columns"/>
    </a-form-item>

    <span v-if="showMinesCount">mines min: {{minesCountFrom}} mines max: {{minesCountTo}}</span>

    <a-form-item
      label="Mines"
      name="mines"
      type="number"
      :rules="[{ required: true, message: 'Please Enter Mines count' }]"
    >

      <a-input v-model:value="mines" :disabled="!showMinesCount"/>
    </a-form-item>

    <a-form-item :wrapper-col="{ offset: 8, span: 16 }">
      <a-button type="primary" html-type="submit">Submit</a-button>
    </a-form-item>
  </a-form>
</template>

<script>
  import {mapActions, mapGetters, mapMutations} from 'vuex';

  export default {
    name: "GameSettings",
    data() {
      return {
        rows: '',
        columns: '',
        mines: '',
      }
    },
    methods: {
      onFinish() {
        this.setGameSettings({rows: this.rows, columns: this.columns, mines: this.mines});
        this.$router.push('/game')
      },
      onFinishFailed() {
        console.log("finish failed");
      },
      ...mapActions('game', {
        calculateMinesCount: 'calculateMinesCount',
        initiateBoard: 'initiateBoard'
      }),
      ...mapMutations('game',{
        setGameSettings: 'setGameSettings'
      })
    },
    computed: {
      showMinesCount() {
        let needToShow = this.rows !== '' && this.columns !== '';
        if (needToShow) {
          this.calculateMinesCount({rows: this.rows, columns: this.columns})
        }
        return needToShow
      },

      ...mapGetters('game', {
        minesCountFrom: 'minesCountFrom',
        minesCountTo: 'minesCountTo'
      }),
    }
  }
</script>

<style scoped>

</style>
