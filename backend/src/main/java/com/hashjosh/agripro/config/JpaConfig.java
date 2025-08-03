package com.hashjosh.agripro.config;


import com.hashjosh.agripro.listener.SoftDeleteLoadEventListener;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.EventListenerFactory;

import java.util.Optional;

@Configuration
public class JpaConfig {

    private final EntityManagerFactory entityManagerFactory;

    public JpaConfig(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void registerListener(){
       try{
         SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);

         if(!(sessionFactory instanceof  SessionFactoryImpl sessionFactoryImpl)){
             System.out.println("SessionFactory is not of expected type, skipping listener registration.");
             return;
         }
           Optional.ofNullable(
                   sessionFactoryImpl.getServiceRegistry().getService(EventListenerRegistry.class)
           ).ifPresent(registry -> {
               registry.appendListeners(EventType.LOAD, new SoftDeleteLoadEventListener());
           });
       }catch (Exception e){
           System.out.println("Failed to register SoftDeleteLoadEventListener: "+e.getMessage());
       }
    }
}
