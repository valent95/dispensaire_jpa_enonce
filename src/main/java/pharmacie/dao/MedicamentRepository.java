package pharmacie.dao;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pharmacie.entity.Medicament;

public interface MedicamentRepository extends JpaRepository<Medicament, Integer> {
    Optional<Medicament> findByNom(String nom);
    // Recherche par valeur numérique (0 pour disponible, 1 pour indisponible)
    List<Medicament> findByIndisponible(short status);

    // Calcul des unités commandées par catégorie
    @Query("SELECT l.medicament.nom as nom, SUM(l.quantite) AS unites " +
           "FROM Ligne l " +
           "WHERE l.medicament.categorie.code = :codeCategorie " +
           "GROUP BY l.medicament.nom")
    List<UnitesParMedicament> unitesCommandeesParCategorie(Integer codeCategorie);

    // Médicaments disponibles (non indisponibles et stock >= commandés)
    @Query("SELECT m FROM Medicament m " +
           "WHERE m.categorie.code = :codeCat " +
           "AND m.indisponible = 0 " +
           "AND m.unitesEnStock >= m.unitesCommandees")
    List<Medicament> findDisponiblesParCategorie(Integer codeCat);
}
