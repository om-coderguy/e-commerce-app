<template>
  <v-data-table
    :headers="headers"
    :items="allSubUsers"
    sort-by="name"
    class="elevation-1 ma-8"
  >
    <template v-slot:top>
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

      <v-toolbar flat>
        <v-toolbar-title class="text-h5">Sub Users</v-toolbar-title>
        <v-divider class="mx-4" inset vertical></v-divider>
        <v-spacer></v-spacer>

        <v-dialog v-model="dialog" max-width="500px">
          <template v-slot:activator="{ on, attrs }">
            <v-btn color="primary" class="mb-2" v-bind="attrs" v-on="on">
              Add Sub User
            </v-btn>
          </template>
          <v-card>
            <v-form
              @submit.prevent="save(editedItem.userId)"
              v-model="subUserInfo"
            >
              <v-card-title>
                <span class="text-h5"
                  >{{ isEdit ? "Edit" : "Add" }} Sub User</span
                >
              </v-card-title>

              <v-card-text>
                <v-container>
                  <v-row>
                    <v-col cols="12">
                      <v-text-field
                        v-model="editedItem.fullName"
                        :rules="[rules.required]"
                        label="Name"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="12">
                      <v-select
                        v-model="editedItem.role"
                        :rules="[rules.required]"
                        :items="roles"
                        label="Sub Role"
                      ></v-select>
                    </v-col>
                    <v-col cols="12">
                      <v-text-field
                        v-model="editedItem.username"
                        :rules="[rules.required,rules.userName]"
                        label="Username"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="12" v-if="!isEdit">
                      <v-text-field
                        v-model="editedItem.password"
                        :rules="[rules.required]"
                        label="Password"
                        type="password"
                      ></v-text-field>
                    </v-col>
                  </v-row>
                </v-container>
              </v-card-text>

              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="close">Cancel</v-btn>
                <v-btn
                  color="blue darken-1"
                  text
                  type="submit"
                  :disabled="!subUserInfo"
                  >Save</v-btn
                >
              </v-card-actions>
            </v-form>
          </v-card>
        </v-dialog>

        <v-dialog v-model="dialogDelete" max-width="500px">
          <v-card>
            <v-card-title class="text-h5">
              Are you sure you want to delete this sub-user?
            </v-card-title>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" text @click="closeDelete"
                >Cancel</v-btn
              >
              <v-btn color="red darken-1" text @click="deleteItemConfirm"
                >Delete</v-btn
              >
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-toolbar>
    </template>

    <template v-slot:[`item.actions`]="{ item }">
      <v-icon small class="mr-2" @click="editItem(item)">mdi-pencil</v-icon>
      <v-icon small @click="deleteItem(item)">mdi-delete</v-icon>
    </template>

    <template v-slot:no-data>No sub-users found.</template>
  </v-data-table>
</template>

<script>
import axios from "axios";
import urls from "@/urls";

export default {
  data() {
    return {
      auth: null,
      dialog: false,
      dialogDelete: false,
      subUserInfo: null,
      isEdit: false,

      headers: [
        { text: "Name", value: "fullName", sortable: true },
        { text: "Username", value: "username", sortable: true },
        { text: "Sub Role", value: "role", sortable: true },
        { text: "Actions", value: "actions", sortable: false },
      ],

      editedItem: {
        fullName: "",
        username: "",
        role: "",
        password: "",
        subUserId: "",
      },

      defaultItem: {
        fullName: "",
        username: "",
        role: "",
        password: "",
        subUserId: "",
      },

      allSubUsers: [],
      roles: ["ADMIN", "MANAGER", "OPERATOR"],

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

      snackbar: {
        value: false,
        message: "",
        color: "success",
      },
    };
  },

  created() {
    const loggedUser = JSON.parse(localStorage.getItem("auth"));
    this.auth = loggedUser;
    if (this.auth?.userType == "SUPER_USER") {
      this.fetchSubUsers();
    }
  },

  methods: {
    fetchSubUsers() {
      this.allSubUsers = [];
      axios
        .get(`${urls().subUser}/seller/${this.auth.userId}`)
        .then((response) => {
          this.allSubUsers = response.data;
        })
        .catch((error) => console.error("Error fetching sub-users:", error));
    },

    openAddDialog() {
      this.isEdit = false;
      this.dialog = true;
    },

    editItem(item) {
      this.isEdit = true;
      this.editedItem = Object.assign({}, item);
      this.dialog = true;
    },

    deleteItem(item) {
      this.editedItem = Object.assign({}, item);
      this.dialogDelete = true;
    },

    deleteItemConfirm() {
      const subuserid = this.editedItem.subUserId;
      axios
        .delete(`${urls().subUser}/${this.editedItem.subUserId}`)
        .then(() => {
          this.allSubUsers = this.allSubUsers.filter(
            (u) => u.subUserId !== subuserid
          );
          this.showSnackbar("Sub User deleted successfully", "error");
        })
        .catch((error) => console.error("Error deleting sub-user:", error));

      this.closeDelete();
    },

    save(userId) {
      console.log(userId);

      this.editedItem.sellerId = this.auth.userId;
      const request = this.isEdit
        ? axios.put(
            `${urls().subUser}/${this.editedItem.subUserId}`,
            this.editedItem
          )
        : axios.post(`${urls().subUser}/create`, this.editedItem);

      request
        .then(() => {
          this.fetchSubUsers();
          this.showSnackbar("Sub User saved successfully", "success");
        })
        .catch((error) => console.error("Error saving sub-user:", error));

      this.close();
    },

    close() {
      this.dialog = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
      });
    },

    closeDelete() {
      this.dialogDelete = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
      });
    },

    showSnackbar(message, color) {
      this.snackbar.value = true;
      this.snackbar.message = message;
      this.snackbar.color = color;
    },
  },
};
</script>

<style scoped>
.v-data-table {
  border-radius: 16px;
}
</style>
