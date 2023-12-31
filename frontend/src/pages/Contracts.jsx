import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import GridView from "../components/GridView";
import AddNewContract from "../components/modal/AddNewContract";
import { faFaceSadTear } from "@fortawesome/free-solid-svg-icons";
import useFetch from "../hooks/useFetch";
import { useState } from "react";
import { CONTRACTS_ENDPOINT } from "../data/apiInfo";

function Contracts() {
  const [offset, setOffset] = useState(1);
  const [reload, setReload] = useState(false);
  const params = new URLSearchParams({
    pageSize: 5,
    offset: offset,
  }).toString();
  const { data, isPending, error } = useFetch(
    `${CONTRACTS_ENDPOINT}/pagination?${params}`,
    reload
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
          data={data?.content}
          title={"Contracts"}
          Modal={AddNewContract}
          isPending={isPending}
          isFirstPage={data?.first}
          isLastPage={data?.last}
          totalPages={data?.totalPages}
          onNextPage={handleNextPage}
          onPrevPage={handlePrevPage}
          currentPage={offset}
          onReload={setReload}
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

export default Contracts;
