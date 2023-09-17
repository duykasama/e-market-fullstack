import { Route, Routes } from "react-router-dom";
import Header from "../components/header/Header";
import SideBar from "../components/sidebar/SideBar";
import Index from "../pages/Index";
import Customers from "../pages/Customers";
import Contracts from "../pages/Contracts";
import Apartments from "../pages/Apartments";
import customerList from "../data/customers.json";
import contractList from "../data/contracts.json";
import apartmentList from "../data/apartments.json";
import UploadFiles from "../pages/UploadFiles";
import NotFound from "../pages/NotFound";

function DefaultLayout() {
  return (
    <div className="grid grid-cols-6 grid-rows-6 min-h-screen">
      <div className="bg-slate-600 col-span-1 row-span-full">
        <SideBar />
      </div>
      <div className="bg-slate-200 row-span-1 col-end-7 row-start-1 col-start-2">
        <Header />
      </div>
      <main className="row-start-2 col-start-2 col-end-7 row-end-7">
        <Routes>
          <Route path="/" element={<Index />} />
          <Route
            path="/customers"
            element={<Customers customers={customerList} />}
          />
          <Route
            path="/contracts"
            element={<Contracts contracts={contractList} />}
          />
          <Route
            path="/apartments"
            element={<Apartments apartments={apartmentList} />}
          />
          <Route path="/upload-files" element={<UploadFiles />} />
          <Route path="*" element={<NotFound />} />
        </Routes>
      </main>
    </div>
  );
}

export default DefaultLayout;
