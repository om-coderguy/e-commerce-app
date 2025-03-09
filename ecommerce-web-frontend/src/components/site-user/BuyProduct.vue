<template>
  <div class="show-products d-flex">
    <div class="product">
      <v-card class="ma-5 green lighten-4" max-width="680" width="680" outlined>
        <v-list-item three-line>
          <v-list-item-content>
            <v-list-item-title class="text-h5 mb-4">
              {{ product.name }}
              <span class="text-h6">{{ product.descr }}</span>
            </v-list-item-title>
            <div class="text-body">₹ {{ product.cost }}</div>
            <div class="text-overline">{{ product.discount }} % off</div>
            <v-list-item-subtitle></v-list-item-subtitle>
          </v-list-item-content>
        </v-list-item>
      </v-card>
    </div>
    <v-spacer></v-spacer>
    <div class="order-info pa-15">
      <v-card class="pa-5 ivory lighten-4" max-width="280">
        <p class="font-weight-light">Price Details</p>
        <v-divider class="mb-5"></v-divider>
        <p>price &emsp; &emsp; : &emsp;₹ {{ product.cost }}</p>
        <p>Discount &emsp; &emsp; : &emsp;{{ product.discount }} %</p>
        <v-divider class="mb-5"></v-divider>
        <p class="font-weight-black">Total Amount : ₹ {{ totalAmount }}</p>
      </v-card>
      <v-btn class="px-15 my-5" color="orange lighten-2" @click="placeOrders"
        >Place Order</v-btn
      >
    </div>
  </div>
</template>

<script>
import axios from "axios";
import urls from "@/urls";

export default {
  data: () => ({
    product: {},
    auth: [],
  }),

  created() {
    this.auth = JSON.parse(localStorage.getItem("auth"));
    const productId = this.$route.params.productId;
    console.log(productId);
    this.productsById(productId);
  },

  computed: {
    totalAmount() {
      let totalAmount =
        this.product.cost - this.product.cost * (this.product.discount / 100);
      return totalAmount;
    },
  },

  methods: {
    placeOrders() {
      axios
        .post(urls().orders, {
          quantity: 1,
          userId: this.auth.userId,
          productId: this.product.id,
        })
        .then((response) => {
          this.$router.push("/");
          console.log(response.data);
        });
    },

    productsById(value) {
      console.log(value);
      if(this.auth!==null){
      axios
        .get(urls().products + "/byid/" + value + "/" + this.auth.userId, {})
        .then((response) => {
          this.product = response.data;
          console.log(this.product);
        });
      }
      else{
        axios
        .get(urls().products + "/byid/" + value, {})
        .then((response) => {
          this.product = response.data;
          console.log(this.product);
        });
      }
    },
  },
};
</script>

<style>
</style>