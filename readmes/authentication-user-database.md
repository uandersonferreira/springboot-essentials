# Autenticação de Usuários no banco de dados com Spring Security 6.xx

## 1° Passo - Criar o UserDetails

Criar uma classe que irá representar o Usuário que será usado no processo de autenticação do spring security, para isso segue a representa do diagrama.

Neste diagrama, a classe Usuario implementa UserDetails( já implementa o Serializable ), e os métodos sobrescritos da interface UserDetails retornam true.

```mermaid
classDiagram
    class Usuario {
        <<UserDetails>>
        +String username
        +String password
        +Collection<GrantedAuthority> authorities
        +boolean isAccountNonExpired()
        +boolean isAccountNonLocked()
        +boolean isCredentialsNonExpired()
        +boolean isEnabled()
        +String getUsername()
        +String getPassword()
        +Collection<GrantedAuthority> getAuthorities()
    }
    
    note for Usuario "Anotações: @Entity, @Table, @Id, @Column"

    Usuario : isAccountNonExpired() *-- true
    Usuario : isAccountNonLocked() *-- true
    Usuario : isCredentialsNonExpired() *-- true
    Usuario : isEnabled() *-- true

   style Usuario color:#FFFFFF, stroke:#00C853, fill:#00C853

   style E color:#FFFFFF, fill:#AA00FF, stroke:#AA00FF
   style I color:#FFFFFF, stroke:#2962FF, fill:#2962FF
```

## 2° - Passo - Criar o Repository do UserDetails

Devemos criar o repository do userDetails com o método
`findByName(String username)`

