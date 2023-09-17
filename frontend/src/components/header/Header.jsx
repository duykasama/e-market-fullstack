import logo from "/1.png";

function Header() {
  const handleSearch = (event) => {
    event.preventDefault();
    console.log("form submitted");
  };
  return (
    <header className="h-full w-full flex items-center justify-between p-4">
      <img src={logo} alt="Logo" width={100}/>
      <form onSubmit={handleSearch} className="flex gap-2">
        <input
          type="text"
          placeholder="Search..."
          className="p-1 rounded-lg indent-1 border-none"
        />
        <button className="py-1 px-2 bg-blue-700 rounded-md font-semibold text-white hover:bg-blue-950 active:scale-90 transition">Search</button>
      </form>
    </header>
  );
}

export default Header;
