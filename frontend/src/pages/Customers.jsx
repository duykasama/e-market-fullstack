import GridView from "../components/GridView";
import AddNewCustomer from "../components/modal/AddNewCustomer";
import useFetch from "../hooks/useFetch";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faFaceSadTear } from "@fortawesome/free-solid-svg-icons";
import { useState } from "react";
import { CUSTOMERS_ENPOINT } from "../data/apiInfo";

function Customers() {
  const [offset, setOffset] = useState(1);
  const params = new URLSearchParams({
    pageSize: 5,
    offset: offset,
  }).toString();
  const { data, isPending, error } = useFetch(
    `${CUSTOMERS_ENPOINT}/pagination?${params}`
  );

  const handleNextPage = () => {
    setOffset((prev) => prev + 1);
  };

  const handlePrevPage = () => {
    setOffset((prev) => prev - 1);
  };

  return (
    <div className="w-full h-full p-8 flex justify-center items-center">
      {data && (
        <GridView
          data={data.content}
          title={"Customers"}
          Modal={AddNewCustomer}
          isPending={isPending}
          isFirstPage={data.first}
          isLastPage={data.last}
          onNextPage={handleNextPage}
          onPrevPage={handlePrevPage}
          currentPage={offset}
        />
      )}
      {error && !isPending && (
        <div className="flex flex-col justify-center items-center gap-4 text-2xl">
          <FontAwesomeIcon icon={faFaceSadTear} className="text-red-600" />
          <p>An error occurred</p>
          <p>{error}</p>
        </div>
      )}
    </div>
  );
}

export default Customers;
