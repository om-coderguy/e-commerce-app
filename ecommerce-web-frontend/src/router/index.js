import Vue from 'vue'
import Router from 'vue-router'
import CategoryContent from '../components/category/CategoryContent.vue'
import CartProducts from '../components/site-user/CartProducts.vue'
import UserOrders from '../components/site-user/UserOrders.vue'
import SellerHome from '../components/seller/SellerHome.vue'
import RegisterSeller from '../components/seller/RegisterSeller.vue'
import RegisterDelivery from '../components/delivery/RegisterDelivery.vue'
import DeliveryHome from '../components/delivery/DeliveryHome.vue'
import BuyProduct from '../components/site-user/BuyProduct.vue'


Vue.use(Router)

export default new Router({
  routes: [
    // {vue
    //   path: '/',
    //   name: 'Dashboard',
    //   component: DashBoard
    // },
    {
      path: '/category/:id',
      name: 'Categories',
      component: CategoryContent
    },
    {
      path: '/cart',
      name: 'Cart',
      component: CartProducts
    },
    {
      path: '/orders',
      name: 'Orders',
      component: UserOrders
    },
    {
      path: '/seller',
      name: 'Seller',
      component: SellerHome
    },
    {
      path: '/delivery',
      name: 'Delivery',
      component: DeliveryHome
    },
    {
      path: '/register/seller',
      name: 'Register Seller',
      component: RegisterSeller
    },
    {
      path: '/register/delivery',
      name: 'Register Delivery',
      component: RegisterDelivery
    },
    {
      path: '/buy/:productId',
      name: 'Buy Product',
      component: BuyProduct
    },
  ]
})
