import { v4 } from "uuid";

function CustomerList({ customers, currentPage }) {
  return (
    <section className="h-full grid grid-rows-6">
      <header className="row-span-1 grid grid-cols-6 text-center gap-10">
        <div className="text-xl font-semibold">No.</div>
        <div className="text-xl font-semibold">First name</div>
        <div className="text-xl font-semibold">Last name</div>
        <div className="text-xl font-semibold">Address</div>
        <div className="text-xl font-semibold">Age</div>
        <div className="text-xl font-semibold">Status</div>
      </header>
      {customers &&
        customers.length > 0 &&
        customers.map((customer, idx) => (
          <div
            key={v4()}
            className="row-span-1 grid grid-cols-6 text-center gap-10 rounded-2xl hover:bg-slate-600 hover:text-gray-100 transition"
          >
            <div className="flex items-center justify-center">
              {(currentPage - 1) * 5 + idx + 1}
            </div>
            <div className="flex items-center justify-center">
              {customer.firstName}
            </div>
            <div className="flex items-center justify-center">
              {customer.lastName}
            </div>
            <div className="flex items-center justify-center">
              {customer.address}
            </div>
            <div className="flex items-center justify-center">
              {customer.age}
            </div>
            <div className="flex items-center justify-center">
              {customer.status}
            </div>
          </div>
        ))}
    </section>
  );
}

export default CustomerList;