package it.pattystrolly.wedding.service.datastore;

public interface GenericConsumer<T> {
    
	void accept(T t);
    
}
