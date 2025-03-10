<template>
    <div class="product">
      <!-- Product Card -->
      <v-card class="ma-5 green lighten-4 d-flex align-center" max-width="680" width="680" min-height="250" outlined>
        <!-- Like Button at Top Right Corner -->
        <v-btn
          icon
          color="red"
          class="like-button"
          @click="toggleLike"
        >
          <v-icon>{{ isLiked ? 'mdi-heart' : 'mdi-heart-outline' }}</v-icon>
        </v-btn>
  
        <v-list-item>
          <!-- Product Image on the Left -->
          <v-list-item-avatar size="200">
            <v-img
              :src="product.image || require('@/assets/DefaultProduct.png')"
              alt="Product Image"
            ></v-img>
          </v-list-item-avatar>
  
          <v-list-item-content class="ml-10">
            <v-list-item-title class="text-h5 mb-4">
              {{ product.name || "" }}
              <span class="text-h6">{{ product.descr }}</span>
            </v-list-item-title>
  
            <!-- Discounted Price and Original Price -->
            <div class="text-body">
              <span class="discounted-price">
                ₹ {{ discountedPrice.toFixed(2) }}
              </span>
              <span class="original-price">
                ₹ {{ product.cost }}
              </span>
            </div>
  
            <!-- Discount Percentage -->
            <div class="text-overline discount">
              {{ product.discount }}% off
            </div>
          </v-list-item-content>
        </v-list-item>
      </v-card>
  
      <!-- Review Submission Form -->
      <v-card class="ma-5 pa-5" max-width="680" width="680" outlined>
        <v-form @submit.prevent="submitReview">
          <v-text-field
            v-model="newReview"
            label="Write a review"
            outlined
            required
          ></v-text-field>
          <v-btn
            type="submit"
            color="blue"
            class="mt-3"
          >
            Submit Review
          </v-btn>
        </v-form>
      </v-card>
  
      <!-- Reviews List -->
      <v-card class="ma-5 pa-5" max-width="680" width="680" outlined>
        <v-list>
          <v-list-item
            v-for="(review, index) in reviews"
            :key="index"
          >
            <v-list-item-content>
              <v-list-item-title>{{ review.author }}</v-list-item-title>
              <v-list-item-subtitle>{{ review.content }}</v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
        </v-list>
      </v-card>
    </div>
  </template>
  
  <script>
  import axios from "axios";
  import urls from "@/urls";
  
  export default {
    data: () => ({
      product: {},
      auth: [],
      isLiked: false,
      newReview: "",
      reviews: [],
    }),
  
    computed: {
      discountedPrice() {
        return this.product.cost - (this.product.cost * this.product.discount) / 100;
      },
    },
  
    created() {
      this.auth = JSON.parse(localStorage.getItem("auth"));
      const productId = this.$route.params.productId;
      this.fetchProductById(productId);
      this.fetchReviews(productId);
    },
  
    methods: {
      toggleLike() {
        this.isLiked = !this.isLiked;
        // Call a dummy API to record the like action
        axios.post(urls().products + "/like", {
          productId: this.product.id,
          userId: this.auth.userId,
          liked: this.isLiked,
        });
      },
  
      submitReview() {
        if (this.newReview.trim() !== "") {
          // Call a dummy API to submit the review
          axios.post(urls().products + "/review", {
            productId: this.product.id,
            userId: this.auth.userId,
            comment: this.newReview,
            rating: 5,
          }).then(() => {
            // Add the new review to the list
            this.reviews.push({
              author: "You",
              content: this.newReview,
            });
            this.newReview = "";
          });
        }
      },
  
      fetchProductById(productId) {
        if (this.auth !== null) {
          axios.post(`${urls().products}/byid`, {
            productId: productId,
            userId: this.auth.userId,
          }).then((response) => {
            this.product = response.data;
          });
        }
      },
  
      fetchReviews(productId) {
        // Fetch dummy reviews from a dummy API
        axios.post(urls().products+"/"+productId + "/reviews", {})
          .then((response) => {
            // Map the dummy data to match the review structure
            this.reviews = response.data.map((item) => ({
              author: item.userName,
              content: item.comment,
            }));
          });
      },
    },
  };
  </script>
  
  <style>
  /* Styling for Discount & Price */
  .discount {
    color: red;
    font-weight: bold;
  }
  
  .discounted-price {
    font-size: 1.2rem;
    font-weight: bold;
    color: green;
  }
  
  .original-price {
    font-size: 1rem;
    color: grey;
    text-decoration: line-through;
    margin-left: 8px;
  }
  
  /* Like Button Position */
  .like-button {
    position: absolute;
    top: 10px;
    right: 10px;
    z-index: 10; /* Ensure it stays on top */
  }
  </style>
  