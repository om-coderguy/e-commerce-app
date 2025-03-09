<template>
  <div class="add-delivery-form" style="width:30%;margin:auto;">
    <h2 class="py-5">REGISTER DELIVERY</h2>
    <v-form @submit.prevent="addDelivery" v-model="valid">
      <v-text-field
        v-model="name"
        :rules="[rules.required]"
        label="Name"
      ></v-text-field>

      <v-text-field
        v-model="userName"
        :rules="[rules.required,rules.userName]"
        label="Username"
      ></v-text-field>

      <v-text-field
        v-model="password"
        :rules="[rules.required]"
        label="password"
      ></v-text-field>

      <v-btn
        class="white-text"
        style="margin:auto;"
        color="red lighten-2"
        type="submit"
        :disabled="!valid"
      >
        Register
      </v-btn>
    </v-form>
  </div>
</template>

<script>
import urls from '../../urls'
import axios from 'axios'

export default {
  data: () => ({
    valid: null,
    name:"",
    userName:"",
    password:"",
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
    },
  }),
  methods: {
    addDelivery() {
       axios
        .post(urls().registerUser+"/registerdelivery", {
          name: this.name,
          userName: this.userName,
          password: this.password,
        })
        .then((response) => {
          console.log(response.data);
          localStorage.setItem("auth", JSON.stringify(response.data));
          this.auth = JSON.parse(localStorage.getItem("auth"));
          this.$router.push("/delivery");
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
  },
};
</script>

<style>
</style>