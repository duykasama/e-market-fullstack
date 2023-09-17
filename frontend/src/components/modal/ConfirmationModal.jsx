import { faClose } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useState } from "react";

function ConfirmationModal({
  setShowConfirmModal,
  setShowModal,
  setShowFileUploader,
}) {
  const [upLoadFile, setUpLoadFile] = useState(false);
  const [addMannualy, setAddMannually] = useState(false);

  const handleUploadFile = () => {
    setShowConfirmModal(false);
    setShowFileUploader(true);
  };

  const handleAddMannually = () => {
    setShowConfirmModal(false);
    setShowModal(true);
  };

  return (
    <div className="absolute bg-slate-700 p-10 rounded-lg shadow-lg shadow-black flex flex-col justify-center items-center gap-10 modal">
      <div className="w-full flex justify-end">
        <button
          onClick={() => setShowConfirmModal(false)}
          className="-mt-6 -mr-6 p-2 text-2xl text-white rounded-lg hover:shadow-lg hover:shadow-black hover:bg-slate-400 hover:text-black active:scale-95 transition"
        >
          <FontAwesomeIcon icon={faClose} />
        </button>
      </div>
      <div className="flex justify-center items-center gap-4">
        <button
          onClick={handleUploadFile}
          className="py-2 px-4 rounded-md text-black font-semibold bg-slate-400 hover:bg-slate-600 hover:text-white active:scale-95 transition"
        >
          Upload file
        </button>
        <button
          onClick={handleAddMannually}
          className="py-2 px-4 rounded-md text-black font-semibold bg-slate-400 hover:bg-slate-600 hover:text-white active:scale-95 transition"
        >
          Add manually
        </button>
      </div>
    </div>
  );
}

export default ConfirmationModal;
