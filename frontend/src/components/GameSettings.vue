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

    <a-form-item
      label="Mines"
      name="mines"
      type="number"
      v-if="showMinesCount"
      :rules="[{ required: true, message: 'Please Enter Mines count' }]"
    >
      minesCountFrom : {{minesCountFrom}} minesCountTo : {{minesCountTo}}
      <a-input v-model:value="mines"/>
    </a-form-item>

    <a-form-item :wrapper-col="{ offset: 8, span: 16 }">
      <a-button type="primary" html-type="submit">Submit</a-button>
    </a-form-item>
  </a-form>
</template>

<script>
  import {mapActions, mapGetters} from 'vuex';

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
        console.log(this.rows);
        console.log(this.columns);
      },
      onFinishFailed() {
        console.log("finish failed");
      },
      ...mapActions('game', {
        calculateMinesCount: 'calculateMinesCount'
      }),
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
