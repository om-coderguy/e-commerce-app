<template>
  <div>
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
    <v-dialog v-model="dialog" width="500">
      <template v-slot:activator="{ on, attrs }">
        <v-btn v-bind="attrs" v-on="on" :color="buttonColor">
          {{ buttonText }}
        </v-btn>
      </template>

      <v-card>
        <v-card-title class="text-h5 grey lighten-2">
          {{ mode === "login" ? "LOG IN" : "SIGN UP" }}
        </v-card-title>

        <v-form @submit.prevent="handleSubmit" v-model="isValid">
          <v-card-text>
            <v-text-field
              v-if="mode === 'signup'"
              v-model="form.name"
              :rules="[rules.required]"
              label="Name"
              required
            ></v-text-field>

            <v-text-field
              v-model="form.username"
              :rules="[rules.required, rules.userName]"
              label="UserName"
            ></v-text-field>

            <v-text-field
              v-model="form.password"
              :rules="[rules.required]"
              label="Password"
              type="password"
            ></v-text-field>
          </v-card-text>

          <v-card-actions class="justify-center pb-5">
            <v-btn color="red lighten-2" type="submit" :disabled="!isValid">
              {{ mode == "login" ? "Log In" : "Register" }}
            </v-btn>
          </v-card-actions>
        </v-form>

        <!-- Toggle between Login and Signup -->
        <div class="text-center pb-4">
          <p v-if="mode === 'login'" class="text mb-2">
            New to e-commerce? Please register to login.
          </p>
          <p v-else class="text mb-2">Already registered? Please log in.</p>

          <v-btn color="red lighten-2" dark @click="$emit('toggleMode')">
            {{ mode == "login" ? "SIGN UP" : "LOG IN" }}
          </v-btn>
        </div>
      </v-card>
    </v-dialog>
  </div>
</template>
  
  <script>
import axios from "axios";
import urls from "../../urls";

export default {
  name: "Login",
  props: {
    mode: {
      type: String,
      default: "login",
    },
    buttonText: {
      type: String,
      default: "LOG IN", // Dynamic button label
    },
    buttonColor: {
      type: String,
      default: "", // Pass custom CSS classes
    },
  },
  data() {
    return {
      dialog: false,
      isValid: false,
      form: {
        name: "",
        username: "",
        password: "",
      },
      snackbar: {
        value: false,
        message: "",
        color: "",
      },
      rules: {
        required: (value) => !!value || "Required.",
        userName: (value) => {
          if (/\D/.test(value)) {
            const pattern = /^[\w-\\.]+@([\w-]+\.)+[\w-]{2,4}$/;
            return pattern.test(value) || "Invalid e-mail.";
          } else {
            return value.length === 10 || "Enter only 10 digits.";
          }
        },
      },
    };
  },
  methods: {
    async handleSubmit() {
      try {
        const url = this.mode === "login" ? urls().login : urls().registerUser;

        const payload =
          this.mode === "signup"
            ? {
                name: this.form.name,
                userName: this.form.username,
                password: this.form.password,
              }
            : {
                username: this.form.username,
                password: this.form.password,
              };

        const response = await axios.post(url, payload);

        localStorage.setItem("auth", JSON.stringify(response.data));
        this.$emit("authSuccess", response.data);
        this.snackbar.value = true;
        this.snackbar.message = "Login successful";
        this.snackbar.color = "green";
        this.dialog = false;
      } catch (error) {
        console.error("Auth error:", error.response?.data);
        this.snackbar.value = true;
        this.snackbar.message = error.response?.data || "Something went wrong";
        this.snackbar.color = "red";
        this.dialog = false;
        // alert(error.response?.data || "Something went wrong");
      }
    },
  },
};
</script>
  
  <style scoped>
.text {
  font-size: 14px;
}
</style>
  