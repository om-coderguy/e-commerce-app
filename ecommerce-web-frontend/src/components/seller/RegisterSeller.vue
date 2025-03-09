<template>
  <div class="add-seller-form" style="width:30%;margin:auto;">
    <v-snackbar
      v-model="snackbar.value"
      class="snackbar pt-13"
      style="justify-content: right; align-items: flex-start"
      :color="snackbar.color"
    >
      <span class="snackbar-msg">{{ snackbar.message }}</span>

      <template v-slot:action="{ attrs }">
        <v-btn text v-bind="attrs" @click="snackbar.value = false">
          Close   
        </v-btn>
      </template>
    </v-snackbar>

    <h2 class="py-5">REGISTER SELLER</h2>
    <v-form @submit.prevent="addSeller" v-model="valid">
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
        v-model="companyName"
        :rules="[rules.required]"
        label="Company Name"
      ></v-text-field>

      <v-text-field
        v-model="gstin"
        :rules="[rules.required,rules.gstin]"
        label="GSTIN"
      ></v-text-field>

      <v-text-field
        v-model="password"
        :rules="[rules.required]"
        label="Password"
      ></v-text-field>

      <v-btn
        class="white-text my-5"
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
import urls from "../../urls";
import axios from "axios";

export default {
  data: () => ({
    auth:[],
    valid: null,
    name:"",
    userName:"",
    companyName:"",
    gstin:"",
    password:"",
    snackbar: {
      value: false,
      message: "",
      color: "",
    },
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
  created(){
    this.auth=JSON.parse(localStorage.getItem("auth"))
  },
  methods: {
    addSeller() {
      console.log("Hello");
       axios
        .post(urls().seller+"/register", {
          ownerName: this.name,
          userName:this.userName,
          password:this.password,
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
  },
};
</script>

<style>
</style>