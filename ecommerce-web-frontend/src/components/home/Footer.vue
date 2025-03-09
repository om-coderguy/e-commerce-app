<template>
  <div id="footer">
    <v-footer class="footer pa-6">
      <ul class="flex-container">
        <li class="links">
          About
          <ol>
            <li class="about">About Us</li>
            <li class="careers">Carrers</li>
          </ol>
        </li>

        <li class="links">
          Connect with Us
          <ol>
            <li class="facebook">Facebook</li>
            <li class="twitter">Twitter</li>
            <li class="instagram">Instagram</li>
          </ol>
        </li>

        <li class="links">
          Support
          <ol>
            <li class="account">Account</li>
            <li class="orders">Orders</li>
          </ol>
        </li>

        <li class="links">
          Make money with Us
          <ol>
            <router-link to="/register/seller" >
            <li class="seller">Register seller</li>
            </router-link>
            <router-link to="/register/delivery">
            <li class="delivery">Register Delivery</li>
            </router-link>
          </ol>
        </li>
      </ul>
    </v-footer>
  </div>
</template>

<script>
export default {
  data:()=>({

  }),
  name: "FooterEcom",
  methods:{
  }
};
</script>

<style>
.footer {
  flex: none;
  box-shadow: 0px 2px 4px -1px rgba(0, 0, 0, 0.2),
    0px 4px 5px 0px rgba(0, 0, 0, 0.14), 0px 1px 10px 0px rgba(0, 0, 0, 0.12);
}

.flex-container {
  width: 90%;
  margin: auto;
  display: flex;
  justify-content: space-between;
}

a{
  text-decoration: none;
}
</style>