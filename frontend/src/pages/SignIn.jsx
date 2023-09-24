import { useState } from "react";
import axios from "../lib/api/axios";
import useAuth from "../hooks/useAuth";
import Loading from "../components/ui/Loading";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCircleXmark, faXmark } from "@fortawesome/free-solid-svg-icons";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { AUTH_ENDPOINT } from "../data/apiInfo";

function SignIn() {
  const [isPending, setIsPending] = useState(false);
  const [formData, setFormData] = useState({ email: "", password: "" });
  const [error, setError] = useState(null);
  const location = useLocation();
  const navigate = useNavigate();
  const from = location.state?.from?.pathname || "/";

  const { setAuth } = useAuth();

  const handleFormSubmit = async (event) => {
    event.preventDefault();
    setIsPending(true);
    try {
      const response = await axios.post(
        AUTH_ENDPOINT,
        formData,
        {
          headers: {
            Accept: "*/*",
            "Content-Type": "application/json",
          },
          withCredentials: true,
          params: formData,
        }
      );

      if (response.data.statusCode === 200) {
        const accessToken = response?.data?.data;
        setAuth({
          user: formData.email,
          roles: ["USER"],
          accessToken,
        });
        navigate(from, { replace: true });
      } else {
        throw new Exception("Wrong username or password");
      }
    } catch (e) {
      console.error(e);
      if (e.code === "ERR_NETWORK") {
        setError("Cannot connect to server");
      } else {
        setError("Wrong username or password");
      }
    }
    await new Promise((r) => setTimeout(r, 500));
    setFormData({ email: "", password: "" });
    setIsPending(false);
  };

  const handleFormDataChange = (event) => {
    setFormData((prev) => ({
      ...prev,
      [event.target.name]: event.target.value,
    }));
  };

  return (
    <div className="w-full h-full p-8 flex justify-center items-center">
      {isPending && <Loading />}
      {!isPending && error && (
        <div className="p-6 flex flex-col bg-slate-400 shadow-lg shadow-gray-600 rounded-lg">
          <div className="flex justify-end">
            <FontAwesomeIcon
              icon={faXmark}
              onClick={() => setError(null)}
              className="p-2 rounded-md -mt-2 -mr-2 hover:bg-slate-400 cursor-pointer"
            />
          </div>
          <div className="flex flex-col justify-center items-center font-semibold gap-4">
            <FontAwesomeIcon
              icon={faCircleXmark}
              className="text-red-600 text-5xl bg-white rounded-full"
            />
            <p>{error}</p>
          </div>
        </div>
      )}
      {!isPending && !error && (
        <form
          onSubmit={handleFormSubmit}
          className="p-6 flex flex-col gap-8 bg-slate-400 shadow-lg shadow-gray-600 rounded-lg"
        >
          <div className="flex justify-between items-center">
            <label htmlFor="email" className="font-semibold">
              Email
            </label>
            <input
              type="email"
              id="email"
              name="email"
              autoComplete="username"
              required
              onChange={handleFormDataChange}
              className="rounded-md p-1"
            />
          </div>
          <div className="flex justify-between items-center gap-6">
            <label htmlFor="password" className="font-semibold">
              Password
            </label>
            <input
              type="password"
              id="password"
              name="password"
              autoComplete="current-password"
              required
              onChange={handleFormDataChange}
              className="rounded-md p-1"
            />
          </div>
          <small>
            <span>Don't have an account?&nbsp;</span>
            <Link to="/sign-up" className="text-blue-700 italic">
              Sign up
            </Link>
          </small>
          <div className="flex justify-center items-center">
            <button className="bg-slate-600 w-fit py-2 px-4 rounded-md text-white font-semibold hover:bg-slate-800 transition active:scale-95">
              Sign in
            </button>
          </div>
        </form>
      )}
    </div>
  );
}

export default SignIn;
