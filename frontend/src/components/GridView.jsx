import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Pagination from "./Pagination";
import { faPlus } from "@fortawesome/free-solid-svg-icons";
import { useEffect, useState } from "react";
import ContractList from "./ContractList";
import ApartmentList from "./ApartmentList";
import CustomerList from "./CustomerList";
import Loading from "./ui/Loading";

function GridView({
  data,
  title,
  Modal,
  isPending,
  isFirstPage,
  isLastPage,
  onNextPage,
  onPrevPage,
  currentPage,
}) {
  const [showModal, setShowModal] = useState(false);

  return (
    <>
      {showModal && <Modal onCloseModal={() => setShowModal(false)} />}
      <div className="p-4 bg-slate-400 rounded-lg sm:w-full h-full grid grid-rows-6 gap-4 shadow-lg shadow-gray-600">
        <div className="flex sm:justify-between justify-end items-center row-span-1">
          <h2 className="text-4xl font-semibold sm:block hidden">{title}</h2>
          <div>
            <button
              onClick={() => setShowModal(true)}
              className="flex justify-center items-center border border-gray-500 rounded-md p-2 shadow-md shadow-gray-600 gap-3 bg-slate-500 font-semibold hover:text-white hover:bg-slate-700 active:scale-95 transition"
            >
              <FontAwesomeIcon icon={faPlus} />
              <span>Add new</span>
            </button>
          </div>
        </div>
        <div className="row-start-2 row-end-6">
          {title === "Customers" && (
            <>
              {isPending ? (
                <Loading />
              ) : (
                <CustomerList customers={data} currentPage={currentPage} />
              )}
            </>
          )}
          {title === "Apartments" && (
            <>
              {isPending ? (
                <Loading />
              ) : (
                <ApartmentList apartments={data} currentPage={currentPage} />
              )}
            </>
          )}
          {title === "Contracts" && (
            <>
              {isPending ? (
                <Loading />
              ) : (
                <ContractList contracts={data} currentPage={currentPage} />
              )}
            </>
          )}
        </div>
        <div className="row-span-1 row-end-7 flex justify-center items-center">
          <Pagination
            isFirstPage={isFirstPage}
            isLastPage={isLastPage}
            onPaginationNextPage={onNextPage}
            onPaginationPrevPage={onPrevPage}
          />
        </div>
      </div>
    </>
  );
}

export default GridView;
