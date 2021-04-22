import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from "@/views/Login.vue"
import Main from "@/views/Main.vue"
import Register from "@/views/Register.vue"
import Survey from "@/views/Survey";
import Profile from "@/views/Profile";
import UserManagement from "@/views/UserManagement";

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
        {
            path: "/profile",
            name: "profile",
            component: Profile
        },
        {
            path: "/user-management",
            name: "user-management",
            component: UserManagement
        },
        {
            path: "/surveys/new",
            name: "surveyNew",
            component: Survey
        },
        {
            path: "/surveys/:surveyUuid", // TODO use survey slug name instead of ID
            name: "surveyById",
            component: Survey,
            props: true
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