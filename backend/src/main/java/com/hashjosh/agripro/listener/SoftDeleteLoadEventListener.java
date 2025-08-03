package com.hashjosh.agripro.listener;


import com.hashjosh.agripro.user.models.SoftDeletable;
import org.hibernate.HibernateException;
import org.hibernate.event.spi.LoadEvent;
import org.hibernate.event.spi.LoadEventListener;

public class SoftDeleteLoadEventListener implements LoadEventListener {

    @Override
    public void onLoad(LoadEvent loadEvent, LoadType loadType) throws HibernateException {
        Object entity = loadEvent.getResult();

        if(entity instanceof  SoftDeletable softDeletable){

        }
    }
}
