import { v4 } from "uuid";

function ContractList({ contracts, currentPage }) {
  return (
    <section className="h-full grid grid-rows-6">
      <header className="row-span-1 grid grid-cols-5 text-center gap-10">
        <div className="text-xl font-semibold">No.</div>
        <div className="text-xl font-semibold">Customer</div>
        <div className="text-xl font-semibold">Apartment</div>
        <div className="text-xl font-semibold">Start date</div>
        <div className="text-xl font-semibold">End date</div>
      </header>
      {contracts &&
        contracts.length > 0 &&
        contracts.map((contract, idx) => (
          <div
            key={v4()}
            className="row-span-1 grid grid-cols-5 text-center gap-10"
          >
            <div>{(currentPage - 1) * 5 + idx + 1}</div>
            <div>{contract.customer.firstName}</div>
            <div>{contract.apartment.address}</div>
            <div>{contract.startDate}</div>
            <div>{contract.endDate}</div>
          </div>
        ))}
    </section>
  );
}

export default ContractList;
