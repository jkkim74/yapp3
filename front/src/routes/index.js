// import Vue from 'vue';
// import VueRouter from 'vue-router';
//
// Vue.use(VueRouter);
//
// export default new VueRouter({
//     mode: 'history',
//     routes: [
//         {
//             path: '/yapp3',
//             redirect: '/mission/index.html'
//         }
//     ]
// })

import { createWebHistory, createRouter } from 'vue-router';

const routes = [
    {
        path: '/yapp3',
        redirect: '/mission/index.html'
    }
];

export const router = createRouter({
    history: createWebHistory(),
    routes,
});