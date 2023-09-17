import { v4 } from "uuid";

function ApartmentList({ apartments, currentPage }) {
  return (
    <section className="h-full grid grid-rows-6">
      <header className="row-span-1 grid grid-cols-4 text-center gap-10">
        <div className="text-xl font-semibold">No.</div>
        <div className="text-xl font-semibold">Address</div>
        <div className="text-xl font-semibold">Rental price</div>
        <div className="text-xl font-semibold">Number of rooms</div>
      </header>
      {apartments &&
        apartments.length > 0 &&
        apartments.map((apartment, idx) => (
          <div
            key={v4()}
            className="row-span-1 grid grid-cols-4 text-center gap-10"
          >
            <div>{(currentPage - 1) * 5 + idx + 1}</div>
            <div>{apartment.address}</div>
            <div>{apartment.rentalPrice}</div>
            <div>{apartment.numberOfRooms}</div>
          </div>
        ))}
    </section>
  );
}

export default ApartmentList;
