package conti.ies.carpark.dao;

import org.springframework.stereotype.Repository;

import conti.ies.carpark.model.ParkingSlot;
import conti.ies.comp.GenericDao;

@Repository
public class ParkingSlotDao extends GenericDao<ParkingSlot, Integer> implements IParkingSlotDao {

}
