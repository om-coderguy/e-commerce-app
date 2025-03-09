<template>
  <v-app>
    <HeaderEcom />
    <div :style="{ height: 'calc(100vh - 205px)', overflow: 'auto' }">
      <CategoryCont v-if="isCat" />
      <!-- <SellerHome v-if="isSeller"/> -->
      <!-- <SwiperProducts v-if="false"/> -->
      <DashBoard v-if="isDash" />
      <router-view :key="$route.path" />
    </div>
    <FooterEcom class="mt-6" />
  </v-app>
</template>
<script>
import HeaderEcom from "./components/home/Header.vue";
import FooterEcom from "./components/home/Footer.vue";
import DashBoard from "./components/home/Dashboard.vue";
import CategoryCont from "./components/category/CategoryLinks.vue";
// import SwiperProducts from './components/SwiperProducts.vue';
// import SellerHome from './components/SellerHome.vue'

export default {
  name: "App",
  data: () => ({
    auth: [],
  }),
  components: {
    HeaderEcom,
    FooterEcom,
    DashBoard,
    CategoryCont,
    // SwiperProducts,
    // SellerHome,
  },
  created() {
    this.auth = JSON.parse(localStorage.getItem("auth"));
    if (this.auth?.userType === "SELLER") this.$router.push("/seller");
    else if (this.auth?.userType === "DELIVERY") this.$router.push("/delivery");
  },

  computed: {
    isDash() {
      if (this.$route.name == "/" || this.$route.name == null) {
        if(this.auth===null) return true
        if (this.auth?.userType === "SITE_USER") return true;
        else return false;
      } else return false;
    },
    isCat() {
      if (
        this.$route.name === "/" ||
        this.$route.name === null ||
        this.$route.name === "Categories"
      ) {
        if(this.auth===null) return true
        if (this.auth?.userType === "SITE_USER") return true;
        else return false;
      } else return false;
    },
    isSeller() {
      if (this.auth?.userType === "SELLER" && this.$route.name == null)
        return true;
      else return false;
    },
  },
};
</script>