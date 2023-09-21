import {
  faBuilding,
  faFileContract,
  faSignIn,
  faSignOut,
  faUpload,
  faUser,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Link } from "react-router-dom";
import useAuth from "../../hooks/useAuth";

function Nav() {
  const { auth } = useAuth();

  return (
    <nav>
      <ul className="flex flex-col justify-center items-center lg:items-start gap-4 sm:p-8">
        {auth?.accessToken && (
          <>
            <li className="p-2 w-full rounded-lg hover:bg-slate-800 hover:text-gray-100 max-w-fit lg:max-w-none transition">
              <Link
                to={"/customers"}
                className="flex justify-start items-center gap-3 font-semibold text-xl"
              >
                <FontAwesomeIcon icon={faUser} />
                <span className="hidden lg:block">Customers</span>
              </Link>
            </li>
            <li className="p-2 w-full rounded-lg hover:bg-slate-800 hover:text-gray-100 max-w-fit lg:max-w-none transition">
              <Link
                to={"/apartments"}
                className="flex justify-start items-center gap-3 font-semibold text-xl"
              >
                <FontAwesomeIcon icon={faBuilding} />
                <span className="hidden lg:block">Apartments</span>
              </Link>
            </li>
            <li className="p-2 w-full rounded-lg hover:bg-slate-800 hover:text-gray-100 max-w-fit lg:max-w-none transition">
              <Link
                to={"/contracts"}
                className="flex justify-start items-center gap-3 font-semibold text-xl"
              >
                <FontAwesomeIcon icon={faFileContract} />
                <span className="hidden lg:block">Contracts</span>
              </Link>
            </li>
            <li className="p-2 w-full rounded-lg hover:bg-slate-800 hover:text-gray-100 max-w-fit lg:max-w-none transition">
              <Link
                to={"/upload-files"}
                className="flex justify-start items-center gap-3 font-semibold text-xl"
              >
                <FontAwesomeIcon icon={faUpload} />
                <span className="hidden lg:block">Upload files</span>
              </Link>
            </li>
          </>
        )}
        {!auth?.accessToken ? (
          <li className="p-2 w-full rounded-lg hover:bg-slate-800 hover:text-gray-100 max-w-fit lg:max-w-none transition">
            <Link
              to={"/upload-files"}
              className="flex justify-start items-center gap-3 font-semibold text-xl"
            >
              <FontAwesomeIcon icon={faSignIn} />
              <span className="hidden lg:block">Sign in</span>
            </Link>
          </li>
        ) : (
          <li className="p-2 w-full rounded-lg hover:bg-slate-800 hover:text-gray-100 max-w-fit lg:max-w-none transition">
            <Link
              to={"/sign-out"}
              className="flex justify-start items-center gap-3 font-semibold text-xl"
            >
              <FontAwesomeIcon icon={faSignOut} />
              <span className="hidden lg:block">Sign out</span>
            </Link>
          </li>
        )}
      </ul>
    </nav>
  );
}

export default Nav;
