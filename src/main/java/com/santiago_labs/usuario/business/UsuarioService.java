package com.santiago_labs.usuario.business;

import com.santiago_labs.usuario.business.converter.UsuarioConverter;
import com.santiago_labs.usuario.business.dto.UsuarioDTO;
import com.santiago_labs.usuario.infrastructure.entity.Usuario;
import com.santiago_labs.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(
                usuarioRepository.save(usuario)
        );
    }
}
