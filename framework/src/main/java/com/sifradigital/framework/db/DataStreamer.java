package com.sifradigital.framework.db;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class DataStreamer<T> extends Spliterators.AbstractSpliterator<T> {

    private int pageSize = 500;
    private List<T> records;
    private int currentPage;
    private int currentIndex;
    private final PageLoader<T> pageLoader;

    public DataStreamer(PageLoader<T> pageLoader) {
        super(Integer.MAX_VALUE, Spliterator.IMMUTABLE);
        this.pageLoader = pageLoader;
        records = pageLoader.loadPage(0, pageSize);
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        T record = nextRecord();
        if (record != null) {
            action.accept(record);
            return true;
        }
        else {
            return false;
        }
    }

    private T nextRecord() {
        T record = null;
        if (currentIndex < records.size()) {
            record = records.get(currentIndex);
            currentIndex++;
        }
        else {
            currentPage++;
            currentIndex = 0;
            records = pageLoader.loadPage(currentPage * pageSize, pageSize);
            if (!records.isEmpty()) {
                record = records.get(0);
                currentIndex++;
            }
        }
        return record;
    }

    public interface PageLoader<T> {

        List<T> loadPage(int offset, int size);
    }
}
