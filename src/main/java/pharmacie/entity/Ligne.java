package pharmacie.entity;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Ligne {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE)
    private int id;

    @ToString.Exclude
	@PositiveOrZero
    private int commande_numero;

    @ToString.Exclude
	@PositiveOrZero
    private int medicament_reference;

    @ToString.Exclude
	@PositiveOrZero
    private int quantite;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medicament_reference")
    private Medicament medicament;

    @ManyToOne(optional = false)
    @JoinColumn(name = "commande_numero")
    private Commande commande;
}
