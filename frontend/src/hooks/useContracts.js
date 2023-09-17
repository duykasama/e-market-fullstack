import { useEffect, useState } from "react";
import axios from "../lib/api/axios";

const useContracts = () => {
  const [contracts, setContracts] = useState();
  const [isPending, setIsPending] = useState(false);

  useEffect(() => {
    const getContracts = async () => {
      try {
        const response = await axios.get("/contracts", {
          headers: {
            "Content-Type": "application/json",
          },
          withCredentials: true,
        });

        setContracts(response.data);
      } catch (error) {
        console.log("An error occurred");
        console.log(error.message);
        setContracts([]);
      }
    };
    getContracts();
  }, []);

  return { contracts };
};

export default useContracts;
