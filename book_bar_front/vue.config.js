const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false,
  devServer: {
    client: {
      overlay: true,
    },
    host: "localhost",
    port: 8080, // 端口号
    https: false, // https:{type:Boolean}
    open: false, //配置后自动启动浏览器
    hot: "only",
    // hotOnly: true, // 热更新
    // proxy: 'http://localhost:8080'   // 配置跨域处理,只有一个代理

    proxy: { //配置多个代理
      "/testIp": {
        target: "http://127.0.0.1:8088/api",
        changeOrigin: true,
        ws: true,//websocket支持
        secure: false,
        pathRewrite: {
          "^/testIp": "/"
        }
      }
    }

  }
})
