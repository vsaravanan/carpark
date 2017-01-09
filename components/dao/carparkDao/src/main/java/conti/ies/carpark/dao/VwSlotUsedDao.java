package conti.ies.carpark.dao;

import org.springframework.stereotype.Repository;

import conti.ies.carpark.model.VwSlotUsed;
import conti.ies.comp.GenericDao;

@Repository
public class VwSlotUsedDao extends GenericDao<VwSlotUsed, Integer> implements IVwSlotUsedDao{

}
