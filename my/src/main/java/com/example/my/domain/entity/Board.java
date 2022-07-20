package com.example.my.domain.entity;

import com.google.cloud.storage.BlobId;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.modelmapper.internal.bytebuddy.implementation.bind.annotation.Default;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Board extends TimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 4)
    private String title;

    @NotNull
    @Size(min = 10)
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    private Account account;

    @Column
    private int isImg;

    @Column
    private String bucket;

    @Column
    private String blobName;
}
