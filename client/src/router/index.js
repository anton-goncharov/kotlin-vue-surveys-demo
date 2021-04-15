import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from "@/views/Login.vue"
import Main from "@/views/Main.vue"
import Register from "@/views/Register.vue"

Vue.use(VueRouter)

export const router = new VueRouter({
    mode: 'history',
    routes: [
        {
            path: "/",
            name: "main",
            component: Main
        },
        {
            path: "/login",
            name: "login",
            component: Login
        },
        {
            path: "/register",
            name: "register",
            component: Register
        },

        // otherwise redirect to home
        { path: '*', redirect: '/' }
    ]
})

router.beforeEach((to, from, next) => {
    const publicPages = ['/login', '/register'];
    const authRequired = !publicPages.includes(to.path);
    const loggedIn = localStorage.getItem('user');

    // redirect to login page if not logged in and trying to access a restricted page
    if (authRequired && !loggedIn) {
        return next('/login');
    }

    next();
})