package com.tpe.cookerytech.dto.response;

import com.tpe.cookerytech.domain.Model;
import com.tpe.cookerytech.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteResponse {

    private Long id;

    private Model model;

    private User user;

    private LocalDateTime create_at;
}
