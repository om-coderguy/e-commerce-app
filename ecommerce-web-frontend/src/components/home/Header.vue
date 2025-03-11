<template>
  <div class="header">
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

    <v-app-bar color="Blue" elevate-on-scroll>
      <v-toolbar-title>
        <v-btn @click="getRoute" class="appbar-title" plain>
          <v-img
            :src="require('../../assets/ecommerce.png')"
            height="25"
            width="25"
            class="mr-4"
          ></v-img>
          <span class="logo-name">e-commerce</span>
        </v-btn>
      </v-toolbar-title>

      <v-spacer></v-spacer>
      <!-- x -->

      <!-- <v-btn class="ma-2" color="secondary" @click="changeDrawer">
        Nav drawer
      </v-btn> -->

      <v-btn
        v-if="isUser"
        color="blue-grey"
        class="ma-2 white--text"
        @click="cart"
      >
        Cart
        <v-icon right dark> mdi-cart </v-icon>
      </v-btn>

      <div v-if="auth !== null" class="avatar">
        <v-menu open-on-click top offset-y style="top: 50px">
          <template v-slot:activator="{ on, attrs }">
            <v-avatar color="primary" size="40" v-bind="attrs" v-on="on">
              <span class="white-text">{{ getName }}</span>
            </v-avatar>
            <!-- <v-btn color="primary" dark v-bind="attrs" v-on="on"> Dropdown </v-btn> -->
          </template>

          <v-list>
            <v-list-item v-for="(item, index) in menuItems" :key="index">
              <v-list-item-title
                @click="editProfile(item.id)"
                style="cursor: pointer"
                >{{ item.title }}</v-list-item-title
              >
            </v-list-item>
          </v-list>
        </v-menu>
      </div>

      <div v-else class="text-center">
        <v-dialog v-model="dialog" width="500">
          <template v-slot:activator="{ on, attrs }">
            <v-btn color="red lighten-2" dark v-bind="attrs" v-on="on">
              LOG IN or SIGN UP
            </v-btn>
          </template>

          <v-card>
            <v-card-title class="text-h5 grey lighten-2"> LOG IN </v-card-title>
            <v-form @submit.prevent="logInUser" v-model="valid">
              <v-card-text>
                <v-text-field
                  v-model="username"
                  :rules="[rules.required, rules.userName]"
                  label="UserName"
                ></v-text-field>

                <v-text-field
                  v-model="password"
                  :counter="20"
                  :rules="[rules.required]"
                  :error-messages="errors"
                  label="Password"
                  type="password"
                ></v-text-field>
              </v-card-text>

              <v-card-actions class="justify-center pb-5">
                <v-btn
                  class="white-text"
                  color="red lighten-2"
                  type="submit"
                  :disabled="!valid"
                >
                  Log IN
                </v-btn>
              </v-card-actions>
            </v-form>

            <div class="text-center">
              <p class="text mb-2">
                New to e-commerce ? Please register to login
              </p>

              <v-dialog v-model="dialog1" width="500">
                <template v-slot:activator="{ on, attrs }">
                  <v-btn
                    color="red lighten-2"
                    dark
                    class="signup-button mb-6"
                    v-bind="attrs"
                    v-on="on"
                    @click="(dialog = false), (dialog1 = true)"
                  >
                    SIGN UP
                  </v-btn>
                </template>

                <v-card>
                  <v-card-title class="text-h5 grey lighten-2">
                    SIGN UP
                  </v-card-title>
                  <v-form @submit.prevent="registerUser" v-model="validReg">
                    <v-card-text>
                      <v-text-field
                        v-model="name"
                        :error-messages="errors"
                        :rules="[rules.required]"
                        label="Name"
                        required
                      ></v-text-field>

                      <v-text-field
                        v-model="regUsername"
                        :error-messages="errors"
                        :rules="[rules.required, rules.userName]"
                        label="UserName"
                      ></v-text-field>

                      <v-text-field
                        v-model="regPassword"
                        :error-messages="errors"
                        :rules="[rules.required]"
                        label="Password"
                        type="password"
                      ></v-text-field>
                    </v-card-text>

                    <v-card-actions
                      class="justify-center pb-6"
                      style="flex-direction: column"
                    >
                      <v-btn
                        class="white-text mb-5"
                        color="red lighten-2"
                        :disabled="!validReg"
                        type="submit"
                      >
                        Register
                      </v-btn>
                      <p class="text mb-2">
                        Already registered !! Please click login
                      </p>
                      <v-btn
                        class="white-text"
                        color="red lighten-2"
                        @click="(dialog1 = false), (dialog = true)"
                      >
                        LOG IN
                      </v-btn>
                    </v-card-actions>
                  </v-form>
                </v-card>
              </v-dialog>
            </div>
          </v-card>
        </v-dialog>
      </div>

      <v-dialog v-model="sellerDialog" width="500">
        <v-card>
          <v-card-title class="text-h5 grey lighten-2"> SIGN UP </v-card-title>
          <v-form @submit.prevent="convertToSeller" v-model="validSeller">
            <v-card-text>
              <v-text-field
                v-model="companyName"
                :rules="[rules.required]"
                label="Company Name"
                type="text"
              ></v-text-field>

              <v-text-field
                v-model="gstin"
                :rules="[rules.required, rules.gstin]"
                label="GSTIN No."
                type="gstin"
              ></v-text-field>
            </v-card-text>

            <v-card-actions class="justify-center pb-6">
              <v-btn
                class="white-text"
                type="submit"
                color="red lighten-2"
                @click="sellerDialog = false"
                :disabled="!validSeller"
              >
                Register
              </v-btn>
            </v-card-actions>
          </v-form>
        </v-card>
      </v-dialog>
    </v-app-bar>
  </div>
