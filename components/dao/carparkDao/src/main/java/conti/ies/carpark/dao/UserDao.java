package conti.ies.carpark.dao;



import org.springframework.stereotype.Repository;

import conti.ies.carpark.model.User;
import conti.ies.comp.GenericDao;


@Repository
public class UserDao extends GenericDao<User, Integer> implements IUserDao {

//
//	@Autowired
//	public UserDao(SessionFactory sessionFactory) {
//		super(sessionFactory);
//	}

}
//	public UserDAOImpl(Class<User> type) {
//		super(type);
//	}


//	@Autowired
//	private SessionFactory sessionFactory;
//
//	public UserDAOImpl() {
//
//	}
//
//	public UserDAOImpl(SessionFactory sessionFactory) {
//		this.sessionFactory = sessionFactory;
//	}
//
//	@Override
//	@Transactional
//	public List<User> list() {
//		@SuppressWarnings("unchecked")
//		List<User> listUser = (List<User>) sessionFactory.getCurrentSession()
//				.createCriteria(User.class)
//				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
//
//		return listUser;
//	}
//
//	@Override
//	@Transactional
//	public void saveOrUpdate(User user) {
//		sessionFactory.getCurrentSession().saveOrUpdate(user);
//	}
//
//	@Override
//	@Transactional
//	public void delete(int id) {
//		User userToDelete = new User();
//		userToDelete.setId(id);
//		sessionFactory.getCurrentSession().delete(userToDelete);
//	}
//
//	@Override
//	@Transactional
//	public User get(int id) {
//		String hql = "from User where id=" + id;
//		Query query = sessionFactory.getCurrentSession().createQuery(hql);
//
//		@SuppressWarnings("unchecked")
//		List<User> listUser = (List<User>) query.list();
//
//		if (listUser != null && !listUser.isEmpty()) {
//			return listUser.get(0);
//		}
//
//		return null;
//	}
