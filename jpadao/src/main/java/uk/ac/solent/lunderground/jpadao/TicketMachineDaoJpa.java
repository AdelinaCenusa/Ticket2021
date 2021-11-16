package uk.ac.solent.lunderground.jpadao;

import uk.ac.solent.lunderground.model.dao.TicketMachineDao;
import uk.ac.solent.lunderground.model.dto.TicketMachine;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import java.util.List;

public class TicketMachineDaoJpa implements TicketMachineDao
{
    /**
     * Entity manager used for persistence.
     */
    private final EntityManager entityManager;

    public TicketMachineDaoJpa(final EntityManager em)
    {
        this.entityManager = em;
    }

    @Override
    public void addTicketMachine(@NotNull TicketMachine newTicketMachine)
    {
        entityManager.getTransaction()
                     .begin();
        entityManager.persist(newTicketMachine);
        entityManager.getTransaction()
                     .commit();
    }

    @Override
    public List<TicketMachine> retrieveAll()
    {
        TypedQuery<TicketMachine> q = entityManager.createQuery("SELECT t FROM TicketMachine t", TicketMachine.class);
        return q.getResultList();
    }

    @Override
    public TicketMachine getTicketMachine(String uuid)
    {
        String query  = "SELECT t FROM TicketMachine t WHERE t.uuid=:uuid";
        TypedQuery<TicketMachine> q = entityManager.createQuery(query, TicketMachine.class);
        q.setParameter("uuid", uuid);
        return q.getSingleResult();
    }

    @Override
    public void deleteAll()
    {
        entityManager.getTransaction()
                     .begin();
        entityManager.createQuery("DELETE FROM TicketMachine ")
                     .executeUpdate();
        entityManager.getTransaction()
                     .commit();
    }

    @Override
    public void updateTicketMachine(TicketMachine ticketMachine)
    {
        entityManager.merge(ticketMachine);
    }
}
