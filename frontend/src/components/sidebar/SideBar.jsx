import Nav from "./Nav";

function SideBar() {
  return (
    <div className="w-full h-full grid grid-cols-1 grid-rows-6">
      <div className="row-span-1 flex justify-center items-center">
        <h1 className="text-4xl font-semibold text-black text-center lg:block hidden">Lavender Company</h1>
      </div>
      <div className="row-span-5">
        <Nav />
      </div>
    </div>
  );
}

export default SideBar;
