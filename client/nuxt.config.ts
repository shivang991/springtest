// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: "2024-04-03",
  devtools: { enabled: true },
  css: ["~/assets/index.css"],
  modules: ["@nuxt/fonts", "@nuxt/image", "@nuxtjs/color-mode"],
});
