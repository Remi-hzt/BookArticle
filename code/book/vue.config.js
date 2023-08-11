module.exports = {
  //transpileDependencies: true
  devServer:{//设置开发环境代理访问路径
    port: 8080, // 端口号
    https: false, // https:{type:Boolean}
    // 配置代理
    proxy:{
      '/api':{
        target: 'http://localhost:8088/',
        secure: false, //https为true
        changeOrigin: true, //允许跨域
        //ws: true, //是否代理websockets
        pathRewrite:{
          '^/api':''
        }
      }
    }
  },
  configureWebpack: {
    resolve: {
      alias: {
        'el-download': 'element-ui/packages/download',
      },
    },
  },
}
