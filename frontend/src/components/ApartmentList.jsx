import { v4 } from "uuid";

function ApartmentList({ apartments, currentPage }) {
  return (
    <section className="h-full grid grid-rows-6">
      <header className="row-span-1 grid sm:grid-cols-8 grid-cols-6 text-center gap-10">
        <div className="sm:text-xl font-semibold col-span-2 text-sm sm:block hidden">No.</div>
        <div className="sm:text-xl font-semibold col-span-2 text-sm">Address</div>
        <div className="sm:text-xl font-semibold col-span-2 text-sm">Rental price</div>
        <div className="sm:text-xl font-semibold col-span-2 text-sm">Number of rooms</div>
      </header>
      {apartments &&
        apartments.length > 0 &&
        apartments.map((apartment, idx) => (
          <div
            key={v4()}
            className="row-span-1 grid sm:grid-cols-4 grid-cols-3 text-center gap-10 rounded-2xl hover:bg-slate-600 hover:text-gray-100 transition"
          >
            <div className="sm:flex hidden items-center justify-center">
              {(currentPage - 1) * 5 + idx + 1}
            </div>
            <div className="flex items-center justify-center">
              {apartment?.address}
            </div>
            <div className="flex items-center justify-center">
              {apartment?.rentalPrice}
            </div>
            <div className="flex items-center justify-center">
              {apartment?.numberOfRooms}
            </div>
          </div>
        ))}
    </section>
  );
}

export default ApartmentList;
