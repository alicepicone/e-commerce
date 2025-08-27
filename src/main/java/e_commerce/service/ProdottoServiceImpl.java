package e_commerce.service;

import e_commerce.dto.ProdottoRequest;
import e_commerce.dto.ProdottoResponse;
import e_commerce.mapper.ProdottoDTOMapper;
import e_commerce.model.Prodotto;
import e_commerce.repository.ProdottoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProdottoServiceImpl implements ProdottoService {

    private final ProdottoRepository prodottoRepository;
    private final ProdottoDTOMapper prodottoDTOMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

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

        String nomeFileImmagine = salvaImmagine(prodottoRequest.getImage());
        prodotto.setImage(nomeFileImmagine);

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

    public ProdottoResponse aggiornaProdotto(Long id, ProdottoRequest prodottoRequest) {

        Prodotto prodotto = getProdottoById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato con id: " + id));

        if (prodottoRequest.getName() != null && !prodottoRequest.getName().isBlank()) {
            prodotto.setName(prodottoRequest.getName());
        }

        if (prodottoRequest.getCategory() != null && !prodottoRequest.getCategory().isBlank()) {
            prodotto.setCategory(prodottoRequest.getCategory());
        }

        if (prodottoRequest.getDescription() != null && !prodottoRequest.getDescription().isBlank()) {
            prodotto.setDescription(prodottoRequest.getDescription());
        }

        if (prodottoRequest.getPrice() != null && !prodottoRequest.getPrice().isBlank()) {
            try {
                prodotto.setPrice(Double.parseDouble(prodottoRequest.getPrice()));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Il prezzo fornito non è un numero valido.");
            }
        }

        if (prodottoRequest.getImage() != null && !prodottoRequest.getImage().isEmpty()) {
            String nomeFileImmagine = salvaImmagine(prodottoRequest.getImage());
            prodotto.setImage(nomeFileImmagine);
        }

        Prodotto prodottoAggiornato = prodottoRepository.save(prodotto);

        return prodottoDTOMapper.apply(prodottoAggiornato);
    }

    private String salvaImmagine(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }
        try {
            String originalFilename = file.getOriginalFilename();
            String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;

            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath);

            return uniqueFilename;
        } catch (Exception e) {
            log.error("Impossibile salvare il file: {}", e.getMessage());
            throw new RuntimeException("Impossibile salvare il file caricato: " + file.getOriginalFilename(), e);
        }
    }
}
