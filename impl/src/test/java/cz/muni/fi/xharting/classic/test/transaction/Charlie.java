package cz.muni.fi.xharting.classic.test.transaction;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.transaction.SystemException;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.Transactional;

import org.jboss.seam.annotations.Name;

/**
 * Seam2 component for transactional tests which defines the transaction attributes at the type level.
 *
 * @author pandreev
 *
 */
@Name("charlie")
@Transactional
public class Charlie implements Serializable {
    private static final long serialVersionUID = -1311284241318892963L;

    @Resource(lookup = "java:comp/TransactionSynchronizationRegistry")
    private TransactionSynchronizationRegistry transactionSynchronizationRegistry;

    /**
     * TX required by default.
     * 
     * @return current transaction status
     * @throws SystemException
     */

    public int required() throws SystemException {
        return transactionSynchronizationRegistry.getTransactionStatus();
    }
}
