import {
  faCircleCheck,
  faCircleXmark,
  faClose,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useState } from "react";
import axios from "../../lib/api/axios";
import Loading from "../ui/Loading";
import { APARTMENTS_ENDPOINT } from "../../data/apiInfo";
import useAuth from "../../hooks/useAuth";

function AddNewApartment({ onCloseModal }) {
  const [formData, setFormData] = useState({
    address: "",
    rentalPrice: 0,
    numberOfRooms: 0,
  });

  const [isPending, setIsPending] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState(null);
  const { auth } = useAuth();

  const handleAddApartment = async (event) => {
    event.preventDefault();
    setIsPending(true);
    try {
      const response = await axios.post(
        APARTMENTS_ENDPOINT,
        formData,
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + auth?.accessToken,
          },
          withCredentials: true,
          params: formData,
        }
      );
      if (response.data.statusCode === 200) {
        setSuccess(true);
      } else {
        throw new Exception(response.data.message);
      }
    } catch (e) {
      setError(e.message);
    }

    await new Promise((r) => setTimeout(r, 500));
    setIsPending(false);
  };

  const handleFormDataChange = (event) => {
    setFormData((prev) => ({
      ...prev,
      [event.target.name]: event.target.value,
    }));
  };

  return (
    <>
      {!isPending && success && (
        <div className="absolute bg-slate-700 p-10 rounded-lg shadow-lg shadow-black flex flex-col justify-center items-center">
          <div className="w-full flex justify-end">
            <button
              onClick={() => onCloseModal()}
              className="p-2 text-2xl text-white rounded-lg hover:shadow-lg hover:shadow-black hover:bg-slate-400 hover:text-black active:scale-95 transition"
            >
              <FontAwesomeIcon icon={faClose} />
            </button>
          </div>
          <FontAwesomeIcon
            icon={faCircleCheck}
            className="text-6xl text-green-600"
          />
          <p className="font-semibold mt-6">New apartment added successfully</p>
        </div>
      )}
      {!isPending && error && (
        <div className="absolute bg-slate-700 p-10 rounded-lg shadow-lg shadow-black flex flex-col justify-center items-center">
          <div className="w-full flex justify-end">
            <button
              onClick={() => onCloseModal()}
              className="p-2 text-2xl text-white rounded-lg hover:shadow-lg hover:shadow-black hover:bg-slate-400 hover:text-black active:scale-95 transition"
            >
              <FontAwesomeIcon icon={faClose} />
            </button>
          </div>
          <FontAwesomeIcon
            icon={faCircleXmark}
            className="text-6xl text-red-600"
          />
          <p className="font-semibold mt-6">
            An error occurred, could not add new apartment
          </p>
          {error && <p className="font-semibold mt-6">{error}</p>}
        </div>
      )}
      {isPending ? (
        <div className="absolute bg-slate-700 p-10 rounded-lg shadow-lg shadow-black flex flex-col justify-center items-center gap-6">
          <Loading />
          <p className="font-semibold">Adding new customer</p>
        </div>
      ) : (
        !success &&
        !error && (
          <form
            onSubmit={handleAddApartment}
            className="absolute bg-slate-700 p-10 rounded-lg shadow-lg shadow-black flex flex-col justify-center items-center modal z-10"
          >
            <div className="w-full flex justify-end">
              <button
                onClick={() => onCloseModal()}
                className="p-2 text-2xl text-white rounded-lg hover:shadow-lg hover:shadow-black hover:bg-slate-400 hover:text-black active:scale-95 transition"
              >
                <FontAwesomeIcon icon={faClose} />
              </button>
            </div>
            <h2 className="text-3xl font-bold text-white mb-12">
              Add new apartment
            </h2>
            <div className="flex flex-col gap-4">
              <div className="flex justify-between gap-10">
                <label htmlFor="firstName" className="text-white font-semibold">
                  Address
                </label>
                <textarea
                  name="address"
                  id="address"
                  onChange={handleFormDataChange}
                  rows={3}
                  className="p-1 rounded-md indent-1"
                />
              </div>
              <div className="flex justify-between gap-10">
                <label htmlFor="lastName" className="text-white font-semibold">
                  Rental price
                </label>
                <input
                  type="number"
                  name="rentalPrice"
                  id="rentalPrice"
                  onChange={handleFormDataChange}
                  min={0}
                  className="p-1 rounded-md indent-1"
                />
              </div>
              <div className="flex justify-between gap-10">
                <label htmlFor="address" className="text-white font-semibold">
                  Number of rooms
                </label>
                <input
                  type="number"
                  name="numberOfRooms"
                  id="numberOfRooms"
                  onChange={handleFormDataChange}
                  min={0}
                  className="p-1 rounded-md indent-1"
                />
              </div>
              <div className="flex justify-center">
                <button className="w-fit rounded-md py-2 px-6 shadow-md shadow-gray-600 gap-3 bg-slate-500 font-semibold  hover:bg-slate-600 active:scale-95 transition text-white">
                  Add
                </button>
              </div>
            </div>
          </form>
        )
      )}
    </>
  );
}

export default AddNewApartment;
