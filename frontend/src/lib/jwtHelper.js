import { jwtPayload } from "jwt-payloader";

export const getExpirationTime = (token) => {
  const request = {
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  };

  const decodedPayload = jwtPayload(request);
  console.log(decodedPayload);
  return decodedPayload;
};
