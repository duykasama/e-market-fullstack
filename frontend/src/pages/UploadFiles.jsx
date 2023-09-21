import {
  faCircleCheck,
  faCircleXmark,
  faClose,
  faTrash,
  faUpload,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useRef, useState, useTransition } from "react";
import { v4 } from "uuid";
import axios from "../lib/api/axios";
import Loading from "../components/ui/Loading";
import useAuth from "../hooks/useAuth";
import { UPLOAD_FILES_ENDPOINT } from "../data/apiInfo";

function UploadFiles() {
  const inputFileRef = useRef(null);
  const [files, setFiles] = useState([]);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);
  const [isPending, setIsPending] = useState(null);
  const [isTransitionPending, startTransition] = useTransition();
  const { auth } = useAuth();

  const handleFilesChange = (event) => {
    const targetFiles = event.target.files;
    let temp = files;
    for (let i = 0; i < targetFiles.length; i++) {
      if (!files.some((file) => file.name === targetFiles[i].name)) {
        temp.push(targetFiles[i]);
      }
    }
    setFiles(temp);
    startTransition(() => {}, []);
  };

  const handleDelete = (selectedFile) => {
    let temp = files;
    const fileIndex = files.indexOf(selectedFile);
    temp.splice(fileIndex, 1);
    setFiles(temp);
    startTransition(() => {}, []);
  };

  const uploadFiles = async () => {
    const formData = new FormData();
    files.forEach((file) => {
      formData.append("files", file);
    });
    setIsPending(true);
    try {
      const response = await axios.post(UPLOAD_FILES_ENDPOINT, formData, {
        headers: {
          Accept: "*/*",
          "Content-Type": "multipart/form-data",
          Authorization: "Bearer " + auth?.accessToken,
        },
        withCredentials: true,
        params: formData,
      });

      if (response.data.statusCode === 200) {
        setSuccess(response.data.message);
      } else {
        setError(response.data.message);
      }
    } catch (e) {
      setError(e.message);
    }

    await new Promise((r) => setTimeout(r, 1000));
    setIsPending(false);
  };

  const handleResetPage = () => {
    setSuccess(null);
    setError(null);
    setFiles([]);
  };

  return (
    <>
      <div className="h-full w-full flex justify-center items-center p-8">
        <div className="p-4 bg-slate-400 rounded-lg w-full h-full gap-4 shadow-lg shadow-gray-600 flex flex-col justify-center items-center">
          {!isPending && success && (
            <div className="absolute bg-slate-700 p-10 rounded-lg shadow-lg shadow-black flex flex-col justify-center items-center">
              <div className="w-full flex justify-end">
                <button
                  onClick={handleResetPage}
                  className="p-2 text-2xl text-white rounded-lg hover:shadow-lg hover:shadow-black hover:bg-slate-400 hover:text-black active:scale-95 transition"
                >
                  <FontAwesomeIcon icon={faClose} />
                </button>
              </div>
              <FontAwesomeIcon
                icon={faCircleCheck}
                className="text-6xl text-green-600"
              />
              {success && <p className="font-semibold mt-6">{success}</p>}
            </div>
          )}
          {!isPending && error && (
            <div className="absolute bg-slate-700 p-10 rounded-lg shadow-lg shadow-black flex flex-col justify-center items-center">
              <div className="w-full flex justify-end">
                <button
                  onClick={handleResetPage}
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
                An error occurred, could not add new data from your source files
              </p>
              {error && <p className="font-semibold mt-6">{error}</p>}
            </div>
          )}
          {isPending ? (
            <Loading />
          ) : (
            !success &&
            !error && (
              <>
                <div
                  onClick={() => inputFileRef.current.click()}
                  className="flex flex-col justify-center items-center gap-3 p-3 rounded-md bg-slate-600 shadow-md hover:shadow-xl shadow-gray-600 text-white w-fit cursor-pointer active:scale-95 transition"
                >
                  <FontAwesomeIcon icon={faUpload} className="text-xl" />
                  <span className="font-semibold">Choose files</span>
                  <input
                    type="file"
                    ref={inputFileRef}
                    multiple
                    onChange={handleFilesChange}
                    hidden
                  />
                </div>

                {files && files.length > 0 && (
                  <section className="flex flex-col gap-2">
                    <div className="grid sm:grid-cols-3 grid-cols-2 gap-4">
                      {files.map((file) => (
                        <div
                          key={v4()}
                          className="flex justify-between items-center gap-6"
                        >
                          <span>{file.name}</span>
                          <FontAwesomeIcon
                            onClick={() => handleDelete(file)}
                            icon={faTrash}
                            className="text-xl text-red-700 cursor-pointer hover:scale-110 transition"
                          />
                        </div>
                      ))}
                    </div>
                    <div className="flex justify-center items-center">
                      <button
                        onClick={uploadFiles}
                        className="w-fit py-2 px-4 bg-slate-700 text-white font-semibold rounded-lg shadow-md shadow-black hover:shadow-gray-600 active:scale-95 transition mt-8"
                      >
                        Upload
                      </button>
                    </div>
                  </section>
                )}
              </>
            )
          )}
        </div>
      </div>
    </>
  );
}

export default UploadFiles;
