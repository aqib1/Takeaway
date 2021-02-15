package org.got.takeaway.domain.base;

import lombok.*;
import org.springframework.http.HttpStatus;


@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseEntity<T> extends Base {
    private T body;
    private HttpStatus status;

    public int getStatusCode() {
        return getStatus().value();
    }
}
