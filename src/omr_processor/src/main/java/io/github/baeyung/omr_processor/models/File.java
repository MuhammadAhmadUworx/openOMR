package io.github.baeyung.omr_processor.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class File
{
    String fileName;
    byte[] file;
}
