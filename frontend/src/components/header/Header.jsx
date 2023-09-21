import React, { useState } from 'react';
import logo from '/1.png';

function Header() {
    const [searchResults, setSearchResults] = useState([]);
    const [isSearching, setIsSearching] = useState(false);

    const handleSearch = async (event) => {
        event.preventDefault();
        const searchQuery = event.target.elements.searchQuery.value;

        setIsSearching(true);

        // Gửi yêu cầu tìm kiếm đến máy chủ hoặc xử lý tìm kiếm dữ liệu ở đây
        // Sau khi nhận được kết quả tìm kiếm, cập nhật trạng thái searchResults và kết thúc quá trình tìm kiếm bằng cách cập nhật isSearching

        // Ví dụ:
        try {
            const response = await fetch(`/api/search?keyword=${searchQuery}`);
            const data = await response.json();
            setSearchResults(data); // data chứa kết quả tìm kiếm từ máy chủ
        } catch (error) {
            console.error('Error:', error);
        }

        setIsSearching(false);
    };

    return (
        <header className="h-full w-full flex items-center justify-between p-4">
            <img src={logo} alt="Logo" width={100} />
            <form onSubmit={handleSearch} className="flex gap-2">
                <input
                    type="text"
                    name="searchQuery"
                    placeholder="Search for..."
                    className="p-1 rounded-lg indent-1 border-none"
                />
                <button className="py-1 px-2 bg-blue-700 rounded-md font-semibold text-white hover:bg-blue-950 active:scale-90 transition">
                    {isSearching ? 'Searching...' : 'Search'}
                </button>
            </form>
        </header>
    );
}

export default Header;