import ReactDOM from 'react-dom'
import App from './App'
import "./assets/css/global"

import "./monaco"

import {StrictMode} from 'react';

ReactDOM.render(
  <StrictMode>
    <App/>
  </StrictMode>,
  document.getElementById('root')
);
