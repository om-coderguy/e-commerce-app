<template>
  <div class="home-page">
    <v-btn text class="green lighten-3 ma-2" @click="pageName = 'deliveryOrders'">
      My Deliveries
    </v-btn>
    <div class="main-box">
    <DeliveryOrders v-if="pageName=='deliveryOrders'" />
  </div>
  </div>
</template>

<script>
import DeliveryOrders from './DeliveryOrders.vue'

export default {
  data: () => ({
    pageName: "deliveryOrders",    
  }),
  components:{
    DeliveryOrders
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