package pharmacie.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import pharmacie.entity.AdressePostale;
import pharmacie.entity.Categorie;
import pharmacie.entity.Commande;
import pharmacie.entity.Dispensaire;
import pharmacie.entity.Medicament;

@DataJpaTest
public class RepositoryCustomMethodsTest {

    @Autowired
    private CategorieRepository categorieRepository;
    @Autowired
    private MedicamentRepository medicamentRepository;
    @Autowired
    private DispensaireRepository dispensaireRepository;
    @Autowired
    private CommandeRepository commandeRepository;

    @Test
    public void testCategorieCustomMethods() {
        // Préparation - utiliser des noms uniques car data.sql charge déjà certaines catégories
        Categorie cat1 = categorieRepository.save(new Categorie("TestAntibiotiqueUnique"));
        Categorie cat2 = categorieRepository.save(new Categorie("TestAntalgiquesUnique"));

        // Test findByLibelle
        Categorie found = categorieRepository.findByLibelle("TestAntibiotiqueUnique");
        assertNotNull(found);
        assertEquals("TestAntibiotiqueUnique", found.getLibelle());

        // Test findByLibelleContaining - chercher un pattern qui existe dans au moins une catégorie
        List<Categorie> list = categorieRepository.findByLibelleContaining("Antalgiques");
        assertTrue(list.stream().anyMatch(c -> c.getLibelle().equals("TestAntalgiquesUnique")));
    }

    @Test
    public void testMedicamentCustomMethods() {
        Categorie cat = categorieRepository.save(new Categorie("Générique"));
        
        Medicament m1 = new Medicament("Doliprane", cat);
        m1.setIndisponible((short) 0); // 0 = Disponible selon l'UML
        medicamentRepository.save(m1);

        Medicament m2 = new Medicament("Lévofloxacine", cat);
        m2.setIndisponible((short) 1); // 1 = Indisponible
        medicamentRepository.save(m2);

        // Test findByNom
        assertTrue(medicamentRepository.findByNom("Doliprane").isPresent());

        // Test findByIndisponible (recherche des disponibles : 0)
        List<Medicament> disponibles = medicamentRepository.findByIndisponible((short) 0);
        assertTrue(disponibles.stream().anyMatch(m -> m.getNom().equals("Doliprane")));
        assertFalse(disponibles.stream().anyMatch(m -> m.getNom().equals("Lévofloxacine")));
    }

    @Test
    public void testDispensaireByRegion() {
        // Création d'un dispensaire avec l'objet AdressePostale (conformément à l'UML)
        Dispensaire d = new Dispensaire("D001", "Pharmacie Centrale");
        AdressePostale addr = new AdressePostale("12 rue des Fleurs", "Toulouse", "Occitanie", "31000", "France");
        d.setAdresse(addr);
        dispensaireRepository.save(d);

        // Test de la recherche par région (imbriquée dans l'adresse)
        List<Dispensaire> result = dispensaireRepository.findByAdresseRegion("Occitanie");
        assertFalse(result.isEmpty());
        assertEquals("Pharmacie Centrale", result.get(0).getNom());
    }

    @Test
    public void testCommandeByDate() {
        Dispensaire d = dispensaireRepository.save(new Dispensaire("D002", "Dispensaire Nord"));

        // Date de référence : 1er Janvier 2024
        Calendar cal = Calendar.getInstance();
        cal.set(2024, Calendar.JANUARY, 1);
        Date datePivot = cal.getTime();

        // Commande après le pivot (Février 2024)
        cal.set(2024, Calendar.FEBRUARY, 1);
        Commande c = new Commande(cal.getTime());
        c.setDispensaire(d);
        commandeRepository.save(c);

        // Test findBySaisieLeAfter
        List<Commande> result = commandeRepository.findBySaisieLeAfter(datePivot);
        assertFalse(result.isEmpty(), "On devrait trouver la commande de Février");
    }

    @Test
    public void testContraintesIntegriteEtCascade() {
        // Test : Suppression en cascade avec dispensaire et commandes
        Dispensaire d = dispensaireRepository.save(new Dispensaire("D88", "Hopital"));
        
        // Initialiser la liste des commandes et ajouter la commande
        if (d.getCommandes() == null) {
            d.setCommandes(new java.util.ArrayList<>());
        }
        
        // Créer et ajouter la commande
        Commande c = new Commande(new Date());
        c.setDispensaire(d);
        d.getCommandes().add(c);
        commandeRepository.save(c);
        
        Integer commandeId = c.getNumero();
        
        // Supprimer le dispensaire - cela devrait supprimer les commandes en cascade
        dispensaireRepository.deleteById(d.getCode());
        
        // Vérifier que la commande a été supprimée par cascade
        assertTrue(commandeRepository.findById(commandeId).isEmpty(), "La commande aurait dû être supprimée par cascade");
    }

    @Test
    public void testRequetesMetierComplexes() {
        Categorie cat = categorieRepository.save(new Categorie("StatCat"));
        
        // Préparer un médicament disponible
        Medicament m1 = new Medicament("MedDispo", cat);
        m1.setUnitesEnStock((short) 10);
        m1.setUnitesCommandees((short) 2);
        m1.setIndisponible((short) 0);
        medicamentRepository.save(m1);

        // Préparer un médicament en rupture (stock < commandé)
        Medicament m2 = new Medicament("MedRupture", cat);
        m2.setUnitesEnStock((short) 1);
        m2.setUnitesCommandees((short) 5);
        m2.setIndisponible((short) 0);
        medicamentRepository.save(m2);

        // Test de la requête des médicaments disponibles à la commande
        List<Medicament> dispos = medicamentRepository.findDisponiblesParCategorie(cat.getCode());
        assertEquals(1, dispos.size());
        assertEquals("MedDispo", dispos.get(0).getNom());
    }

    @Test
    public void testArticlesEnvoyesEtEnCours() {
        Dispensaire d = dispensaireRepository.save(new Dispensaire("D10", "DispoTest"));
        
        // Commande envoyée
        Commande c1 = new Commande(new Date());
        c1.setEnvoyeeLe(new Date());
        c1.setDispensaire(d);
        commandeRepository.save(c1);

        // Commande en cours (envoyeeLe est nul)
        Commande c2 = new Commande(new Date());
        c2.setDispensaire(d);
        commandeRepository.save(c2);

        // Vérification des commandes en cours
        List<Commande> enCours = commandeRepository.findByDispensaireCodeAndEnvoyeeLeIsNull("D10");
        assertEquals(1, enCours.size());
    }
}