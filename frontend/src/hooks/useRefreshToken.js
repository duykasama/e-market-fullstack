import { REFRESH_TOKEN_ENDPOINT } from "../data/apiInfo";
import axios from "../lib/api/axios";
import useAuth from "./useAuth";

function useRefreshToken() {
  const { setAuth } = useAuth();

  const refresh = async () => {
    const response = await axios.get(REFRESH_TOKEN_ENDPOINT, {
      withCredentials: true,
    });
    setAuth((prev) => ({ ...prev, roles: ["USER"], accessToken: response?.data?.data }));

    return response?.data?.data;
  };
  return refresh;
}

export default useRefreshToken;
