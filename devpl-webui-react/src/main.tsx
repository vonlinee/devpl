import ReactDOM from "react-dom/client";
import App from "./App";
import "./assets/css/global";

import "./components/editor/monaco";

import React from "react";

let container: HTMLElement | null = document.getElementById("root");

if (container) {
  const root = ReactDOM.createRoot(container);
  root.render(
    <React.StrictMode>
      <App />
    </React.StrictMode>
  );
}