</template>

<script>
import urls from "../../urls";
import axios from "axios";

export default {
  name: "HeaderEcom",
  components: {},
  data: () => ({
    auth: [],
    dialog: false,
    dialog1: false,
    sellerDialog: false,
    name: "",
    username: "",
    regUsername: "",
    password: "",
    regPassword: "",
    companyName: "",
    gstin: "",
    errors: [],
    valid: null,
    validReg: null,
    validSeller: null,
    snackbar: {
      value: false,
      message: "",
      color: "",
    },
    items: [
      { id: "profile", title: "My profile" },
      { id: "orders", title: "My Orders" },
      { id: "regSeller", title: "Register as Seller" },
      { id: "logout", title: "Log out" },
    ],
    multiLine: true,
    rules: {
      required: (value) => !!value || "Required.",
      userName: (value) => {
        if (/\D/.test(value)) {
          const pattern =
            /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
          return pattern.test(value) || "Invalid e-mail.";
        } else {
          return value.length === 10 || "Enter only 10 digits.";
        }
      },
      gstin: (value) => value.length === 15 || "Enter the correct gstin",
    },
  }),
  props: {},
  created() {
    this.auth = JSON.parse(localStorage.getItem("auth"));
  },
  computed: {
    getName() {
      return this.auth.name[0];
    },
    menuItems: function () {
      if (this.auth?.userType == "SELLER"||this.auth?.userType == "SUPER_USER" || this.auth?.userType == "DELIVERY")
        return this.items.filter(
          (item) => item.id !== "regSeller" && item.id !== "orders"
        );
      return this.items;
    },
    isUser() {
      if (this.auth?.userType === "SITE_USER") return true;
      else return false;
    },
  },
  methods: {
    cart() {
      this.$router.push("/cart");
    },
    getRoute() {
      switch (this.auth?.userType) {
        case "SITE_USER":
          this.$router.push("/");
          break;
        case "SELLER":
          this.$router.push("/seller");
          break;
        case "SUPER_USER":
          this.$router.push("/seller");
          break;
        case "DELIVERY":
          this.$router.push("/delivery");
          break;
        default:
          this.$router.push("/");
      }
    },
    convertToSeller() {
      console.log(this.auth);
      axios
        .post(urls().seller + "/convert-seller", {
          userId: this.auth.userId,
          companyName: this.companyName,
          gstIn: this.gstin,
        })
        .then((response) => {
          this.snackbar.message = response.data;
          this.snackbar.color = "green";
          this.snackbar.value = true;
          localStorage.setItem("auth", JSON.stringify(response.data));
          this.auth = JSON.parse(localStorage.getItem("auth"));
          this.$router.push("/seller");
        })
        .catch((error) => {
          console.log(error);
          this.snackbar.message = error.response.data;
          this.snackbar.color = "red";
          this.snackbar.value = true;
        });
    },

    editProfile(value) {
      switch (value) {
        case "profile":
          // value=1;
          break;

        case "regSeller":
          this.sellerDialog = true;
          break;

        case "orders":
          window.location.href = "/orders";
          break;

        case "logout":
          localStorage.removeItem("auth");
          this.auth = JSON.parse(localStorage.getItem("auth"));
          this.snackbar.message = "Logout successful";
          this.snackbar.color = "blue";
          console.log(this.$route.name);
          if (this.$route.name !== null) this.$router.push("/");
          window.location.reload(true);
          break;
      }
    },

    registerUser() {
      axios
        .post(urls().registerUser, {
          name: this.name,
          userName: this.regUsername,
          password: this.regPassword,
        })
        .then((response) => {
          console.log(response);
          localStorage.setItem("auth", JSON.stringify(response.data));
          this.auth = JSON.parse(localStorage.getItem("auth"));
          this.$router.reload();
        })
        .catch((error) => {
          console.log(error);
          if (error.response.status === 400) {
            this.snackbar.message = error.response.data;
            this.snackbar.color = "red";
            this.snackbar.value = true;
          }
        });
    },

    logInUser() {
      axios
        .post(urls().login, {
          username: this.username,
          password: this.password,
        })
        .then((response) => {
          console.log(response.data);
          localStorage.setItem("auth", JSON.stringify(response.data));
          this.auth = JSON.parse(localStorage.getItem("auth"));
          this.dialog = false;
          this.snackbar.message = "LogIn successful";
          this.snackbar.color = "green";
          this.snackbar.value = true;
          window.location.reload(true);
          console.log(this.auth);
        })
        .catch((error) => {
          console.log(error);
          if (error.response.status === 403) {
            console.log(error);
            this.snackbar.message = error.response.data;
            this.snackbar.color = "red";
            this.snackbar.value = true;
          } else if (error.response.status === 404) {
            this.snackbar.message = error.response.data;
            this.snackbar.color = "red";
            this.snackbar.value = true;
          }
        });
    },
    changeDrawer() {
      this.$router.push("/nav");
    },
  },
  watch: {},
};
</script>

<style>
.appbar-title {
  display: flex;
  text-decoration: none;
}

.v-responsive {
  flex: none;
}

.white-text .v-btn__content {
  color: white;
}

.white-text {
  color: white;
}

.logo-name {
  color: #000;
}
</style>