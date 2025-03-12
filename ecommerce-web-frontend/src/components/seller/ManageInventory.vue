<template>
  <v-data-table
    :headers="headers"
    :items="allProducts"
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
        <v-toolbar-title class="text-h5">My Inventory</v-toolbar-title>
        <v-divider class="mx-4" inset vertical></v-divider>
        <v-spacer></v-spacer>
        <v-dialog v-model="dialog" max-width="500px">
          <v-card>
            <v-form @submit.prevent="save(editedItem.id)" v-model="productInfo">
              <v-card-title>
                <span class="text-h5">Edit Order Status</span>
              </v-card-title>

              <v-card-text>
                <v-container>
                  <v-row>
                    <v-col cols="12" sm="6" md="4">
                      <v-text-field
                        v-model="editedItem.name"
                        :rules="[rules.required]"
                        label="Product Name"
                      ></v-text-field>
                    </v-col>

                    <v-col cols="12" sm="6" md="4">
                      <v-text-field
                        v-model="editedItem.totalQuantity"
                        :rules="[rules.required]"
                        label="Total Quantity"
                        type="number"
                      ></v-text-field>
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
      </v-toolbar>
    </template>
    <template v-slot:[`item.actions`]="{ item }">
      <!-- v-slot:item.actions="{ item }" -->
      <v-icon small class="mr-2" @click="editItem(item)"> mdi-pencil </v-icon>
    </template>
    <template v-slot:no-data>
      Products are not added yet.
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
        text: "Product Name",
        align: "start",
        sortable: false,
        value: "name",
      },
      { text: "Product Description", value: "descr" },
      { text: "Total Quantity", value: "totalQuantity" },
      { text: "Actions", value: "actions", sortable: false },
    ],
    editedIndex: -1,
    editedItem: {
      id: 0,
      name: "",
      totalQuantity: 0,
    },
    defaultItem: {
      id: 0,
      name: "",
      totalQuantity: 0,
    },
    rules: {
      required: (value) => !!value || "Required.",
    },
    allProducts: [],
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
    this.getAllProducts(this.auth.userId);
  },

  methods: {
    initialize() {
      this.allProducts = [
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
      this.editedIndex = this.allProducts.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialog = true;
    },

    close() {
      this.dialog = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
      });
    },

    save(value) {
      axios
        .post(urls().inventory, {
          productId: value,
          totalQuantity: this.editedItem.totalQuantity,
        })
        .then((response) => {
          console.log(response.data);
          this.snackbar.message = "Product updated successfully";
          this.snackbar.color = "green";
          this.snackbar.value = true;
        });
      Object.assign(this.allProducts[this.editedIndex], this.editedItem);
      this.close();
    },

    getAllProducts(value) {
      axios
        .get(urls().products + "/seller/inventory/" + value, {})
        .then((response) => {
          this.allProducts = response.data;
          console.log(this.allProducts);
        });
    },
  },
};
</script>