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
        <v-toolbar-title class="text-h5 ">My Products</v-toolbar-title>
        <v-divider class="mx-4" inset vertical></v-divider>
        <v-spacer></v-spacer>
        <v-dialog v-model="dialog" max-width="500px">
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              color="green lighten-3"
              class="mb-2"
              v-bind="attrs"
              v-on="on"
            >
              Add Product
            </v-btn>
          </template>
          <v-card>
            <v-form @submit.prevent="save(editedItem.id)" v-model="productInfo">
              <v-card-title>
                <span class="text-h5">{{ formTitle }}</span>
              </v-card-title>

              <v-card-text>
                <v-container>
                  <v-row>
                    <v-col cols="12" sm="6" md="4">
                      <v-text-field
                        v-model="editedItem.name"
                        :rules="[rules.required]"
                        label="Product name"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="12" sm="6" md="4">
                      <v-text-field
                        v-model="editedItem.descr"
                        :rules="[rules.required]"
                        label="Description"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="12" sm="6" md="4">
                      <v-text-field
                        v-model="editedItem.cost"
                        :rules="[rules.required]"
                        label="Cost"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="12" sm="6" md="4">
                      <v-text-field
                        v-model="editedItem.discount"
                        :rules="[rules.required]"
                        label="Discount"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="12" sm="6" md="4">
                      <v-select
                        :rules="[rules.required]"
                        :items="categories"
                        v-model="editedItem.categoryId"
                        label="Category"
                        item-text="catName"
                        item-value="catId"
                      >
                      </v-select>

                      <!-- <v-text-field
                      v-model="editedItem.categoryId"
                      label="Category"
                    ></v-text-field> -->
                    </v-col>
                    <v-col cols="12">
                      <v-file-input
                        v-model="editedItem.photo"
                        label="Upload Product Image"
                        accept="image/*"
                        show-size
                      ></v-file-input>
                    </v-col>
                  </v-row>
                </v-container>
              </v-card-text>

              <v-card-actions>
                <v-btn
                  color="blue darken-1"
                  text
                  @click="addSpecDialogue"
                  v-if="addSpecFlag"
                >
                  Add Spec
                </v-btn>
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

        <v-dialog v-model="dialogSpec" persistent max-width="500px">
          <v-card>
            <v-card-title class="text-h5">
              Product Specifications
            </v-card-title>

            <v-form
              class="d-flex mx-5 mb-5"
              @submit.prevent="addSpec()"
              v-model="productInfo"
            >
              <v-row class="align-center">
                <v-col cols="12" sm="6" md="4">
                  <v-text-field
                    v-model="editedSpec.key"
                    :rules="[rules.required]"
                    label="key"
                  ></v-text-field>
                </v-col>
                <v-col cols="12" sm="6" md="4">
                  <v-text-field
                    v-model="editedSpec.value"
                    :rules="[rules.required]"
                    label="value"
                  ></v-text-field>
                </v-col>
                <v-col cols="12" sm="6" md="4">
                  <v-btn class="green lighten-3" type="submit">
                    Add
                    <v-icon class="ml-2">mdi-plus</v-icon>
                  </v-btn>
                </v-col>
              </v-row>
            </v-form>

            <v-card-text
              class="body d-flex"
              color="black"
              v-for="(key, value) in editedItem.specifications"
              :key="value"
            >
              <div class="key-spec" style="width: 30%">{{ value }}</div>
              :
              <div class="value-spec" style="width: 30%">
                &emsp;&emsp;{{ key }}
              </div>
              <v-btn color="" text>
                <v-icon dark> mdi-pencil</v-icon>
              </v-btn>
              <v-btn color="" text>
                <v-icon dark> mdi-delete</v-icon>
              </v-btn>
            </v-card-text>
            <v-card-actions>
              <v-btn class="green lighten-3 ma-5" @click="dialogSpec = false">
                Back
              </v-btn> </v-card-actions
            >.
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
    dialogSpec: false,
    productInfo: null,
    snackbar: {
      value: false,
      message: "",
      color: "",
    },
    headers: [
      {
        text: "Product Name",
        align: "start",
        sortable: false,
        value: "name",
      },
      { text: "Description", value: "descr" },
      { text: "Cost", value: "cost" },
      { text: "Discount", value: "discount" },
      { text: "Category", value: "categoryId" },
      { text: "Actions", value: "actions", sortable: false },
    ],
    editedIndex: -1,
    editedItem: {
      id: 0,
      name: "",
      descr: "",
      cost: 0,
      discount: 0,
      categoryId: 0,
      specifications: {},
      photo: null,
    },

    editedSpec: {
      key: "",
      value: "",
    },
    defaultItem: {
      name: "",
      descr: "",
      cost: 0,
      discount: 0,
      categoryId: 0,
      specifications: {},
    },
    rules: {
      required: (value) => !!value || "Required.",
    },
    categories: [
      {
        catId: 1,
        catName: "Cloths",
      },
      {
        catId: 2,
        catName: "Electronics",
      },
      {
        catId: 3,
        catName: "Shoes",
      },
      {
        catId: 4,
        catName: "Books",
      },
      {
        catId: 5,
        catName: "Pens",
      },
    ],
    allProducts: [],
    auth: [],
  }),

  computed: {
    formTitle() {
      return this.editedIndex === -1 ? "New Product" : "Edit Product";
    },
    addSpecFlag() {
      return this.editedIndex === -1 ? false : true;
    },
  },

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
    this.allCates();
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

    addSpecDialogue() {
      this.dialogSpec = true;
    },

    addSpec() {
      axios
        .post(urls().products + "/" + this.editedItem.id + "/specifications", {
          // productId: this.editedItem.id,
          specName: this.editedSpec.key,
          specValue: this.editedSpec.value,
        })
        .then((response) => {
          console.log(response.data, typeof this.editedSpec.key);
          // const newSpec={[this.editedSpec.key]:this.editedSpec.value}
          const newSpec = {};
          newSpec[this.editedSpec.key] = this.editedSpec.value;
          console.log(Object.assign(this.editedItem.specifications, newSpec));
          this.editedItem.specifications = Object.assign(
            this.editedItem.specifications,
            newSpec
          );
        });
    },

    editItem(item) {
      this.editedIndex = this.allProducts.indexOf(item);
      this.editedItem = Object.assign({}, item);
      console.log(this.editedItem);
      this.dialog = true;
    },

    deleteItem(item) {
      this.editedIndex = this.allProducts.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialogDelete = true;
    },

    deleteItemConfirm() {
      axios
        .delete(urls().products + "/delete/" + this.editedItem.id, {})
        .then((response) => {
          console.log(response.data);
          this.snackbar.message = "Product deleted successfully";
          this.snackbar.color = "red";
          this.snackbar.value = true;
          this.allProducts.splice(this.editedIndex, 1);
        })
        .catch((error) => {
          console.log(error);
        });

      this.closeDelete();
    },

    close() {
      this.dialog = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
    },

    closeDelete() {
      this.dialogDelete = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
    },

    save(productId) {
      const formData = new FormData();
      formData.append("name", this.editedItem.name);
      formData.append("descr", this.editedItem.descr);
      formData.append("cost", this.editedItem.cost);
      formData.append("discount", this.editedItem.discount);
      formData.append("categoryId", this.editedItem.categoryId);
      formData.append("active", true);
      if (this.editedItem.photo) {
        formData.append("photo", this.editedItem.photo);
      }
      if (this.editedIndex > -1) {
        axios
          .put(urls().products + "/" + productId, 
            formData
          )
          .then((response) => {
            console.log(response.data);
          });
        Object.assign(this.allProducts[this.editedIndex], this.editedItem);
      } else {
        axios
          .post(urls().products, formData)
          .then((response) => {
            console.log(response.data);
          });
        this.allProducts.push(this.editedItem);
      }
      this.close();
    },

    getAllProducts(value) {
      axios.get(urls().products + "/seller/" + value, {}).then((response) => {
        this.allProducts = response.data;
        console.log(this.allProducts);
      });
    },
    async allCates() {
      await axios.get(urls().categories, {}).then((response) => {
        this.categories = response.data;
        console.log(this.category);
      });
    },
  },
};
</script>
