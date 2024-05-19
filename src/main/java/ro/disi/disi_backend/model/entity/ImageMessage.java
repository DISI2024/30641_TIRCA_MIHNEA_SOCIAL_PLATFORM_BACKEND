package ro.disi.disi_backend.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Type;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ImageMessage extends Message {

    @Column(name = "image_data", columnDefinition = "bytea")
    private byte[] imageData;

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}
