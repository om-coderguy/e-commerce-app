<template>
  <div class="category-content">
    <v-sheet elevation="6">
      <v-tabs
        v-model="selectedTab"
        background-color="cyan"
        dark
        next-icon="mdi-arrow-right-bold-box-outline"
        prev-icon="mdi-arrow-left-bold-box-outline"
        show-arrows
      >
        <v-tab
          v-for="(cat, index) in category"
          :key="cat.catName"
          :value="index"
          @click="navCategory(cat.catId)"
        >
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
    category: [],
    selectedTab: null, // Controls the active tab
  }),
  created() {
    this.allCates();
  },
  methods: {
    async allCates() {
      const response = await axios.get(urls().categories);
      this.category = response.data;
      if (this.category.length) {
        // Optionally set the first tab as active
        this.selectedTab = 0; 
      }
    },
    navCategory(value) {
      this.$router.push("/category/" + value);
    },
  },
};
</script>

<style>
.v-tabs-slider {
  display: none;
}
</style>
