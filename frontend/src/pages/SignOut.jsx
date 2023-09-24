import { Navigate } from "react-router-dom";
import { useContext } from "react";
import AuthContext from "../context/AuthProvider";
import axios from "../lib/api/axios";
import { SIGN_OUT_ENDPOINT } from "../data/apiInfo";

function SignOut() {
  const { setAuth } = useContext(AuthContext);
  setAuth({});

  const signOut = async () => {
    await axios.delete(SIGN_OUT_ENDPOINT, {
      headers: {
        "Content-Type": "application/json",
      },
      withCredentials: true,
    });
  };

  signOut();

  return <Navigate to={"/"} replace />;
}

export default SignOut;
