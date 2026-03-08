import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import TeamUpdate from './Context/TeamUpdate';
import { BrowserRouter } from 'react-router-dom';
import AuthProvider from './Context/AuthContext';
import NotificationProvider from './Context/NotificationContext';
import { Provider } from 'react-redux';
import store from './redux/store';
import { ToastContainer } from 'react-toastify';
import "react-toastify/dist/ReactToastify.css";

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <AuthProvider>
      <Provider store={store}>
        <BrowserRouter>
          <TeamUpdate>
            <App />
            <ToastContainer/>
          </TeamUpdate>
        </BrowserRouter>
      </Provider>
    </AuthProvider>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
