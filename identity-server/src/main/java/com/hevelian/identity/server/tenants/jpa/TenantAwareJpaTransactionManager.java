package com.hevelian.identity.server.tenants.jpa;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.hevelian.identity.server.tenants.resolvers.CurrentTenantResolver;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class TenantAwareJpaTransactionManager extends JpaTransactionManager {
    private static final long serialVersionUID = -6848514898591292849L;
    @NonNull
    private CurrentTenantResolver<? extends Serializable> tenantResolver;
    @NonNull
    private String multitenantProperty;

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        super.doBegin(transaction, definition);
        final EntityManagerHolder emHolder = (EntityManagerHolder) TransactionSynchronizationManager
                .getResource(getEntityManagerFactory());
        final EntityManager em = emHolder.getEntityManager();
        setProperties(em);
    }

    protected void setProperties(final EntityManager em) {
        final Serializable tenantId = getTenantResolver().getCurrentTenantId();
        if (tenantId != null) {
            em.setProperty(getMultitenantProperty(), tenantId);
        } else {
            throw new NoTenantProvidedException();
        }
    }

    class NoTenantProvidedException extends RuntimeException {
        private static final long serialVersionUID = -178473142074813252L;

        public NoTenantProvidedException() {
            super("No tenant identifier could be resolved by current "
                    + tenantResolver.getClass().getName() + " instance.");
        }

    }
}