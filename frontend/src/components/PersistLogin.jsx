import { useEffect, useState } from "react";
import useRefreshToken from "../hooks/useRefreshToken";
import useAuth from "../hooks/useAuth";
import Loading from "./ui/Loading";
import { Outlet } from "react-router-dom";

function PersistLogin() {
  const [isPending, setIsPending] = useState(true);
  const refresh = useRefreshToken();
  const { auth } = useAuth();

  useEffect(() => {
    const verifyRefreshToken = async () => {
      try {
        await refresh();
      } catch (error) {
        console.error(error);
      } finally {
        setIsPending(false);
      }
    };

    !auth?.accessToken ? verifyRefreshToken() : setIsPending(false);
  }, []);

  return <>{isPending ? <Loading /> : <Outlet />}</>;
}

export default PersistLogin;
