package com.example.emarket.util;

import com.example.emarket.enums.FileType;
import com.example.emarket.exceptions.FileFormatNotSupported;

public class FileTypeDistributor {

    private static final String CUSTOMER_HEADERS = "FirstName,LastName,Address,Age,Status";
    private static final String APARTMENT_HEADERS = "Address,RentalPrice,NumberOfRooms";
    private static final String CONTRACT_HEADERS = "CustomerId,ApartmentId,StartDate,EndDate";


    public static FileType getFileTypeFromHeaders(String headersStr) throws FileFormatNotSupported {

        if (headersStr.equalsIgnoreCase(CUSTOMER_HEADERS)) {
            return FileType.CUSTOMER;
        } else if (headersStr.equalsIgnoreCase(APARTMENT_HEADERS)) {
            return FileType.APARTMENT;
        } else if (headersStr.equalsIgnoreCase(CONTRACT_HEADERS)) {
            return FileType.CONTRACT;
        } else {
            throw new FileFormatNotSupported();
        }
    }
}
