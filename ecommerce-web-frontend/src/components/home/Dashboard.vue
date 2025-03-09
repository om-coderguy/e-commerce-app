<template>
  <div class="dashboard-root">
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

    <div id="dashboard" class="ma-2">
      <div class="top-products">
        <!-- <SwiperComponent :mediaItems="topProducts"/> -->
        <h2 class="mt-4 mb-3">Deals of the day</h2>
        <template>
          <v-carousel hide-delimiters height="250">
            <!-- Grouping 4 products per carousel item -->
            <v-carousel-item
              v-for="(chunk, index) in Math.ceil(topProducts.length / 4)"
              :key="index"
              style="height: 100%;"
            >
              <v-layout style="height: 100%;">
                <v-flex
                  v-for="item in topProducts.slice(index * 4, index * 4 + 4)"
                  :key="item.name"
                  xs3
                  class="green lighten-4 text-center pb-3 pt-3 mx-2 d-flex align-center"
                  @click="showProductDetails(item.id)"
                >
                  <!-- Dummy Image on the Left -->
                  <div style="width: 150px; margin: 10px;">
                    <v-img
                      :src="
                        product?.image || require('@/assets/DefaultProduct.png')
                      "
                      alt="Product Image"
                    ></v-img>
                  </div>

                  <!-- Product Details -->
                  <div class="text-left">
                    <strong>{{ item.name }}</strong
                    ><br />
                    {{ item.descr }}<br />

                    <!-- Discount with Color -->
                    <span
                      class="discount"
                      style="color: red; font-weight: bold"
                    >
                      {{ item.discount }}% off </span
                    ><br />

                    <!-- Discounted Price and Original Price -->
                    <span class="discounted-price"
                      >₹
                      {{
                        (item.cost - (item.cost * item.discount) / 100).toFixed(
                          2
                        )
                      }}</span
                    >
                    <span
                      class="original-price"
                      style="text-decoration: line-through; margin-left: 8px"
                    >
                      ₹ {{ item.cost }} </span
                    ><br />

                    <!-- Action Buttons -->
                    <v-btn
                      class="mb-2"
                      color="red lighten-3"
                      @click.stop="buyNow(item.id)"
                      >Buy Now</v-btn
                    ><br />
                    <v-btn
                      color="orange lighten-2"
                      @click.stop="addToCart(item.id)"
                      >Add To Cart</v-btn
                    >
                  </div>
                </v-flex>
              </v-layout>
            </v-carousel-item>
          </v-carousel>
        </template>
      </div>
      <div class="all-products">
        <h2 class="mt-4 mb-3">All Products</h2>
        <template>
          <v-carousel hide-delimiters height="250">
            <!-- Grouping 4 products per carousel item -->
            <v-carousel-item
              v-for="(chunk, index) in Math.ceil(allProducts.length / 4)"
              :key="index" style="height: 100%;"
            >
              <v-layout style="height: 100%;">
                <v-flex
                  v-for="item in allProducts.slice(index * 4, index * 4 + 4)"
                  :key="item.name"
                  xs3
                  class="green lighten-4 text-center pb-3 pt-3 mx-2 d-flex align-center"
                  @click="showProductDetails(item.id)"
                >
                  <!-- Dummy Image on the Left -->
                  <div style="width: 150px; margin: 10px;">
                    <v-img
                      :src="
                        product?.image || require('@/assets/DefaultProduct.png')
                      "
                      alt="Product Image"
                    ></v-img>
                  </div>

                  <!-- Product Details -->
                  <div class="text-left">
                    <strong>{{ item.name }}</strong
                    ><br />
                    {{ item.descr }}<br />

                    <!-- Discount with Color -->
                    <span
                      class="discount"
                      style="color: red; font-weight: bold"
                    >
                      {{ item.discount }}% off </span
                    ><br />

                    <!-- Discounted Price and Original Price -->
                    <span class="discounted-price"
                      >₹
                      {{
                        (item.cost - (item.cost * item.discount) / 100).toFixed(
                          2
                        )
                      }}</span
                    >
                    <span
                      class="original-price"
                      style="text-decoration: line-through; margin-left: 8px"
                    >
                      ₹ {{ item.cost }} </span
                    ><br />

                    <!-- Action Buttons -->
                    <v-btn
                      class="mb-2"
                      color="red lighten-3"
                      @click.stop="buyNow(item.id)"
                      >Buy Now</v-btn
                    ><br />
                    <v-btn
                      color="orange lighten-2"
                      @click.stop="addToCart(item.id)"
                      >Add To Cart</v-btn
                    >
                  </div>
                </v-flex>
              </v-layout>
            </v-carousel-item>
          </v-carousel>
        </template>
      </div>
      <div class="recent-products" v-if="auth !== null">
        <h2 class="mt-4 mb-3">Recently viewed products</h2>
        <template>
          <v-carousel hide-delimiters height="250">
            <!-- Grouping 4 products per carousel item -->
            <v-carousel-item
              v-for="(chunk, index) in Math.ceil(recentProducts.length / 4)"
              :key="index" style="height: 100%;"
            >
              <v-layout style="height: 100%;">
                <v-flex
                  v-for="item in recentProducts.slice(index * 4, index * 4 + 4)"
                  :key="item.name"
                  xs3
                  class="green lighten-4 text-center pb-3 pt-3 mx-2 d-flex align-center"
                  @click="showProductDetails(item.id)"
                >
                  <!-- Dummy Image on the Left -->
                  <div style="width: 150px; margin: 10px;">
                    <v-img
                      :src="
                        product?.image || require('@/assets/DefaultProduct.png')
                      "
                      alt="Product Image"
                    ></v-img>
                  </div>

                  <!-- Product Details -->
                  <div class="text-left">
                    <strong>{{ item.name }}</strong
                    ><br />
                    {{ item.descr }}<br />

                    <!-- Discount with Color -->
                    <span
                      class="discount"
                      style="color: red; font-weight: bold"
                    >
                      {{ item.discount }}% off </span
                    ><br />

                    <!-- Discounted Price and Original Price -->
                    <span class="discounted-price"
                      >₹
                      {{
                        (item.cost - (item.cost * item.discount) / 100).toFixed(
                          2
                        )
                      }}</span
                    >
                    <span
                      class="original-price"
                      style="text-decoration: line-through; margin-left: 8px"
                    >
                      ₹ {{ item.cost }} </span
                    ><br />

                    <!-- Action Buttons -->
                    <v-btn
                      class="mb-2"
                      color="red lighten-3"
                      @click.stop="buyNow(item.id)"
                      >Buy Now</v-btn
                    ><br />
                    <v-btn
                      color="orange lighten-2"
                      @click.stop="addToCart(item.id)"
                      >Add To Cart</v-btn
                    >
                  </div>
                </v-flex>
              </v-layout>
            </v-carousel-item>
          </v-carousel>
        </template>
      </div>
    </div>
  </div>
