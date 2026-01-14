package pharmacie.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Getter @Setter @NoArgsConstructor @RequiredArgsConstructor @ToString
public class Commande {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE)
    private int numero;

    @NonNull
    private Date envoyeeLe;

    @ToString.Exclude
    private BigDecimal port;

    @ToString.Exclude
    private BigDecimal remise;
    
    @NonNull
    private Date saisieLe;

    @Column(length = 5)
    private String dispensaire_code;

    @Column(length = 10)
    private String code_Postal;

    @Column(length = 15)
    private String pays;

    @Column(length = 15)
    private String region;

    @Column(length = 15)
    private String ville;

    @Column(length = 40)
    private String destinataire;

    @Column(length = 60)
    private String adresse;

    @ManyToOne(optional = false)
    @ToString.Exclude
    @JoinColumn(name = "dispensaire_code", referencedColumnName = "code")
    private Dispensaire dispensaire;
}
