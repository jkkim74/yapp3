const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,
  publicPath: "/mission/",
  outputDir: "../src/main/webapp/mission",
  indexPath: "index.html",
  devServer: {
    port: 8081,
    proxy: "http://localhost:8080"
  },

  chainWebpack: config => {
    const svgRule = config.module.rule("svg");
    svgRule.uses.clear();
    svgRule.use("vue-svg-loader").loader("vue-svg-loader");
  }

})
