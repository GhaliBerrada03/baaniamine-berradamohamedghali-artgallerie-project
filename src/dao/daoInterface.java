package dao;
import java.util.List;
public interface daoInterface<T> {
     T findById(int id) throws Exception; // find a client by its id that's why it ruturned an object T.
     List<T> findAll() throws Exception; // list the list of clients we have.
     int  insert(T obj) throws Exception; // insert a client.
     boolean update(T obj) throws Exception;
     boolean delete(int id) throws Exception;

}