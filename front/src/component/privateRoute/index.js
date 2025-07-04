import React from "react";
import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../authorizationProvider/index";

const PrivateRoute = () => {
    const user = useAuth();
    if (!user.token) {
        return <Navigate to="/authorization" />;
    }
    return <Outlet />;
};

export default PrivateRoute;