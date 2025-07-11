import React from "react";
import {Route, Routes} from "react-router-dom";
import PdfDragAndDropUploader from "./page/pdfDragAndDropUploader";
import AuthorizationComponent from "./page/authorization";
import AuthorizationProvider from "./component/authorizationProvider";
import PrivateRoute from "./component/privateRoute";

function App() {

    return (
        <>
            <AuthorizationProvider>
                <Routes>
                        <Route path="/authorization" element={<AuthorizationComponent/>}/>
                        <Route element={<PrivateRoute/>}>
                            <Route path="/pdfUploader" element={<PdfDragAndDropUploader/>}/>
                        </Route>
                </Routes>
            </AuthorizationProvider>
        </>
    );
}

export default App;