</template>

<script>
import urls from "@/urls";
// import urls from "../urls";
import axios from "axios";
// import SwiperComponent from './SwipeProducts.vue'

export default {
  data() {
    return {
      auth: JSON.parse(localStorage.getItem("auth")),
      allProducts: this.allProducts1(),
      topProducts: this.topProducts1(),
      recentProducts: this.recentProducts1(),
      multiLine: false,
      snackbar: {
        value: false,
        message: "",
        color: "",
      },
      // swiperOption: {
      //   slidesPerView: 7,
      //   spaceBetween: 30,
      //   loop: true,
      //   navigation: {
      //     nextEl: ".swiper-button-next",
      //     prevEl: ".swiper-button-prev",
      //   },
      // },
    };
  },
  name: "DashBoard",
  components: {},
  computed: {},
  created() {},
  methods: {
    async allProducts1() {
      console.log("Hello Iam inside");
      await axios.get(urls().products, {}).then((response) => {
        console.log(response);
        this.allProducts = response.data;
      });
    },
    async topProducts1() {
      await axios.get(urls().products + "/top", {}).then((response) => {
        console.log(response);
        this.topProducts = response.data;
      });
      // .catch((error) => {
      //   console.log(error);
      //   if (error.response.status === 400) {
      //     this.snackbar.message = error.response.data;
      //     this.snackbar.color = "red";
      //     this.snackbar.value = true;
      //   }
      // });
    },
    async recentProducts1() {
      const auth = JSON.parse(localStorage.getItem("auth"));
      console.log(auth);
      if (auth) {
        await axios
          .get(urls().products + "/recent/" + auth.userId, {})
          .then((response) => {
            console.log(response.data);
            this.recentProducts = response.data;
          });
        // .catch((error) => {
        //   console.log(error);
        //   if (error.response.status === 400) {
        //     this.snackbar.message = error.response.data;
        //     this.snackbar.color = "red";
        //     this.snackbar.value = true;
        //   }
        // });
      }
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
      console.log(value);
      this.$router.push("/buy/" + value);
    },
    showProductDetails(value) {
      window.location.href = `/ProductDetails/${value}`;
    },
  },
};
</script>

<style>
</style>