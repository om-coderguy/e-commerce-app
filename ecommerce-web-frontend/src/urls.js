export default () => {
  if (window.location.href.indexOf("localhost:8084") > -1) {
    return {
      login: "http://localhost:8083/login",
      registerUser: "http://localhost:8083/api/users",
      seller: "http://localhost:8083/api/sellers",
      products: "http://localhost:8083/api/products",
      cart: "http://localhost:8083/api/carts",
      categories: "http://localhost:8083/api/categories",
      orders: "http://localhost:8083/api/orders",
      inventory:"http://localhost:8083/api/inventories",
      subUser:"http://localhost:8083/api/subusers",
      // topProducts: 'http://localhost:8083/api/products/top',
      // recentProducts :'http://localhost:8083/api/products/recent',
      // singleProduct: 'http://localhost:8083/api/products/byId',
    };
  }
};
