package conti.ies.carpark.dao;

import org.springframework.stereotype.Repository;

import conti.ies.carpark.model.Parking;
import conti.ies.comp.GenericDao;

@Repository
public class ParkingDao extends GenericDao<Parking, Integer> implements IParkingDao {

}
