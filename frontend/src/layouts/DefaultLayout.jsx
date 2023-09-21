import { Route, Routes } from "react-router-dom";
import Header from "../components/header/Header";
import SideBar from "../components/sidebar/SideBar";
import Index from "../pages/Index";
import Customers from "../pages/Customers";
import Contracts from "../pages/Contracts";
import Apartments from "../pages/Apartments";
import UploadFiles from "../pages/UploadFiles";
import NotFound from "../pages/NotFound";
import RequireAuth from "../components/RequireAuth";
import SignIn from "../pages/SignIn";
import SignUp from "../pages/SignUp";
import SignOut from "../pages/SignOut";
import Unauthorized from "../pages/Unauthorized";
import PersistLogin from "../components/PersistLogin";

function DefaultLayout() {
  return (
    <div className="grid grid-cols-12 grid-rows-6 min-h-screen">
      <div className="bg-slate-600 sm:col-span-2 col-span-1 row-span-full">
        <SideBar />
      </div>
      <div className="bg-slate-200 row-span-1 col-end-13 row-start-1 sm:col-start-3 col-start-2">
        <Header />
      </div>
      <main className="row-start-2 sm:col-start-3 col-start-2 col-end-13 row-end-7">
        <Routes>
          <Route path="/" element={<Index />} />
          <Route element={<PersistLogin />}>
            <Route element={<RequireAuth allowedRoles={["USER"]} />}>
              <Route path="/customers" element={<Customers />} />
              <Route path="/contracts" element={<Contracts />} />
              <Route path="/apartments" element={<Apartments />} />
              <Route path="/upload-files" element={<UploadFiles />} />
            </Route>
          </Route>
          <Route path="/sign-in" element={<SignIn />} />
          <Route path="/sign-up" element={<SignUp />} />
          <Route path="/sign-out" element={<SignOut />} />
          <Route path="/unauthorized" element={<Unauthorized />} />
          <Route path="*" element={<NotFound />} />
        </Routes>
      </main>
    </div>
  );
}

export default DefaultLayout;
