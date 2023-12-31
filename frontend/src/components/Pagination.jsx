import {
  faChevronLeft,
  faChevronRight,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Link } from "react-router-dom";

function Pagination({
  isFirstPage,
  isLastPage,
  onPaginationNextPage,
  onPaginationPrevPage,
  currentPage,
  totalPages,
}) {
  return (
    <nav>
      <ul className="flex justify-center items-center gap-4">
        <li
          onClick={onPaginationPrevPage}
          className={`p-1 border-slate-700 rounded-md w-12 h-8 text-center cursor-pointer bg-slate-300 hover:scale-110 transition shadow-sm hover:shadow-black${
            isFirstPage && " cursor-no-drop pointer-events-none opacity-50"
          }`}
        >
          <Link>
            <FontAwesomeIcon icon={faChevronLeft} />
          </Link>
        </li>
        <li className="p-1 border-slate-700 rounded-md w-12 h-8 text-center cursor-pointer bg-slate-300 transition font-semibold">
          <Link>{currentPage}</Link>
        </li>
        {currentPage < totalPages && totalPages - currentPage >= 1 && (
          <li className="p-1 border-slate-700 rounded-md w-12 h-8 text-center cursor-pointer bg-slate-300 hover:scale-110 transition font-semibold shadow-sm hover:shadow-black">
            <Link>{currentPage + 1}</Link>
          </li>
        )}
        {currentPage < totalPages && totalPages - currentPage >= 2 && (
          <li className="p-1 border-slate-700 rounded-md w-12 h-8 text-center cursor-pointer bg-slate-300 hover:scale-110 transition font-semibold shadow-sm hover:shadow-black">
            <Link>{currentPage + 2}</Link>
          </li>
        )}
        <li
          onClick={onPaginationNextPage}
          className={`p-1 border-slate-700 rounded-md w-12 h-8 text-center cursor-pointer bg-slate-300 hover:scale-110 transition shadow-sm hover:shadow-black${
            isLastPage && " cursor-no-drop pointer-events-none opacity-50"
          }`}
        >
          <Link>
            <FontAwesomeIcon icon={faChevronRight} />
          </Link>
        </li>
      </ul>
    </nav>
  );
}

export default Pagination;
