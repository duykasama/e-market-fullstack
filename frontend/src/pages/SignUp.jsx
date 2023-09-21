import { useState } from "react";
import { Link } from "react-router-dom";
import axios from "../lib/api/axios";
import { REGISTER_ENDPOINT } from "../data/apiInfo";
import Loading from "../components/ui/Loading";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faCheckCircle,
  faXmark,
  faXmarkCircle,
} from "@fortawesome/free-solid-svg-icons";

function SignUp() {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    confirmPassword: "",
  });
  const [isPending, setIsPending] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);
  const passwordPattern =
    /^(?=.*[0-9])(?=.*[!@#$%^&*()-_+=])(?=.*[A-Z]).{12,}$/;
  const [passwordWarning, setPasswordWarning] = useState("");
  const [confirmPasswordWarning, setConfirmPasswordWarning] = useState("");

  const handleFormSubmit = async (event) => {
    event.preventDefault();
    if (passwordWarning != null || confirmPasswordWarning != null) {
      return;
    }

    setIsPending(true);

    try {
      console.log(formData);
      const response = await axios.post(REGISTER_ENDPOINT, formData, {
        params: formData,
      });
      if (response.data?.statusCode === 200) {
        setSuccess(response.data?.message);
      }
    } catch (e) {
      setError("User already exists");
    }

    await new Promise((r) => setTimeout(r, 300));
    setIsPending(false);
  };

  const handleFormDataChange = (event) => {
    const eventName = event.target.name;
    const eventValue = event.target.value;
    if (eventName === "password") {
      if (eventValue != "" && !passwordPattern.test(eventValue)) {
        setPasswordWarning("*Your password is too weak");
      } else {
        setPasswordWarning(null);
      }
      if (formData.confirmPassword != "") {
        if (eventValue == formData.confirmPassword) {
          setConfirmPasswordWarning(null);
        } else {
          setConfirmPasswordWarning("*Your passwords doesn't match");
        }
      }
    }

    if (eventName === "confirmPassword") {
      if (eventValue != "" && eventValue != formData.password) {
        setConfirmPasswordWarning("*Your passwords doesn't match");
        console.log(eventName);
      } else {
        setConfirmPasswordWarning(null);
      }
    }

    setFormData((prev) => ({
      ...prev,
      [eventName]: eventValue,
    }));
  };

  const handleResetForm = () => {
    setError(null);
    setSuccess(null);
    setFormData({
      email: "",
      password: "",
      confirmPassword: "",
    });
  };

  return (
    <div className="w-full h-full p-8 flex justify-center items-center">
      {isPending && <Loading />}
      {!isPending && error && (
        <div className="bg-slate-500 p-4 rounded-lg flex flex-col ">
          <div className="flex justify-end">
            <FontAwesomeIcon
              icon={faXmark}
              onClick={handleResetForm}
              className="p-2 hover:bg-slate-400 cursor-pointer rounded-md transition"
            />
          </div>
          <div className="flex flex-col justify-center items-center gap-3">
            <FontAwesomeIcon
              icon={faXmarkCircle}
              className="text-5xl text-red-500"
            />
            {error && <p className="font-semibold text-red-500">{error}</p>}
          </div>
        </div>
      )}
      {!isPending && success && (
        <div className="bg-slate-500 p-4 rounded-lg flex flex-col ">
          <div className="flex justify-end">
            <FontAwesomeIcon
              icon={faXmark}
              onClick={handleResetForm}
              className="p-2 hover:bg-slate-400 cursor-pointer rounded-md transition"
            />
          </div>
          <div className="flex flex-col justify-center items-center gap-3">
            <FontAwesomeIcon
              icon={faCheckCircle}
              className="text-5xl text-green-700"
            />
            <p className="font-semibold text-green-700">
              Registered successfully
            </p>
          </div>
        </div>
      )}
      {!isPending && !success && !error && (
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
              required
              onChange={handleFormDataChange}
              className="rounded-md p-1"
            />
          </div>
          <div className="flex flex-col">
            <div className="flex justify-between items-center gap-6">
              <label htmlFor="password" className="font-semibold">
                Password
              </label>
              <input
                type="password"
                id="password"
                name="password"
                required
                onChange={handleFormDataChange}
                className="rounded-md p-1"
              />
            </div>
            {passwordWarning && (
              <small className="text-red-600 italic">{passwordWarning}</small>
            )}
          </div>
          <div className="flex flex-col">
            <div className="flex justify-between items-center gap-6">
              <label htmlFor="confirmPassword" className="font-semibold">
                Confirm password
              </label>
              <input
                type="password"
                id="confirmPassword"
                name="confirmPassword"
                required
                onChange={handleFormDataChange}
                className="rounded-md p-1"
              />
            </div>
            {confirmPasswordWarning && (
              <small className="text-red-600 italic">
                {confirmPasswordWarning}
              </small>
            )}
          </div>
          <small>
            <span>Already have an account?&nbsp;</span>
            <Link to="/sign-in" className="text-blue-700 italic">
              Sign in
            </Link>
          </small>
          <div className="flex justify-center items-center">
            <button className="bg-slate-600 w-fit py-2 px-4 rounded-md text-white font-semibold hover:bg-slate-800 transition active:scale-95">
              Sign up
            </button>
          </div>
        </form>
      )}
    </div>
  );
}

export default SignUp;
