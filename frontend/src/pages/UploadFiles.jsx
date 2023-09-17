import {
  faClose,
  faFile,
  faTrash,
  faUpload,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useRef, useState, useTransition } from "react";
import { v4 } from "uuid";

function UploadFiles() {
  const inputFileRef = useRef(null);
  const [files, setFiles] = useState([]);
  const [isPending, startTransition] = useTransition();

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

  const uploadFiles = () => {
    console.log("files uploaded");
  };

  return (
    <div className="h-full w-full flex justify-center items-center p-8">
      <div className="p-4 bg-slate-400 rounded-lg w-full h-full gap-4 shadow-lg shadow-gray-600 flex flex-col justify-center items-center">
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
            <div className="grid grid-cols-3 gap-4">
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
      </div>
    </div>
  );
}

export default UploadFiles;
