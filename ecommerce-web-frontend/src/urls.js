export default () => {
  if (window.location.href.indexOf("localhost:8081") > -1) {
    return {
      login: "http://localhost:8080/login",
      registerUser: "http://localhost:8080/api/users",
      seller: "http://localhost:8080/api/sellers",
      products: "http://localhost:8080/api/products",
      cart: "http://localhost:8080/api/carts",
      categories: "http://localhost:8080/api/categories",
      orders: "http://localhost:8080/api/orders",
      inventory:"http://localhost:8080/api/inventories",
      // topProducts: 'http://localhost:8080/api/products/top',
      // recentProducts :'http://localhost:8080/api/products/recent',
      // singleProduct: 'http://localhost:8080/api/products/byId',
    };
  }
};
