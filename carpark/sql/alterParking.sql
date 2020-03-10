ALTER TABLE parking ADD slotusedid int NOT NULL;
ALTER TABLE parking ADD CONSTRAINT parking_slotusedid_fkey FOREIGN KEY (slotusedid) REFERENCES slotused(slotusedid);

ALTER TABLE parking ADD finalBillId int NULL;
ALTER TABLE parking ADD CONSTRAINT parking_finalbillid_fkey FOREIGN KEY (finalBillId) REFERENCES parkingBill(billId);
