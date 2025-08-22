package e_commerce.api;

import e_commerce.dto.ProdottoRequest;
import e_commerce.dto.ProdottoResponse;
import e_commerce.model.Prodotto;
import e_commerce.service.ProdottoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/v1/prodotti")
@RestController
@RequiredArgsConstructor
public class ProdottoController {

    private final ProdottoService prodottoService;

    @GetMapping
    public List<Prodotto> getAllProducts() {

        return prodottoService.getProdotti();
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ProdottoResponse createProduct(@ModelAttribute ProdottoRequest prodottoRequest) {

        return prodottoService.registraProdotto(prodottoRequest);
    }

    @DeleteMapping("/{id}")
    public void eliminaProdotto(@PathVariable int id) {

        prodottoService.cancellaProdotto(id);
    }

}
