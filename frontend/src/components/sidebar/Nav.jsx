import {
  faBuilding,
  faFileContract,
  faUpload,
  faUser,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Link } from "react-router-dom";

function Nav() {
  return (
    <nav>
      <ul className="flex flex-col justify-center items-start gap-4 p-8">
        <li className="p-2 w-full rounded-lg hover:bg-slate-800 hover:text-gray-100 transition">
          <Link
            to={"/customers"}
            className="flex justify-start items-center gap-3 font-semibold text-xl"
          >
            <FontAwesomeIcon icon={faUser} />
            <span>Customers</span>
          </Link>
        </li>
        <li className="p-2 w-full rounded-lg hover:bg-slate-800 hover:text-gray-100 transition">
          <Link
            to={"/apartments"}
            className="flex justify-start items-center gap-3 font-semibold text-xl"
          >
            <FontAwesomeIcon icon={faBuilding} />
            <span>Apartments</span>
          </Link>
        </li>
        <li className="p-2 w-full rounded-lg hover:bg-slate-800 hover:text-gray-100 transition">
          <Link
            to={"/contracts"}
            className="flex justify-start items-center gap-3 font-semibold text-xl"
          >
            <FontAwesomeIcon icon={faFileContract} />
            <span>Contracts</span>
          </Link>
        </li>
        <li className="p-2 w-full rounded-lg hover:bg-slate-800 hover:text-gray-100 transition">
          <Link
            to={"/upload-files"}
            className="flex justify-start items-center gap-3 font-semibold text-xl"
          >
            <FontAwesomeIcon icon={faUpload} />
            <span>Upload files</span>
          </Link>
        </li>
      </ul>
    </nav>
  );
}

export default Nav;
