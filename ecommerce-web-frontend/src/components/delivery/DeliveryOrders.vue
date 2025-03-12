<template>
  <v-data-table
    :headers="headers"
    :items="allOrders"
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
        <v-toolbar-title>My Deliveries</v-toolbar-title>
        <v-divider class="mx-4" inset vertical></v-divider>
        <v-spacer></v-spacer>
        <v-dialog v-model="dialog" max-width="500px">
          <v-card>
            <v-form @submit.prevent="save()" v-model="productInfo">
              <v-card-title>
                <span class="text-h5">Edit Order Status</span>
              </v-card-title>

              <v-card-text>
                <v-container>
                  <v-row>
                    <v-col cols="12" sm="6" md="4">
                      <v-text-field
                        v-model="editedItem.orderId"
                        :rules="[rules.required]"
                        label="Order Id"
                        disabled
                      ></v-text-field>
                    </v-col>

                    <v-col cols="12" sm="6" md="4">
                      <v-select
                        :rules="[rules.required]"
                        :items="status"
                        v-model="editedItem.status"
                        label="Order Status"
                        item-text="statName"
                        item-value="status"
                      >
                      </v-select>

                      <!-- <v-text-field
                      v-model="editedItem.categoryId"
                      label="Category"
                    ></v-text-field> -->
                    </v-col>
                  </v-row>
                </v-container>
              </v-card-text>

              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="close">
                  Cancel
                </v-btn>
                <v-btn
                  color="blue darken-1"
                  text
                  type="submit"
                  :disabled="!productInfo"
                >
                  Save
                </v-btn>
              </v-card-actions>
            </v-form>
          </v-card>
        </v-dialog>
        <v-dialog v-model="dialogDelete" max-width="500px">
          <v-card>
            <v-card-title class="text-h5"
              >Are you sure you want to delete this item?</v-card-title
            >
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" text @click="closeDelete"
                >Cancel</v-btn
              >
              <v-btn color="blue darken-1" text @click="deleteItemConfirm"
                >OK</v-btn
              >
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-toolbar>
    </template>
    <template v-slot:[`item.actions`]="{ item }">
      <!-- v-slot:item.actions="{ item }" -->
      <v-icon small class="mr-2" @click="editItem(item)"> mdi-pencil </v-icon>
      <v-icon small @click="deleteItem(item)"> mdi-delete </v-icon>
    </template>
    <template v-slot:no-data>
      Orders are not added yet.
      <!-- <v-btn color="primary" @click="initialize"> Reset </v-btn> -->
    </template>
  </v-data-table>
</template>

<script>
import axios from "axios";
import urls from "@/urls";

export default {
  data: () => ({
    dialog: false,
    dialogDelete: false,
    productInfo: null,
    headers: [
      {
        text: "Order ID",
        align: "start",
        sortable: false,
        value: "orderId",
      },
      { text: "Status", value: "status" },
      { text: "Product Name", value: "productName" },
      { text: "Product Description", value: "productDesc" },
      { text: "Total Cost", value: "totalCost" },
      { text: "Actions", value: "actions", sortable: false },
    ],
    editedItem: {
      orderId: "",
      status: "",
      productName: "",
      productDesc: "",
      totalCost: 0,
      deliveryId: 0,
    },
    defaultItem: {
      orderId: "",
      status: "",
      productName: "",
      productDesc: "",
      deliveryId: 0,
      totalCost: 0,
    },
    status: [
      {
        statId: 0,
        statName: "CREATED",
      },
      {
        statId: 1,
        statName: "PICK_UP",
      },
      {
        statId: 2,
        statName: "IN_PROCESSING",
      },
      {
        statId: 3,
        statName: "COMPLETED",
      },
    ],
    rules: {
      required: (value) => !!value || "Required.",
    },
    allOrders: [],
    auth: [],
    snackbar: {
        value: false,
        message: "",
        color: "",
      },
  }),

  computed: {},

  watch: {
    dialog(val) {
      val || this.close();
    },
    dialogDelete(val) {
      val || this.closeDelete();
    },
  },

  created() {
    this.initialize();

    this.auth = JSON.parse(localStorage.getItem("auth"));
    console.log(this.auth);
    this.getAllOrders(this.auth.userId);
  },

  methods: {
    initialize() {
      this.allOrders = [
        // {
        //   name: "Puma",
        //   descr: "Shoes",
        //   cost: 2000,
        //   discount: 10.35,
        //   category: 1,
        // },
      ];
    },

    editItem(item) {
      this.editedIndex = this.allOrders.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialog = true;
    },

    deleteItem(item) {
      this.editedIndex = this.allOrders.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialogDelete = true;
    },

    deleteItemConfirm() {
      axios
        .delete(urls().orders + "/" + this.editedIndex, {})
        .then((response) => {
          this.allOrders.splice(this.editedIndex, 1);
          console.log(response.data);
          this.snackbar.message = "Order deleted successfully";
          this.snackbar.color = "red";
          this.snackbar.value = true;
        })
        .catch((error) => {
          console.log(error);
          if (error.response.status) {
            this.snackbar.message = error.response.data;
            this.snackbar.color = "red";
            this.snackbar.value = true;
          }
        });
      this.closeDelete();
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

    save() {
      const status = this.editedItem.status;
      axios
        .put(urls().orders + "/order-status", {
          deliveryId: this.auth.userId,
          orderId: this.editedItem.orderId,
          status: this.editedItem.status,
        })
        .then((response) => {
          console.log(response.data);
          this.allOrders[this.editedIndex].status = status;
          this.snackbar.message = "Status updated successfully";
          this.snackbar.color = "green";
          this.snackbar.value = true;
        })
        .catch((error) => {
          console.log(error);
          if (error.response.status) {
            this.snackbar.message = error.response.data;
            this.snackbar.color = "red";
            this.snackbar.value = true;
          }
        });
      this.close();
    },

    getAllOrders(value) {
      axios.get(urls().orders + "/delivery/" + value, {}).then((response) => {
        this.allOrders = response.data;
        console.log(this.allOrders);
      });
    },
  },
};
</script>
