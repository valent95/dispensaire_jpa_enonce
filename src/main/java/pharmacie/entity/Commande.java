package pharmacie.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor @RequiredArgsConstructor @ToString
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer numero;

    @NonNull
    private Date saisieLe;

    private Date envoyeeLe;

    private BigDecimal port;

    private BigDecimal remise;

    private String destinataire;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name="adresse", column=@Column(name="livraison_adresse")),
        @AttributeOverride(name="ville", column=@Column(name="livraison_ville")),
        @AttributeOverride(name="region", column=@Column(name="livraison_region")),
        @AttributeOverride(name="codePostal", column=@Column(name="livraison_cp")),
        @AttributeOverride(name="pays", column=@Column(name="livraison_pays"))
    })
    private AdressePostale adresseLivraison;

    @ManyToOne(optional = false)
    @ToString.Exclude
    @JoinColumn(name = "dispensaire_code")
    private Dispensaire dispensaire;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Ligne> lignes;
}