package pharmacie.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pharmacie.entity.Commande;

public interface CommandeRepository extends JpaRepository<Commande, Integer> {
    List<Commande> findBySaisieLeAfter(Date date);

    // Nombre total d'articles déjà envoyés pour un dispensaire
    @Query("SELECT SUM(l.quantite) FROM Ligne l " +
           "WHERE l.commande.dispensaire.code = :dispCode " +
           "AND l.commande.envoyeeLe IS NOT NULL")
    Long countArticlesEnvoyesParDispensaire(String dispCode);

    // Commandes en cours (date d'envoi non renseignée)
    List<Commande> findByDispensaireCodeAndEnvoyeeLeIsNull(String dispCode);
}