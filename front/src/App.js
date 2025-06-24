import React from "react";
import {Route, Routes} from "react-router-dom";
import PdfUploaderComponent from "./component/pdfUploader";
import AuthorizationComponent from "./component/authorizationComponent";

function App() {

    return (
        <>
            <Routes>
                <Route path="/authorization" element={<AuthorizationComponent/>}/>
                <Route path="/pdfUploader" element={<PdfUploaderComponent/>}/>
            </Routes>
        </>
    );
}

export default App;