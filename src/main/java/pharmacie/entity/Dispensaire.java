package pharmacie.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor @RequiredArgsConstructor @ToString
public class Dispensaire {
    @Id
    @Column(length = 5)
    @NonNull
    private String code;

    @NonNull
    private String nom;
    
    private String contact;
    private String fonction;
    private String telephone;
    private String fax;

    @Embedded
    private AdressePostale adresse;

    @OneToMany(mappedBy = "dispensaire")
    @ToString.Exclude
    private List<Commande> commandes;
}