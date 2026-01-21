package pharmacie.entity;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor @RequiredArgsConstructor @ToString
public class Medicament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer reference;

    @NonNull
    @Column(unique=true, length = 255)
    private String nom;

    private String quantiteParUnite = "Une boîte de 12";

    @PositiveOrZero
    private BigDecimal prixUnitaire = BigDecimal.TEN;

    @ToString.Exclude
    private short unitesEnStock = 0;

    @ToString.Exclude
    private short unitesCommandees = 0;

    @ToString.Exclude
    private short niveauDeReappro = 0;

    @ToString.Exclude
    private short indisponible = 0; // 0 pour faux, 1 pour vrai selon UML

    @Column(length = 500)
    private String imageURL;

    @ManyToOne(optional = false)
    @NonNull
    @ToString.Exclude
    private Categorie categorie;

    @OneToMany(mappedBy = "medicament")
    @ToString.Exclude
    private List<Ligne> lignes;
}