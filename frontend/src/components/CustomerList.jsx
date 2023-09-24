import { v4 } from "uuid";

function CustomerList({ customers, currentPage }) {
  return (
    <section className="h-full grid grid-rows-6">
      <header className="row-span-1 grid sm:grid-cols-12 grid-cols-10 text-center gap-10">
        <div className="sm:text-xl font-semibold col-span-2 text-sm sm:block hidden">No.</div>
        <div className="sm:text-xl font-semibold col-span-2 text-sm">First name</div>
        <div className="sm:text-xl font-semibold col-span-2 text-sm">Last name</div>
        <div className="sm:text-xl font-semibold col-span-2 text-sm">Address</div>
        <div className="sm:text-xl font-semibold col-span-2 text-sm">Age</div>
        <div className="sm:text-xl font-semibold col-span-2 text-sm">Status</div>
      </header>
      {customers &&
        customers.length > 0 &&
        customers.map((customer, idx) => (
          <div
            key={v4()}
            className="sm:text-base text-xs row-span-1 grid sm:grid-cols-6 grid-cols-5 text-center gap-10 rounded-2xl hover:bg-slate-600 hover:text-gray-100 transition"
          >
            <div className="sm:flex items-center justify-center hidden">
              {(currentPage - 1) * 5 + idx + 1}
            </div>
            <div className="flex items-center justify-center">
              {customer?.firstName}
            </div>
            <div className="flex items-center justify-center">
              {customer?.lastName}
            </div>
            <div className="flex items-center justify-center">
              {customer?.address}
            </div>
            <div className="flex items-center justify-center">
              {customer?.age}
            </div>
            <div className="flex items-center justify-center">
              {customer?.status}
            </div>
          </div>
        ))}
    </section>
  );
}

export default CustomerList;
