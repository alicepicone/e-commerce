package e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class ProdottoRequest {

    private String name;

    private String category;

    private String description;

    private String price;

    private MultipartFile image;
}
