<template>
    <div class="all-products d-flex flex-wrap">
      <v-snackbar
        v-model="snackbar.value"
        class="snackbar pt-13"
        style="justify-content: right; align-items: flex-start"
        :color="snackbar.color"
      >
        <span class="snackbar-msg">{{ snackbar.message }}</span>
        <template v-slot:action="{ attrs }">
          <v-btn text v-bind="attrs" @click="snackbar.value = false">
            Close
          </v-btn>
        </template>
      </v-snackbar>
      <v-card
        class="ma-5 blue lighten-4"
        max-width="280"
        outlined
        v-for="(order, index) in orders"
        :key="order.id"
      >
        <v-list-item three-line>
          <v-list-item-content>
            <v-list-item-title class="text-h5 mb-4">
              {{ order.productName }}
            </v-list-item-title>
            <span class=" text-body mb-2" style="color:grey;">{{ order.productDesc }}</span>
            <span class="text-body mb-2">Quantity &emsp; : &emsp; {{ order.quantity }}</span>
            <div class="text-body mb-2">Order Status &emsp; : &emsp; {{ order.status }}</div>
            <div class="text-bodymb-2"> Order Total &emsp;: &emsp;₹ {{ order.totalCost }} </div>
            <v-list-item-subtitle></v-list-item-subtitle>
          </v-list-item-content>
        </v-list-item>

        <br />
        <div class="ml-3">
          <v-btn
            class="ma-3"
            color="red lighten-3"
            @click="deleteOrder(order.id, index)"
          >
            Delete Order<v-icon right dark>mdi-delete</v-icon></v-btn
          >
        </div>
      </v-card>
    </div>
</template>

<script>
import axios from "axios";
import urls from "@/urls";

export default {
  data: () => ({
    orders: [],
    auth: [],
    snackbar: {
      value: false,
      message: "",
      color: "",
    },
  }),

  created() {
    this.auth = JSON.parse(localStorage.getItem("auth"));
    this.userOrders();
  },

  computed: {
    orderLength() {
      return this.orders.length;
    },
  },
  methods: {
    userOrders() {
      axios.get(urls().orders+"/user/"+this.auth.userId, {}).then((response) => {
        this.orders = response.data;
        console.log(this.orders);
      });
    },
    deleteOrder(value, index) {
      console.log("Order ID " + value, index);
      axios
        .delete(urls().orders+"/"+value, {})
        .then((response) => {
          this.orders.splice(index, 1);
          console.log(response.data);
          this.snackbar.message = "Order deleted successfully";
          this.snackbar.color = "red";
          this.snackbar.value = true;
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