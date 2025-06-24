import React, {useEffect, useState} from "react";
import {Navigate, Route, Routes} from "react-router-dom";
import PdfDragAndDropUploader from "./component/pdfDragAndDropUploader";
import AuthorizationComponent from "./component/authorization";

function App() {
    const [isLoggedIn, setIsLoggedIn] = useState(
        () => localStorage.getItem('logged_user') !== null
    );

    useEffect(() => {
        localStorage.setItem('logged_user', JSON.stringify(isLoggedIn));
    }, [isLoggedIn]);


    const logIn = () => setIsLoggedIn(true);
    const logOut = () => setIsLoggedIn(false);

    return (
        <>
            <Routes>
                <Route path="/authorization" element={<AuthorizationComponent/>}/>
                <Route path="/pdfUploader"  element={isLoggedIn ? <PdfDragAndDropUploader/> : <Navigate to='/login'/>}/>
            </Routes>
        </>
    );
}

export default App;