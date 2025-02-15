package sub.librarymanagement.common.cache;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CacheMessage {
    private String cacheName;
    private String key;

    @JsonCreator
    public CacheMessage(
            @JsonProperty("cacheName") String cacheName,
            @JsonProperty("key") String key) {
        this.cacheName = cacheName;
        this.key = key;
    }

}