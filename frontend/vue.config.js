module.exports = {
  productionSourceMap: false,
  devServer: {
    port: 8090,
    disableHostCheck: true,
    proxy: {
      '/backend': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
    }
  },
  chainWebpack: (config) => {
    config.resolve.alias
      .set('@', require('path').resolve(__dirname, 'src'));
  }
}
