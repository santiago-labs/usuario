package com.santiago_labs.usuario.business;

import com.santiago_labs.usuario.business.converter.UsuarioConverter;
import com.santiago_labs.usuario.business.dto.EnderecoDTO;
import com.santiago_labs.usuario.business.dto.TelefoneDTO;
import com.santiago_labs.usuario.business.dto.UsuarioDTO;
import com.santiago_labs.usuario.infrastructure.entity.Endereco;
import com.santiago_labs.usuario.infrastructure.entity.Telefone;
import com.santiago_labs.usuario.infrastructure.entity.Usuario;
import com.santiago_labs.usuario.infrastructure.exceptions.ConflictException;
import com.santiago_labs.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.santiago_labs.usuario.infrastructure.repository.EnderecoRepository;
import com.santiago_labs.usuario.infrastructure.repository.TelefoneRepository;
import com.santiago_labs.usuario.infrastructure.repository.UsuarioRepository;
import com.santiago_labs.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder PasswordEncoder;
    private final JwtUtil jwtUtil;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(PasswordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(
                usuarioRepository.save(usuario)
        );
    }
    public void emailExiste(String email){
        try{
            boolean existe = verificarEmailExistente(email);
            if (existe){
                throw new ConflictException("Email já cadastrado" + email);
            }
        }catch (ConflictException e){
            throw new ConflictException("Email já cadastrado", e.getCause());
        }
    }
    public boolean verificarEmailExistente(String email){
        return usuarioRepository.existsByEmail(email);
    }

    public UsuarioDTO buscarUsuarioPorEmail(String email){
        try {
            return usuarioConverter.paraUsuarioDTO(
                    usuarioRepository.findByEmail(email)
                    .orElseThrow(
                    ()-> new ResourceNotFoundException("Email não encontrado " + email))
            );
    }catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException( "Email não encontrado " + email);
        }
    }

    public void deletaUsuarioPorEmail(String email){
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizadDadosUsuario(String token, UsuarioDTO dto){
        //Busca o email do usuario através do token
        String email = jwtUtil.extrairEmailToken(token.substring(7));
        //Criptografia na senha
        dto.setSenha(dto.getSenha() != null ? PasswordEncoder.encode(dto.getSenha()) : null);
        //Busca os dados do usuario no banco de dados
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("email nao localizado" + email));
        //Mesclar os dados recebidos pela requisição DTO com os dados do banco de dados
        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);
        //Salvando os dados do usuario convertido e pegando o retorno pra converter em usuarioDTO
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO){
        Endereco entity = enderecoRepository.findById(idEndereco).orElseThrow(() ->
                new ResourceNotFoundException("Id não encontrado" + idEndereco));

        Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO, entity);

        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
    }
    public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO dto){

        Telefone entity = telefoneRepository.findById(idTelefone).orElseThrow(() ->
                new ResourceNotFoundException("Id não encontrado" + idTelefone));
        Telefone telefone = usuarioConverter.updateTelefone(dto, entity);
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
    }

    public EnderecoDTO cadastraEndereco(String token, EnderecoDTO dto){
        String email = jwtUtil.extrairEmailToken(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("email não localizado" + email));
        Endereco endereco = usuarioConverter.paraEnderecoEntity(dto, usuario.getId());
        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
    }
    public TelefoneDTO cadastraTelefone(String token, TelefoneDTO dto){
        String email = jwtUtil.extrairEmailToken(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("email não localizado" + email));
        Telefone telefoneEntity = usuarioConverter.paraTelefoneEntity(dto, usuario.getId());
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefoneEntity));
    }
}
