package pharmacie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AdressePostale {
    private String adresse;
    private String ville;
    private String region;
    @Column(name = "code_postal")
    private String codePostal;
    private String pays;
}