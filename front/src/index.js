import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import {BrowserRouter} from "react-router-dom";
import {HeroUIProvider} from "@heroui/react";

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
        <BrowserRouter>
            <HeroUIProvider>
                <main className="dark text-foreground bg-background">
                    <App />
                </main>
            </HeroUIProvider>
        </BrowserRouter>

);

reportWebVitals();
