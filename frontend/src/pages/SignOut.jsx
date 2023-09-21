import { Navigate, useLocation } from "react-router-dom";
import useAuth from "../hooks/useAuth";
import { useContext } from "react";
import AuthContext from "../context/AuthProvider";

function SignOut() {
  const { setAuth } = useContext(AuthContext);
  setAuth({});
  return <Navigate to={"/"} replace />;
}

export default SignOut;
