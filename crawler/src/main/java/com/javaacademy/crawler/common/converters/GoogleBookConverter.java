package com.javaacademy.crawler.common.converters;

import com.javaacademy.crawler.common.logger.AppLogger;
import com.javaacademy.crawler.common.model.BookModel;
import com.javaacademy.crawler.googlebooks.model.BookItem;
import com.javaacademy.crawler.googlebooks.model.Isbn;
import com.javaacademy.crawler.googlebooks.model.SaleInfo;
import com.javaacademy.crawler.googlebooks.model.VolumeInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import static com.javaacademy.crawler.common.logger.AppLogger.DEFAULT_LEVEL;

public class GoogleBookConverter {

    public BookModel convertToDto(BookItem bookItem) {
        AppLogger.logger.log(DEFAULT_LEVEL, "Converting book item: " + bookItem);
        BookModel bookModel = new BookModel();
        VolumeInfo volumeInfo = bookItem.getVolumeInfo();
        bookModel.setTitle(volumeInfo.getTitle());
        bookModel.setSubtitle(volumeInfo.getSubtitle());
        bookModel.setAuthors(volumeInfo.getAuthors());
        List<Isbn> industryIdentifiers = volumeInfo.getIndustryIdentifiers();
        if (industryIdentifiers != null) {
            bookModel.setIndustryIdentifier(getIsbn(industryIdentifiers));
        }
        bookModel.setCategories(volumeInfo.getCategories());
        bookModel.setSmallThumbnail(volumeInfo.getImageLinks().getSmallThumbnail());
        bookModel.setCanonicalVolumeLink(volumeInfo.getCanonicalVolumeLink());
        SaleInfo saleInfo = bookItem.getSaleInfo();
        bookModel.setSaleability(saleInfo.getSaleability());
        bookModel.setListPriceAmount(saleInfo.getListPrice().getAmount());
        bookModel.setListPriceCurrencyCode(saleInfo.getListPrice().getCurrencyCode());
        bookModel.setRetailPriceAmount(saleInfo.getRetailPrice().getAmount());
        bookModel.setRetailPriceCurrencyCode(saleInfo.getRetailPrice().getCurrencyCode());
        return bookModel;
    }

    private Long getIsbn(List<Isbn> list) {
        Map<String, Long> map = new HashMap<>();
        for (Isbn isbn :
                list) {
            try {
                String name = isbn.getType();
                Long value = Long.valueOf(isbn.getIdentifier());
                map.put(name, value);
            } catch (NumberFormatException e) {
                AppLogger.logger.log(Level.INFO, "Could not parse ISBN", e);
            }
        }
        return map.getOrDefault("ISBN_13", -1L);
    }
}