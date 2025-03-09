<template>
  <div class="add-product-form">
    <v-form @submit.prevent="addProduct" v-model="valid">
      <v-text-field
        v-model="name"
        :rules="[rules.required]"
        label="Product Name"
      ></v-text-field>

      <v-text-field
        v-model="descr"
        :rules="[rules.required]"
        label="Description"
      ></v-text-field>

      <v-text-field
        v-model="cost"
        :rules="[rules.required]"
        label="Cost"
      ></v-text-field>

      <v-text-field
        v-model="discount"
        :rules="[rules.required]"
        label="Discount"
      ></v-text-field>

      <v-text-field
        v-model="catId"
        :rules="[rules.required]"
        label="Category"
      ></v-text-field>

      <v-text-field
        v-model="active"
        :rules="[rules.required]"
        label="Active"
      ></v-text-field>

      <v-btn
        class="white-text"
        color="red lighten-2"
        type="submit"
        :disabled="!valid"
      >
        ADD
      </v-btn>
    </v-form>
  </div>
</template>

<script>
export default {
  data: () => ({
    valid: null,
    name:"",
    descr:"",
    cost:null,
    discount:null,
    catId:null,
    active:null,
    rules: {
      required: (value) => !!value || "Required.",
    },
  }),
  methods: {
    addProduct() {
      console.log("Hello");
    },
  },
};
</script>

<style>
</style>