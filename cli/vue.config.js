const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave:false,
  devServer: {
    overlay: {
      warnings: false, //不显示警告
    },
  },
  })

module.exports = {
  publicPath : './',
  devServer: {
    port: 8082,   // 端口号
    // disableHostCheck: true
    allowedHosts: "all",
  },
};