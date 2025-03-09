<template>
  <div class="show-products d-flex">
    <div class="all-products">
      <v-card
        class="ma-5 green lighten-4"
        max-width="280"
        outlined
        v-for="(product, index) in products"
        :key="product.id"
      >
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

        <br />
        <div class="ml-3">
          <v-btn
            class="ma-3"
            color="red lighten-3"
            @click="deleteFromCart(product.id, index)"
          >
            Delete <v-icon right dark>mdi-delete</v-icon></v-btn
          >
          <v-btn class="ma-3" color="yellow lighten-3" @click="buyNow(product.id)">
            Buy Now </v-btn
          ><br />
        </div>
      </v-card>
    </div>
    <v-spacer></v-spacer>
    <div class="order-info pa-15">
      <v-card class="pa-5 ivory lighten-4" max-width="280">
        <p class="font-weight-light">Price Details</p>
        <v-divider class="mb-5"></v-divider>
        <p>price ( {{ prodLength }} items) : ₹ {{ totalCost }}</p>
        <p>Discount &emsp; &emsp; : {{ totalDiscount }}</p>
        <v-divider class="mb-5"></v-divider>
        <p class="font-weight-black">Total Amount : ₹ {{ totalAmount }}</p>
      </v-card>
      <v-btn class="px-15 my-5" color="orange lighten-2" @click="placeOrders"
        >Place All Orders</v-btn
      >
    </div>
  </div>
</template>

<script>
import axios from "axios";
import urls from "@/urls";

export default {
  data: () => ({
    products: [],
    auth: [],
  }),

  created() {
    this.auth = JSON.parse(localStorage.getItem("auth"));
    this.productInCart();
  },

  computed: {
    prodLength() {
      return this.products.length;
    },

    totalCost() {
      let totalCost = 0;
      this.products.forEach((product) => {
        totalCost += product.cost;
      });
      return totalCost;
    },

    totalDiscount() {
      let totalDisc = 0;
      this.products.forEach((product) => {
        totalDisc += product.cost * (product.discount / 100);
      });
      return totalDisc;
    },

    totalAmount() {
      let totalAmount = 0;
      totalAmount = this.totalCost - this.totalDiscount;
      return totalAmount;
    },
  },
  methods: {
    placeOrders() {
      this.products.forEach((product,index) => {
        axios
          .post(urls().orders, {
            quantity: 1,
            userId: this.auth.userId,
            productId: product.id,
          })
          .then((response) => {
            this.deleteFromCart(product.id,index)
            this.products.splice(index,1)
            console.log(response.data);
          });
      });
    },
    productInCart() {
      axios.get(urls().cart + "/" + this.auth.userId, {}).then((response) => {
        this.products = response.data;
        console.log(this.products);
      });
    },

    buyNow(value) {
      this.$router.push("/buy/" + value);
    },

    deleteFromCart(value, index) {
      console.log("Product ID " + value, index);
      axios
        .delete(urls().cart, {
          data: {
            userId: this.auth.userId,
            productId: value,
          },
        })
        .then((response) => {
          this.products.splice(index, 1);
          console.log(response.data);
        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
};
</script>

<style>
</style>