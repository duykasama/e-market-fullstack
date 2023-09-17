import { useEffect, useState } from "react";
import axios from "../lib/api/axios";

const useFetch = (url) => {
  const [data, setData] = useState(null);
  const [isPending, setIsPending] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const getData = async () => {
      try {
        const response = await axios.get(url, {
          headers: {
            "Content-Type": "application/json",
          },
          withCredentials: true,
        });

        setData(response.data.data);
        await new Promise((r) => setTimeout(r, 300));
        setIsPending(false);
      } catch (error) {
        setIsPending(false);
        setError(error.message);
        console.log("An error occurred: ", error.message);
      }
    };

    setIsPending(true);
    getData();
  }, [url]);

  return { data, isPending, error };
};

export default useFetch;
