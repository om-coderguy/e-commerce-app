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
import Unauthorized from '@/components/home/Unauthorized.vue'
import ProductDetails from '@/components/site-user/ProductDetails.vue'


Vue.use(Router)

// User roles
const UserTypes = {
  SITE_USER: 'SITE_USER',
  SELLER: 'SELLER',
  DELIVERY: 'DELIVERY',
};

// Helper function to get user details from auth token
function getUser() {
  const token = localStorage.getItem('auth');
  console.log("Token: ", token);
  
  if (!token) return null; // If no token, return null

  try {
    // Parse the JSON directly without Base64 decoding
    const payload = JSON.parse(token); 
    console.log("Parsed Payload: ", payload);
    return payload; // Example: { userType: 'SITE_USER', name: 'Omkar' }
  } catch (e) {
    console.error("Error parsing token:", e);
    return null;
  }
}


const router = new Router({
  mode: 'history',
  routes: [
    // {vue
    //   path: '/',
    //   name: 'Dashboard',
    //   component: DashBoard
    // },
    {
      path: '/category/:id',
      name: 'Categories',
      component: CategoryContent,
      meta: { requiresAuth: true, allowedRoles: [UserTypes.SITE_USER] },
    },
    {
      path: '/cart',
      name: 'Cart',
      component: CartProducts,
      meta: { requiresAuth: true, allowedRoles: [UserTypes.SITE_USER] },
    },
    {
      path: '/orders',
      name: 'Orders',
      component: UserOrders,
      meta: { requiresAuth: true, allowedRoles: [UserTypes.SITE_USER] },
    },
    {
      path: '/seller',
      name: 'Seller',
      component: SellerHome,
      meta: { requiresAuth: true, allowedRoles: [UserTypes.SELLER] },
    },
    {
      path: '/delivery',
      name: 'Delivery',
      component: DeliveryHome,
      meta: { requiresAuth: true, allowedRoles: [ UserTypes.DELIVERY] },
    },
    {
      path: '/register/seller',
      name: 'Register Seller',
      component: RegisterSeller,
      meta: { requiresAuth: true, allowedRoles: [ UserTypes.SELLER] },
    },
    {
      path: '/register/delivery',
      name: 'Register Delivery',
      component: RegisterDelivery,
    },
    {
      path: '/buy/:productId',
      name: 'Buy Product',
      component: BuyProduct,
      meta: { requiresAuth: true, allowedRoles: [UserTypes.SITE_USER] },
    },
    {
      path: '/ProductDetails/:productId',
      name: 'Product Details',
      component: ProductDetails,
    },
    {
      path: '/unauthorized',
      name: 'Unauthorized',
      component: Unauthorized,
    }
  ]
});

  router.beforeEach((to, from, next) => {
    const user = getUser();
    console.log(user);
    
  
    // Check if the route requires authentication
    if (to.meta.requiresAuth) {
      if (!user) {
        return next('/'); // Redirect to login if not authenticated
      }
  
      // Check if the user's role is allowed
      if (!to.meta.allowedRoles.includes(user.userType)) {
        return next('/unauthorized'); // Redirect if not authorized
      }
    }
  
    next(); // Proceed to the route
  
})

export default router;