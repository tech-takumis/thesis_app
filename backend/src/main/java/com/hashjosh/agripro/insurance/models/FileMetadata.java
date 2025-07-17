package com.hashjosh.agripro.insurance.models;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileMetadata {

    private boolean hasCoordinate;
    private String coordinate;
}
