<template>
  <div class="content">
    <v-snackbar
      v-model="snackbar.value"
      class="snackbar pt-13"
      style="justify-content: right; align-items: flex-start"
      :multi-line="multiLine"
      :color="snackbar.color"
    >
      <span class="snackbar-msg">{{ snackbar.message }}</span>

      <template v-slot:action="{ attrs }">
        <v-btn text v-bind="attrs" @click="snackbar.value = false">
          Close
        </v-btn>
      </template>
    </v-snackbar>
    <v-row class="pt-3 pl-3" style="width: 100%">
      <v-card
        class="ma-3 green lighten-4"
        max-width="350"
        outlined
        v-for="product in productsByCat"
        :key="product.id"
        @click="productDetails(product.id)"
      >
        <v-col cols="12">
          <v-list-item>
            <v-list-item-avatar size="100">
              <v-img
                :src="
                  product?.photo
                    ? product.photo
                    : require('@/assets/DefaultProduct.png')
                "
                alt="Product Image"
              ></v-img>
            </v-list-item-avatar>
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
              class="mb-2"
              color="red lighten-3"
              @click.stop="buyNow(product.id)"
            >
              Buy Now </v-btn
            ><br />
            <v-btn
              class="mb-4"
              color="orange lighten-2"
              @click.stop="addToCart(product.id)"
            >
              Add To Cart
            </v-btn>
          </div>
        </v-col>
      </v-card>
    </v-row>
  </div>
</template>

<script>
import urls from "../../urls";
import axios from "axios";

export default {
  data: () => ({
    auth: [],
    category: [],
    productsByCat: [],
    snackbar: {
      value: false,
      message: "",
      color: "",
    },
  }),
  created() {
    this.auth = JSON.parse(localStorage.getItem("auth"));
    const catId = this.$route.params.id;
    this.productsByCatId(catId);
    console.log(this.$route.path);
  },
  computed: {},
  methods: {
    async productsByCatId(value) {
      await axios
        .get(urls().products + "/category/" + value, {})
        .then((response) => {
          this.productsByCat = response.data;
          console.log(this.productsByCat);
        });
    },
    addToCart(value) {
      console.log(value);
      if (this.auth) {
        axios
          .post(urls().cart, {
            userId: this.auth.userId,
            productId: value,
          })
          .then((response) => {
            console.log(response);
            this.snackbar.message = response.data;
            this.snackbar.color = "green";
            this.snackbar.value = true;
          });
      }
    },
    buyNow(value) {
      this.$router.push("/buy/" + value);
    },
    productDetails(value) {
      window.location.href = `/ProductDetails/${value}`;
    },
  },
};
</script>

<style>
.v-data-table-header tr > th {
  font-size: 16px !important;
  font-weight: bold;
  color: #411085 !important;
}
</style>
