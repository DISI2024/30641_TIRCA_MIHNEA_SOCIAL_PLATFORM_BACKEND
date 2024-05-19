package ro.disi.disi_backend.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class VocalMessage extends Message {

    @Column(name = "sound_data", columnDefinition = "bytea")
    private byte[] soundData;

    public byte[] getSoundData() {
        return soundData;
    }

    public void setSoundData(byte[] soundData) {
        this.soundData = soundData;
    }
}
