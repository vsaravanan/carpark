package conti.ies.comp;

import java.io.Serializable;
import java.util.List;




public interface IGenericDao<T, PK extends Serializable> {

	public List<T> list();

	public <kls> List<? extends kls> list(String sql, Class<?> kls);

	public List<T> list(int pageSize, int skip);

	public List<T> adhocQry(String Sql);

	public KendoRead qryPaging(Object kendoFilter);

	//public KendoRead qryPaging(Object kendoFilter, Class<?> kls);



	public T get(PK id); // returns null if there is no matching row. no exception is thrown
							// returns persistent or null

	public void saveOrUpdate(T t); 	// insert or update
	// transient to persistent
	// detached to persistent

	public void saveOrUpdate(T... t);

	public void saveOrUpdate(List<T> t);

	public void delete(PK pk); // persistent to detached

	public void delete(T t);

	public void delete(T... t);

	public void delete(List<T> t);

	public T findByUserName(String userName);


}

/*	open this only when required, do not delete these commented lines
//	public void persist(T t); 	// May not insert immediately. will insert on flush for non-transactions
//								// transient to persistent
//
//	public void persist(T... t);
//
//	public void save(T t); 	// it will insert immediately.But first it generates identifier
//							// transient to persistent
//
//	public void save(T... t);
//
//	public void merge(T t); // it will update. but it does not required to be loaded already
//							// transient to persistent
//							// detached to persistent
//
//	public void merge(T... t);

//	public void flush(); 	// persistent to detached after issuing UPDATE
//	// call before commit and closing the session
//	// synchronizing memory to persistent store
//
//public void clear();
//
//public void refresh(T t);
*/

	// public void load(); // to persist row from database, error thrown if row does not exists
	// public void update();  // detached to persistent



/*
 *
 * http://stackoverflow.com/questions/5311928/hibernate-envers-merge-saveorupdate
 *
 * http://www.stevideter.com/2008/12/07/saveorupdate-versus-merge-in-hibernate/
 *   with good examples
 *
 * saveOrUpdate() does the following:

if the object is already persistent in this session, do nothing
if another object associated with the session has the same identifier, throw an exception
if the object has no identifier property, save() it
if the object's identifier has the value assigned to a newly instantiated object, save() it
if the object is versioned by a or , and the version property value is the same value assigned to a newly instantiated object, save() it
otherwise update() the object
and merge() is very different:

if there is a persistent instance with the same identifier currently associated with the session, copy the state of the given object onto the persistent instance
if there is no persistent instance currently associated with the session, try to load it from the database, or create a new persistent instance
the persistent instance is returned
the given instance does not become associated with the session, it remains detached
It means that you can use saveOrUpdate() if you are sure that the object with the same identifier is not associated with the session. Otherwise you should use merge().
 *
 *
 */
