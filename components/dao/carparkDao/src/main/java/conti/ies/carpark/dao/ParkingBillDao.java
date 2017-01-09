package conti.ies.carpark.dao;

import org.springframework.stereotype.Repository;

import conti.ies.carpark.model.ParkingBill;
import conti.ies.comp.GenericDao;

@Repository
public class ParkingBillDao extends GenericDao<ParkingBill, Integer> implements IParkingBillDao {

}
