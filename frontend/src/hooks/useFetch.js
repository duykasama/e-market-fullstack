import { useEffect, useState } from "react";
import useAxiosPrivate from "./useAxiosPrivate";
import { useLocation, useNavigate } from "react-router-dom";
import useAuth from "./useAuth";

const useFetch = (endpoint, reload) => {
  const [data, setData] = useState(null);
  const [isPending, setIsPending] = useState(false);
  const [error, setError] = useState(null);
  const axiosPrivate = useAxiosPrivate();
  const navigate = useNavigate();
  const location = useLocation();
  const { setAuth } = useAuth();

  useEffect(() => {
    const getData = async () => {
      try {
        const response = await axiosPrivate.get(endpoint);

        setData(response.data.data);
        await new Promise((r) => setTimeout(r, 300));
        setIsPending(false);
      } catch (error) {
        setIsPending(false);
        setAuth({});
        navigate("/sign-in", { state: { from: location }, replace: true });
        setError(error.message);
      }
    };

    setIsPending(true);
    getData();
  }, [endpoint, reload]);

  return { data, isPending, error };
};

export default useFetch;
