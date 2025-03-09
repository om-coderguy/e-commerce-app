<template>

  <div class="category-content">
    <v-sheet elevation="6">
      <v-tabs
        background-color="cyan"
        dark
        next-icon="mdi-arrow-right-bold-box-outline"
        prev-icon="mdi-arrow-left-bold-box-outline"
        show-arrows
      >
        <v-tab v-for="cat in category" :key="cat.catName" @click="navCategory(cat.catId)">
          {{ cat.catName }}
        </v-tab>
      </v-tabs>
    </v-sheet>

  </div>
</template>

<script>
import urls from "../../urls";
import axios from "axios";

export default {
  name: "CategoryCont",
  data: () => ({
    show: true,
    category: [],
  }),
  created() {
    this.allCates();
  },
  methods: {
    async allCates() {
      await axios.get(urls().categories, {}).then((response) => {
        this.category = response.data;
        console.log(this.category);
      });
    },
    navCategory(value) {
      this.$router.push("/category/"+value);
    },
  },
};
</script>

<style>
.v-tabs-slider{
  display: none;
}

</style>