<template>
  <div class="home-page">

    <v-btn
      text
      class="green lighten-3 outlined ma-2"
      @click="openPage('products')"
    >
      My Products
    </v-btn>

     <v-btn text class="green lighten-3 outlined ma-2" @click="pageName = 'manageInventory'">
      Manage Inventory
    </v-btn>

    <v-btn
      text
      class="green lighten-3 outlined ma-2"
      @click="openPage('orders')"
    >
      My Orders
    </v-btn>
    <div class="main-box">
      <ManageInventory  v-if="pageName == 'manageInventory'"/>
      <SellersProduct v-if="pageName == 'products'" />
      <SellerOrders v-if="pageName=='orders'" />
    </div>
  </div>
</template>

<script>
import ManageInventory from './ManageInventory.vue';
import SellerOrders from './SellerOrders.vue';
import SellersProduct from './SellersProduct.vue'

export default {
  data: () => ({
    pageName: "products",    
  }),
  components:{
    SellersProduct,
    SellerOrders,
    ManageInventory
  },
  methods: {
    openPage(value) {
      this.pageName=value
      console.log(value);
    },   
  },
};
</script>

<style>
</style>