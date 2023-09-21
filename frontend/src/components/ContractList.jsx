import { v4 } from "uuid";

function ContractList({ contracts, currentPage }) {
  return (
    <section className="h-full grid grid-rows-6">
      <header className="row-span-1 grid sm:grid-cols-10 grid-cols-8 text-center gap-10">
        <div className="sm:text-xl text-sm col-span-2 font-semibold sm:block hidden">No.</div>
        <div className="sm:text-xl text-sm col-span-2 font-semibold">Customer</div>
        <div className="sm:text-xl text-sm col-span-2 font-semibold">Apartment</div>
        <div className="sm:text-xl text-sm col-span-2 font-semibold">Start date</div>
        <div className="sm:text-xl text-sm col-span-2 font-semibold">End date</div>
      </header>
      {contracts &&
        contracts.length > 0 &&
        contracts.map((contract, idx) => (
          <div
            key={v4()}
            className="row-span-1 grid sm:grid-cols-5 grid-cols-4 text-center gap-10 rounded-2xl hover:bg-slate-600 hover:text-gray-100 transition"
          >
            <div className="sm:flex hidden items-center justify-center">
              {(currentPage - 1) * 5 + idx + 1}
            </div>
            <div className="flex items-center justify-center">
              {contract?.customer?.firstName}
            </div>
            <div className="flex items-center justify-center">
              {contract?.apartment?.address}
            </div>
            <div className="flex items-center justify-center">
              {contract?.startDate}
            </div>
            <div className="flex items-center justify-center">
              {contract?.endDate}
            </div>
          </div>
        ))}
    </section>
  );
}

export default ContractList;
