package com.example.serviceuser.pojo;


import lombok.*;
import lombok.Data;
import org.springframework.aot.generate.GenerationContext;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document("File")
public class Files {
    @Id

    private String id;
    private String name;
    private String type;
    private String filePath;
    private byte[] imageData;





}
