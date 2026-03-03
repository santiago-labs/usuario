package com.santiago_labs.usuario.business.converter;

import com.santiago_labs.usuario.business.dto.EnderecoDTO;
import com.santiago_labs.usuario.business.dto.TelefoneDTO;
import com.santiago_labs.usuario.business.dto.UsuarioDTO;
import com.santiago_labs.usuario.infrastructure.entity.Endereco;
import com.santiago_labs.usuario.infrastructure.entity.Telefone;
import com.santiago_labs.usuario.infrastructure.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioConverter {

    public Usuario paraUsuario(UsuarioDTO usuarioDTO){
        return Usuario.builder()
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha())
                .enderecos(paraListaEndereco(usuarioDTO.getEnderecos()))
                .telefones(paraListaTelefones(usuarioDTO.getTelefones()))
                .build();
    }
    public List<Endereco> paraListaEndereco(List<EnderecoDTO> enderecoDTOS){if (enderecoDTOS == null || enderecoDTOS.isEmpty()) {
        return List.of();
    }
        return enderecoDTOS.stream().map(this::paraEndereco).toList();
    }
    public Endereco paraEndereco (EnderecoDTO enderecoDTO){
        return Endereco.builder()
                .rua(enderecoDTO.getRua())
                .numero(enderecoDTO.getNumero())
                .cidade(enderecoDTO.getCidade())
                .cep(enderecoDTO.getCep())
                .estado(enderecoDTO.getEstado())
                .build();
    }
    public List<Telefone> paraListaTelefones(List<TelefoneDTO> telefoneDTOS){
        return telefoneDTOS.stream().map(this::paraTelefone).toList();
    }
    public Telefone paraTelefone( TelefoneDTO telefoneDTO){
       return Telefone.builder()
                .numero(telefoneDTO.getNumero())
                .ddd(telefoneDTO.getDdd())
                .build();
    }

    public UsuarioDTO paraUsuarioDTO(Usuario usuarioDTO){
        return UsuarioDTO.builder()
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha())
                .enderecos(paraListaEnderecoDTO(usuarioDTO.getEnderecos()))
                .telefones(paraListaTelefonesDTO(usuarioDTO.getTelefones()))
                .build();
    }
    public List<EnderecoDTO> paraListaEnderecoDTO(List<Endereco> enderecoDTOS){
        return enderecoDTOS.stream().map(this::paraEnderecoDTO).toList();
    }
    public EnderecoDTO paraEnderecoDTO (Endereco endereco){
        return EnderecoDTO.builder()
                .id(endereco.getId())
                .rua(endereco.getRua())
                .numero(endereco.getNumero())
                .complemento(endereco.getComplemento())
                .cidade(endereco.getCidade())
                .cep(endereco.getCep())
                .estado(endereco.getEstado())
                .build();
    }
    public List<TelefoneDTO> paraListaTelefonesDTO(List<Telefone> telefoneDTOS){
        return telefoneDTOS.stream().map(this::paraTelefoneDTO).toList();
    }
    public TelefoneDTO paraTelefoneDTO( Telefone telefone){
        return TelefoneDTO.builder()
                .id(telefone.getId())
                .numero(telefone.getNumero())
                .ddd(telefone.getDdd())
                .build();
    }

    public Usuario updateUsuario(UsuarioDTO usuarioDTO, Usuario entity){
        return Usuario.builder()
                .nome(usuarioDTO.getNome()!=null ? usuarioDTO.getNome(): entity.getNome())
                .id(entity.getId())
                .senha(usuarioDTO.getSenha()!=null ? usuarioDTO.getSenha(): entity.getSenha())
                .email(usuarioDTO.getEmail()!=null ? usuarioDTO.getEmail(): entity.getEmail())
                .enderecos(entity.getEnderecos())
                .telefones(entity.getTelefones())
                .build();
    }

    public Endereco updateEndereco (EnderecoDTO dto, Endereco entity){
        return Endereco.builder()
                .id(entity.getId())
                .rua(dto.getRua()!= null ? dto.getRua(): entity.getRua())
                .numero(dto.getNumero()!= null ?  dto.getNumero(): entity.getNumero())
                .complemento(dto.getComplemento()!= null ? dto.getComplemento(): entity.getComplemento())
                .cidade( dto.getCidade()!= null ? dto.getCidade(): entity.getCidade() )
                .estado(dto.getEstado() != null ? dto.getEstado(): entity.getEstado())
                .cep(dto.getCep()!= null ? dto.getCep(): entity.getCep())
                .build();
    }

    public Telefone updateTelefone(TelefoneDTO dto, Telefone entity){
        return Telefone.builder()
                .id(entity.getId())
                .ddd(dto.getDdd()!=null ? dto.getDdd(): entity.getDdd())
                .numero(dto.getNumero()!= null ? dto.getNumero(): entity.getNumero())
                .build();
    }
    public  Endereco paraEnderecoEntity (EnderecoDTO dto, Long idUsuario){
        return Endereco.builder()
                .rua(dto.getRua())
                .numero(dto.getNumero())
                .complemento(dto.getComplemento())
                .cidade(dto.getCidade())
                .cep(dto.getCep())
                .estado(dto.getEstado())
                .usuario_id(idUsuario)
                .build();
    }
    public Telefone paraTelefoneEntity(TelefoneDTO dto, Long idUsuario){
        return Telefone.builder()

                .numero(dto.getNumero())
                .ddd(dto.getDdd())
                .usuario_id(idUsuario)
                .build();
    }
}
