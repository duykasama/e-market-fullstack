import {
  faCircleCheck,
  faCircleXmark,
  faClose,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useState } from "react";
import { v4 } from "uuid";
import Loading from "../ui/Loading";
import useFetch from "../../hooks/useFetch";
import useAuth from "../../hooks/useAuth";
import {
  APARTMENTS_ENDPOINT,
  CONTRACTS_ENDPOINT,
  CUSTOMERS_ENDPOINT,
} from "../../data/apiInfo";
import useAxiosPrivate from "../../hooks/useAxiosPrivate";

function AddNewContract({ onCloseModal }) {
  const { data: customers } = useFetch(CUSTOMERS_ENDPOINT);
  const { data: apartments } = useFetch(APARTMENTS_ENDPOINT);

  let formData = {
    customerId: "",
    apartmentId: "",
    startDate: "",
    endDate: "",
  };

  const [isPending, setIsPending] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState(null);
  const { auth } = useAuth();
  const axiosPrivate = useAxiosPrivate();

  const handleAddContract = async (event) => {
    event.preventDefault();
    setIsPending(true);
    try {
      const response = await axiosPrivate.post(CONTRACTS_ENDPOINT, formData, {
        headers: {
          Accept: "*/*",
          "Content-Type": "application/json",
          Authorization: "Bearer " + auth?.accessToken,
        },
        withCredentials: true,
        params: formData,
      });

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
    formData = { ...formData, [event.target.name]: event.target.value };
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
          <p className="font-semibold mt-6">New contract added successfully</p>
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
            An error occurred, could not add new contract
          </p>
          {error && <p className="font-semibold mt-6">{error}</p>}
        </div>
      )}
      {isPending ? (
        <div className="absolute bg-slate-700 p-10 rounded-lg shadow-lg shadow-black flex flex-col justify-center items-center gap-6">
          <Loading />
          <p className="font-semibold">Adding new contract</p>
        </div>
      ) : (
        !success &&
        !error && (
          <form
            onSubmit={handleAddContract}
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
              Add new contract
            </h2>
            <div className="flex flex-col gap-4">
              <div className="flex justify-between gap-10">
                <label
                  htmlFor="customerId"
                  className="text-white font-semibold"
                >
                  Customer
                </label>
                <select
                  name="customerId"
                  onChange={handleFormDataChange}
                  className="p-1 rounded-md w-40"
                >
                  <option hidden>Choose customer</option>
                  {customers &&
                    customers.map((customer) => (
                      <option key={v4()} value={customer.id}>
                        {customer.firstName} {customer.lastName}
                      </option>
                    ))}
                </select>
              </div>
              <div className="flex justify-between gap-10">
                <label
                  htmlFor="apartmentId"
                  className="text-white font-semibold"
                >
                  Apartment
                </label>
                <select
                  name="apartmentId"
                  id="apartmentId"
                  onChange={handleFormDataChange}
                  className="p-1 rounded-md w-40"
                >
                  <option hidden>Choose apartment</option>
                  {apartments &&
                    apartments.map((apartment) => (
                      <option key={v4()} value={apartment.id}>
                        {apartment.address}
                      </option>
                    ))}
                </select>
              </div>
              <div className="flex justify-between gap-10">
                <label htmlFor="startDate" className="text-white font-semibold">
                  Start date
                </label>
                <input
                  type="date"
                  name="startDate"
                  onChange={handleFormDataChange}
                  id="startDate"
                  className="p-1 rounded-md indent-1"
                />
              </div>
              <div className="flex justify-between gap-10">
                <label htmlFor="endDate" className="text-white font-semibold">
                  End date
                </label>
                <input
                  type="date"
                  name="endDate"
                  onChange={handleFormDataChange}
                  id="endDate"
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

export default AddNewContract;
