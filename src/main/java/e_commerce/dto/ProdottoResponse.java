package e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProdottoResponse {

    private String name;

    private String category;

    private double price;
}
