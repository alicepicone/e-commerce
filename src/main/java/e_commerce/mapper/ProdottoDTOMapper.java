package e_commerce.mapper;

import e_commerce.dto.ProdottoResponse;
import e_commerce.model.Prodotto;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ProdottoDTOMapper implements Function<Prodotto, ProdottoResponse> {

    @Override
    public ProdottoResponse apply(Prodotto prodotto) {
        return new ProdottoResponse(
                prodotto.getName(),
                prodotto.getCategory(),
                prodotto.getPrice()
        );
    }
}