import { faSpinner } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

function Loading() {
  return (
    <div className="col-span-4 w-full h-full flex justify-center items-center">
      <FontAwesomeIcon icon={faSpinner} className="text-5xl animate-spin" />
    </div>
  );
}

export default Loading;
