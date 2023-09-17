import { useEffect, useState } from "react";
import axios from "../lib/api/axios";

const useCustomers = () => {
  const [customers, setCustomers] = useState();
  const [isPending, setIsPending] = useState(false);

  useEffect(() => {
    const getCustomers = async () => {
      try {
        const response = await axios.get(
          "/customers/pagination?pageSize=5&offset=1",
          {
            headers: {
              "Content-Type": "application/json",
            },
            withCredentials: true,
          }
        );
console.log(response.data.data);
        setCustomers(response.data.data.content);
      } catch (error) {
        console.log("An error occurred");
        console.log(error.message);
        console.log(error);
        setCustomers([]);
      }
    };
    getCustomers();
  }, []);
  return { customers };
};

export default useCustomers;
