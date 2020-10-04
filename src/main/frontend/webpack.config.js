const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');

var path = require('path');

const SRC_DIR = path.resolve(__dirname, 'app');
const BUILD_DIR = path.resolve(__dirname, '../../../target/classes/static');
const INDEX_DIR = path.resolve(__dirname, '../resources/static');


console.log('##### BUILD_DIR', BUILD_DIR);
console.log('##### SRC_DIR', SRC_DIR);
console.log('##### INDEX_DIR', INDEX_DIR);


module.exports = {
  entry: SRC_DIR + '/app.js',
  devtool: 'source-map',
  cache: true,
  output: {
    path: BUILD_DIR,
    filename: 'app.[hash].js'
  },
  externals: {
    'react/addons': true,
    'react/lib/ExecutionEnvironment': true,
    'react/lib/ReactContext': true,
  },
  module: {
    rules: [
      {
        test: path.join(__dirname, '.'),
        exclude: /(node_modules)/,
        use: {
          loader: 'babel-loader',
          options: {
            cacheDirectory: true,
            presets: ['env', 'react']
          }
        }


      },
      {
        test: /\.(png|jpg|gif)$/,
        use: [
          {
            loader: 'file-loader',
            options: {}
          }
        ]
      }
    ]
  },


  resolve: {
    extensions: ['.js', '.jsx']
  },
  plugins: [
    new webpack.DefinePlugin({
      "process.env.BCKOFFICE_URL": "\'http://localhost:5000\'"
    }),
    new webpack.HotModuleReplacementPlugin(),
    new webpack.NamedModulesPlugin(),
    new HtmlWebpackPlugin({template: INDEX_DIR + '/index.html'})
  ],
  devServer: {
    port: 9090,
    proxy: {
      '/': {
        target: 'http://localhost:5000',
        secure: false,
        prependPath: false
      }
    },
    headers: {
      'Access-Control-Allow-Origin': '*'
    },
    publicPath: 'http://localhost:9090',
    historyApiFallback: true
  },
};
