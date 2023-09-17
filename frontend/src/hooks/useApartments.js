import { useEffect, useState } from "react";
import axios from "../lib/api/axios";

const useApartments = () => {
  const [apartments, setApartments] = useState();
  const [isPending, setIsPending] = useState(false);

  useEffect(() => {
    const getApartments = async () => {
      try {
        const response = await axios.get("/apartments", {
          headers: {
            "Content-Type": "application/json",
          },
          withCredentials: true,
        });

        setApartments(response.data);
      } catch (error) {
        console.log("An error occurred");
        console.log(error.message);
        setApartments([]);
      }
    };
    getApartments();
  }, []);

  return { apartments };
};

export default useApartments;
