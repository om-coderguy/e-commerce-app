<template>
  <div>
    <h2 class="swiper-container__title">{{ sliderHeading }}</h2>
    <swiper :options="swiperOption" ref="swiper" class="swiper">
      <!-- Loops through item data and creates a carousel item -->

      <swiper-slide v-for="item in mediaItems" :key="item.id">
        <!-- <item :mediaItem="item"></item> -->
          <!-- <v-row> -->
          {{ item.name }}
          {{ item.descr }}<br />
          <span class="discount">{{ item.discount }}% off</span><br />
          <span class="discount">₹ {{ item.cost }}</span
          ><br />
          <v-btn class="mb-2" color="red lighten-3" @click="buyNow"
            >Buy Now</v-btn
          ><br />
          <v-btn color="orange lighten-2" @click="addToCart(item.id)"
            >Add To Cart</v-btn
          >

          <!-- </v-row> -->
      </swiper-slide>
      <!-- 
      <template v-slot:button-prev>
        <div
          @click="$refs.swiper.swiperInstance.slidePrev()"
          class="swiper-button-prev"
        ></div>
      </template>
      <template v-slot:button-next>
        <div
          @click="$refs.swiper.swiperInstance.slideNext()"
          class="swiper-button-next"
        ></div>
      </template> -->
    </swiper>
    <hr class="carousel-container__separator" />
  </div>
</template>

<script>
import { Swiper, SwiperSlide } from "vue-awesome-swiper";

export default {
  name: "swiper-example-pagination-progress",
  title: "Progress pagination",
  components: {
    Swiper,
    SwiperSlide,
  },
  data() {
    return {
      swiperOption: {
        slidesPerView: 7,
        spaceBetween: 30,
        loop: true,
        navigation: {
          nextEl: ".swiper-button-next",
          prevEl: ".swiper-button-prev",
        },
      },
    };
  },
  props: {
    mediaItems: {
      type: Array,
      default: () => [],
    },
    sliderHeading: String,
  },
};
</script>