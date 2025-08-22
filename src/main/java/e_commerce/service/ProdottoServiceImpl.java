package e_commerce.service;

import e_commerce.dto.ProdottoRequest;
import e_commerce.dto.ProdottoResponse;
import e_commerce.mapper.ProdottoDTOMapper;
import e_commerce.model.Prodotto;
import e_commerce.repository.ProdottoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProdottoServiceImpl implements ProdottoService {

    private final ProdottoRepository prodottoRepository;
    private final ProdottoDTOMapper prodottoDTOMapper;

    @Override
    public List<Prodotto> getProdotti() {

        return prodottoRepository.findAll();
    }

    @Override
    public Optional<Prodotto> getProdottoById(int id) {
        return prodottoRepository.findById(id);
    }

    @Override
    public ProdottoResponse registraProdotto(ProdottoRequest prodottoRequest) {

        Prodotto prodotto = new Prodotto();

        prodotto.setName(prodottoRequest.getName());
        prodotto.setCategory(prodottoRequest.getCategory());
        prodotto.setDescription(prodottoRequest.getDescription());

        try {
            prodotto.setPrice(Double.parseDouble(prodottoRequest.getPrice()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Il prezzo fornito non è un numero valido.");
        }

        MultipartFile imagine = prodottoRequest.getImage();
        if (imagine != null && !imagine.isEmpty()) {
            try {
                String formato = imagine.getContentType();
                String immagineCodificata = "data:" + formato + ";base64," +
                        Base64.getEncoder().encodeToString(imagine.getBytes());
                prodotto.setImage(immagineCodificata);
            } catch (Exception e) {
                log.error("Errore durante la codifica dell'immagine: {}", e.getMessage());
                throw new RuntimeException("Impossibile elaborare il file dell'immagine.", e);
            }
        }

        Prodotto prodottoSalvato = prodottoRepository.save(prodotto);

        return prodottoDTOMapper.apply(prodottoSalvato);
    }

    @Override
    public void cancellaProdotto(int id) {

        if (!prodottoRepository.existsById(id)) {
            throw new RuntimeException("Prodotto non trovato con id: " + id);
        }
        prodottoRepository.deleteById(id);
    }

}
