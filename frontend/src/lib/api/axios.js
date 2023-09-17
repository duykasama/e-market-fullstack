import axios from "axios";
import { DOMAIN } from "../../data/apiInfo";

export default axios.create({
  baseURL: DOMAIN,
});
