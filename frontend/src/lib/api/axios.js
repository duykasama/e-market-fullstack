import axios from "axios";
import { DOMAIN } from "../../data/apiInfo";

export default axios.create({
  baseURL: DOMAIN,
});

export const axiosPrivate = axios.create({
  baseURL: DOMAIN,
  headers: {
    "Content-Type": "application/json",
    withCredentials: true,
  },
});
