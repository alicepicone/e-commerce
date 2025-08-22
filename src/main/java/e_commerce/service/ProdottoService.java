package e_commerce.service;

import e_commerce.dto.ProdottoRequest;
import e_commerce.dto.ProdottoResponse;
import e_commerce.model.Prodotto;

import java.util.List;
import java.util.Optional;

public interface ProdottoService {

    List<Prodotto> getProdotti();

    Optional<Prodotto> getProdottoById(int id);

    ProdottoResponse registraProdotto(ProdottoRequest prodottoRequest);

    void cancellaProdotto(int id);

}
